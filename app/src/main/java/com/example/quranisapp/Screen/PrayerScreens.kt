package com.example.quranisapp.Screen

import android.Manifest
import android.location.Geocoder
import android.location.Location
import android.os.Parcelable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quranisapp.R
import com.example.quranisapp.service.ApiInterface
import com.example.quranisapp.service.api.Time
import com.example.quranisapp.service.location.LocationService
import com.example.quranisapp.service.location.LocationServiceCondition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun PrayerScreens(
    back: () -> Unit
) {
    val context = LocalContext.current

    val geoCoder = Geocoder(context)
    val locationCLient = LocationServices.getFusedLocationProviderClient(context)
    val locationState = remember { MutableStateFlow<LocationServiceCondition<Location?>?>(null) }
    val locationService = LocationService(
        locationCLient, context.applicationContext
    )

    val locationPermission = rememberMultiplePermissionsState(
        permissions = listOf(
            //buat manifest pastiin yg android
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    if (!locationPermission.allPermissionsGranted) {
        LaunchedEffect(key1 = true) {
            locationPermission.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(key1 = true) {
        locationState.emit(locationService.getCurrentLocation())
    }

    LaunchedEffect(key1 = locationPermission.allPermissionsGranted) {
        locationState.emit(locationService.getCurrentLocation())
    }

    val scope = rememberCoroutineScope()

    val api = ApiInterface.createApi()
    val prayerTime = remember {
        mutableStateListOf<Time?>()
    }

    var cityLocation by remember { mutableStateOf("") }
    var provinceLocation by remember { mutableStateOf("") }

    val timeSholatButton = mutableListOf<ItemTimePrayer>()


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { back() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                },
                title = { Text(text = "Prayer Timings", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.blue)),
            )
        }
    ) {
        LazyColumn(
            Modifier
                .padding(it)
                .background(colorResource(id = R.color.white_background))
        ) {
            //next prayer
            item {
                Spacer(modifier = Modifier.padding(top = 16.dp))
                PermissionsRequired(
                    multiplePermissionsState = locationPermission,
                    permissionsNotGrantedContent = {
                        //ketika permission ditolak
                        cityLocation = "Location is Rejected"
                    },
                    permissionsNotAvailableContent = {
                        //ketika permisioan ditolak dan
                        cityLocation = "Location is Rejected"
                    },
                ) {
                    locationState.collectAsState().let { state ->
                        when (val locationCondition = state.value) {
                            is LocationServiceCondition.Error -> {
                                cityLocation = "Location error"
                            }

                            is LocationServiceCondition.MissingPermission -> {
                                cityLocation = "Missing location"
                            }

                            is LocationServiceCondition.NoGps -> {
                                cityLocation = "Gps Not Activated"
                            }

                            is LocationServiceCondition.Success -> {
                                val location = locationCondition.location

                                scope.launch {
                                    val result = api.getJadwalSholat(
                                        location?.latitude.toString(),
                                        location?.longitude.toString()
                                    )
                                    prayerTime.clear()
                                    prayerTime.addAll(result.times)
                                }

                                val locationList = location?.let {
                                    geoCoder.getFromLocation(
                                        it.latitude,
                                        it.longitude,
                                        1
                                    )
                                }
                                cityLocation = locationList?.get(0)?.locality ?: ""
                                provinceLocation = locationList?.get(0)?.subAdminArea ?: ""

                                val listJudulSholat = listOf(
                                    "Shubuh",
                                    "Dzuhur",
                                    "Ashar",
                                    "Maghrib",
                                    "Isya"
                                )

                                if (prayerTime.isNotEmpty()){
                                    timeSholatButton.clear()
                                    for (i in listJudulSholat.indices){
                                        val dataJadwalSholat = ItemTimePrayer(
                                            listJudulSholat[i],
                                            listOf(
                                                prayerTime[0]?.fajr,
                                                prayerTime[0]?.dhuhr,
                                                prayerTime[0]?.asr,
                                                prayerTime[0]?.maghrib,
                                                prayerTime[0]?.isha
                                            )[i].toString()
                                        )
                                        timeSholatButton.add(dataJadwalSholat)
                                    }
                                }
                            }

                            null -> {
                                cityLocation = "Location error"
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(10))
                        .clickable { },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 16.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.white)
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(
                            modifier = Modifier
                                .padding(vertical = 30.dp)
                                .padding(start = 20.dp)
                        ) {
                            Text(text = "Next Prayer", fontSize = 20.sp)
                            Text(
                                text = "11.30",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = colorResource(id = R.color.blue)
                            )
                            if (prayerTime.isNotEmpty()) {
                                Spacer(modifier = Modifier.size(20.dp))
                                Text(text = "${prayerTime[0]?.gregorian}", fontSize = 20.sp)
                                Spacer(modifier = Modifier.size(20.dp))
                                Text(
                                    modifier = Modifier.width(160.dp),
                                    text = "$cityLocation, $provinceLocation",
                                    fontSize = 20.sp
                                )
                            }
                        }
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = "",
                            Modifier
                                .weight(9f / 16f, fill = false)
                                .fillMaxWidth()
                        )
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(timeSholatButton){it->
                            Button(
                                onClick = { /*TODO*/ }, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                            ) {
                                Text(text = "${it.judulSholat}, ${it.waktuSholat}", modifier = Modifier.padding(start = 16.dp))
                            }
                        }
                    }
                }
            }
            //read hadist
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(10)),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.white)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 30.dp, horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Studying Hadist",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.blue)
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Button(
                            onClick = { /*TODO*/ }, modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Done, contentDescription = "")
                            Text(text = "Hadist", modifier = Modifier.padding(start = 16.dp))
                        }
                        Button(
                            onClick = { /*TODO*/ }, modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Text(text = "Hadist", modifier = Modifier.padding(start = 16.dp))
                        }
                        Button(
                            onClick = { /*TODO*/ }, modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Text(text = "Hadist", modifier = Modifier.padding(start = 16.dp))
                        }
                        Button(
                            onClick = { /*TODO*/ }, modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Text(text = "Hadist", modifier = Modifier.padding(start = 16.dp))
                        }
                        Button(
                            onClick = { /*TODO*/ }, modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Text(text = "Hadist", modifier = Modifier.padding(start = 16.dp))
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                        Row {
                            Text(
                                text = "Last Check:  ",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "9.03.2023",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Parcelize
data class ItemTimePrayer(
    val waktuSholat : String,
    val judulSholat : String
) : Parcelable