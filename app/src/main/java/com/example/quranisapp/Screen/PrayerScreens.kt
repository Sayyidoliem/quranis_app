package com.example.quranisapp.Screen

import android.Manifest
import android.location.Geocoder
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quranisapp.R
import com.example.quranisapp.bottomscreens.CardNextPrayer
import com.example.quranisapp.bottomscreens.CardNextPrayerNotPermission
import com.example.quranisapp.service.location.LocationService
import com.example.quranisapp.service.location.LocationServiceCondition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun PrayerScreens(
    back: () -> Unit
) {
    val context = LocalContext.current

    val geoCoder = Geocoder(context)
    val locationCLient = LocationServices.getFusedLocationProviderClient(context)
    val locationState = MutableStateFlow<LocationServiceCondition<Location?>?>(null)
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

    LaunchedEffect(key1 = locationPermission.allPermissionsGranted) {
        locationState.emit(locationService.getCurrentLocation())
    }

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
                .background(colorResource(id = R.color.white_background))) {
            //next prayer
            item {
                Spacer(modifier = Modifier.padding(top = 16.dp))
                PermissionsRequired(
                    multiplePermissionsState = locationPermission,
                    permissionsNotGrantedContent = {
                        //ketika permission ditolak
                        CardNextPrayerNotPermission(
                            nav = {  },
                            location = "Location is reject"
                        )
                    },
                    permissionsNotAvailableContent = {
                        //ketika permisioan ditolak dan
                        CardNextPrayerNotPermission(
                            nav = {  },
                            location = "Location is rejected"
                        )
                    },
                ) {
                    locationState.collectAsState().let { state ->
                        when (val locationCondition = state.value) {
                            is LocationServiceCondition.Error -> {
                                CardNextPrayerNotPermission(
                                    nav = {  },
                                    location = "Location Error"
                                )
                            }

                            is LocationServiceCondition.MissingPermission -> {
                                CardNextPrayerNotPermission(
                                    nav = {  },
                                    location = "Missing location"
                                )
                            }

                            is LocationServiceCondition.NoGps -> {
                                CardNextPrayerNotPermission(
                                    nav = {  },
                                    location = "Gps not activated"
                                )
                            }

                            is LocationServiceCondition.Success -> {
                                val location = locationCondition.location

                                val locationList = location?.let {
                                    geoCoder.getFromLocation(
                                        it.latitude,
                                        it.longitude,
                                        1
                                    )
                                }
                                CardNextPrayer(
                                    nav = {},
                                    location = locationList?.get(0)?.countryCode
                                )
                            }

                            null -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                    }
                }
            }
            //today pray
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
                            text = "Today Pray",
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
                            Text(text = "Shubuh", modifier = Modifier.padding(start = 16.dp))
                        }
                        Button(
                            onClick = { /*TODO*/ }, modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Text(text = "Dzhuhur", modifier = Modifier.padding(start = 16.dp))
                        }
                        Button(
                            onClick = { /*TODO*/ }, modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Text(text = "Ashar", modifier = Modifier.padding(start = 16.dp))
                        }
                        Button(
                            onClick = { /*TODO*/ }, modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Text(text = "Maghrib", modifier = Modifier.padding(start = 16.dp))
                        }
                        Button(
                            onClick = { /*TODO*/ }, modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Text(text = "Isya", modifier = Modifier.padding(start = 16.dp))
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
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
                            Text(text = "Last Check:  ", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = "9.03.2023", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}