package com.example.quranisapp.service.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class LocationService(
    private val client: FusedLocationProviderClient,
    private val application: Context
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getCurrentLocation(): LocationServiceCondition<Location?> {

        //buat pastiin dapet ijin dari aplikasi
        val isFineLocationPermitted = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        )== PackageManager.PERMISSION_GRANTED

        val isCoarseLocationPermited = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager = application.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        val isGpsEnabled =
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGpsEnabled){
            return LocationServiceCondition.NoGps()
        }

        if (!(isFineLocationPermitted || isCoarseLocationPermited)){
            return LocationServiceCondition.MissingPermission()
        }

        return suspendCancellableCoroutine {coroutine->
            client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)//buat akurasi lokasi
                .apply {
                    if (isComplete){
                        if (isSuccessful){
                            coroutine.resume(LocationServiceCondition.Success(result)){}
                        }else{
                            coroutine.resume(LocationServiceCondition.Error()){}
                        }
                        return@suspendCancellableCoroutine
                    }
                    addOnCanceledListener {
                        coroutine.cancel()
                    }
                    addOnSuccessListener {
                        coroutine.resume(LocationServiceCondition.Success(result)){}
                    }
                    addOnFailureListener {
                        coroutine.resume(LocationServiceCondition.Error()){}
                    }
                }

        }
    }
}