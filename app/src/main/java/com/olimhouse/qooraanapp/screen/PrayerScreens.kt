package com.olimhouse.qooraanapp.screen

import android.Manifest
import android.location.Geocoder
import android.location.Location
import android.os.Parcelable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.olimhouse.qooraanapp.R
import com.olimhouse.qooraanapp.data.kotpref.SettingPreferences
import com.olimhouse.qooraanapp.service.ApiInterface
import com.olimhouse.qooraanapp.service.api.Time
import com.olimhouse.qooraanapp.service.location.LocationService
import com.olimhouse.qooraanapp.service.location.LocationServiceCondition
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun PrayerScreens(
    openDrawer: () -> Unit
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
                    IconButton(onClick = { openDrawer() }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                title = {
                    Text(
                        text = when (SettingPreferences.isSelectedLanguage) {
                            SettingPreferences.INDONESIA -> {
                                "Jadwal Ibadah"
                            }

                            else -> {
                                "Prayer Timings"
                            }
                        },
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            item {
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
                                cityLocation =when (SettingPreferences.isSelectedLanguage) {
                                    SettingPreferences.INDONESIA -> {
                                        "Lokasi bermasalah"
                                    }

                                    else -> {
                                        "Location Error"
                                    }
                                }
                            }

                            is LocationServiceCondition.MissingPermission -> {
                                cityLocation = when (SettingPreferences.isSelectedLanguage) {
                                    SettingPreferences.INDONESIA -> {
                                        "Dimohon untuk aktifkan izin "
                                    }

                                    else -> {
                                        "Please Enabled your permission"
                                    }
                                }
                            }

                            is LocationServiceCondition.NoGps -> {
                                cityLocation = when (SettingPreferences.isSelectedLanguage) {
                                    SettingPreferences.INDONESIA -> {
                                        "Dimohon untuk aktifkan GPS anda "
                                    }

                                    else -> {
                                        "Please activated your GPS"
                                    }
                                }
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

                                if (prayerTime.isNotEmpty()) {
                                    timeSholatButton.clear()
                                    for (i in listJudulSholat.indices) {
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

                            else -> {}
                        }
                    }
                }

                val dateFlow = getTime().collectAsState(initial = "")
                Text(
                    text = dateFlow.value,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.surfaceTint,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                when {
                    prayerTime.isNotEmpty() -> {
                        Spacer(modifier = Modifier.size(20.dp))
                        Text(text = "Date : ${prayerTime[0]?.gregorian}",modifier = Modifier.padding(start = 16.dp), fontSize = 20.sp)
                        Spacer(modifier = Modifier.size(16.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                            text = "$cityLocation, \n$provinceLocation",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.size(20.dp))
                    }

                    else -> {
                        Spacer(modifier = Modifier.size(20.dp))
                        LinearProgressIndicator(Modifier.fillMaxWidth().padding(16.dp))
                    }
                }
                timeSholatButton.forEach { item ->
                    ItemCardPrayer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                            .clip(shape = RoundedCornerShape(10.dp)),
                        titleSholat = item.judulSholat,
                        timesholat = item.waktuSholat
                    )
                }
            }
            item {
                Row(
                    verticalAlignment = Alignment.Bottom, modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 56.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Bottom)
                            .fillMaxWidth(),
                        text = "QOORAAN",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily(Font(R.font.brunoace))
                    )
                }
            }
        }
    }
}

@Parcelize
data class ItemTimePrayer(
    val waktuSholat: String,
    val judulSholat: String
) : Parcelable

@Composable
fun ItemCardPrayer(
    modifier: Modifier = Modifier,
    titleSholat: String,
    timesholat: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = timesholat,
                modifier = Modifier.padding(12.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = titleSholat,
                modifier = Modifier.padding(12.dp),
                fontWeight = FontWeight.Bold
            )
        }

    }
}

private fun getTime() = flow {
    while (true) {
        val date = SimpleDateFormat(
            "HH:mm:ss",
            Locale.getDefault()
        ).format(System.currentTimeMillis())
        emit(date)
        delay(1000)
    }
}
//read hadist
/*
item {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(10)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
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
 */