package com.example.quranisapp

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quranisapp.Screen.PrayerScreens
import com.example.quranisapp.Screen.QuranScreens
import com.example.quranisapp.bottomscreens.BookmarkScreens
import com.example.quranisapp.bottomscreens.HomeScreens
import com.example.quranisapp.bottomscreens.ProfileScreens
import com.example.quranisapp.bottomscreens.SettingScreens
import com.example.quranisapp.navigation.Screen
import com.example.quranisapp.service.location.LocationService
import com.example.quranisapp.service.location.LocationServiceCondition
import com.example.quranisapp.tabrowscreens.AyatScreens
import com.example.quranisapp.ui.theme.QURANISAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MainActivity() {
    QURANISAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            val navController: NavHostController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            var currentRoute: String? = navBackStackEntry?.destination?.route

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
                modifier = Modifier,
                bottomBar = {
                    if (bottomNavItemList.any {
                            it.route == currentRoute
                        }
                    )
                        NavigationBar(
                            modifier = Modifier.clip(
                                RoundedCornerShape(
                                    topStart = 30.dp,
                                    topEnd = 30.dp
                                )
                            )
                        ) {
                            bottomNavItemList.map { item: BottomNavItem ->
                                NavigationBarItem(
                                    selected = currentRoute == item.route,//untuk membandingkan index
                                    onClick = {
                                        currentRoute = item.route
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState =
                                                    true //buat menyimpan state mutable di setiap screen
                                            }
                                            restoreState = true
                                            launchSingleTop =
                                                true//buat klo back, langsung keluar app(bukan ke home)
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = item.iconBottom,
                                            contentDescription = item.label
                                        )
                                    },
                                    label = { Text(text = item.label) },
                                )
                            }
                        }
                }
            ) { innerPadding ->
                PermissionsRequired(
                    multiplePermissionsState = locationPermission,
                    permissionsNotGrantedContent = {
                        HomeScreens(
                            goToPrayTime = {},
                            countryLocation = "",
                            provinceLocation = ""
                        )
                    },
                    permissionsNotAvailableContent = {
                        HomeScreens(
                            goToPrayTime = {},
                            countryLocation = "",
                            provinceLocation = ""
                        )
                    },
                ) {//ketika permision dipenuhi
                    locationState.collectAsState().let { state ->
                        when (val locationCondition = state.value) {
                            is LocationServiceCondition.Error -> {
                                HomeScreens(
                                    goToPrayTime = {},
                                    countryLocation = "error",
                                    provinceLocation = "error"
                                )
                            }

                            is LocationServiceCondition.MissingPermission -> {
                                HomeScreens(
                                    goToPrayTime = {},
                                    countryLocation = "error",
                                    provinceLocation = "error"
                                )
                            }

                            is LocationServiceCondition.NoGps -> {
                                HomeScreens(
                                    goToPrayTime = {},
                                    countryLocation = "GPS not found",
                                    provinceLocation = "GPS not found"
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
                                HomeScreens(
                                    goToPrayTime = {  },
                                    countryLocation = "location ${locationList!![0]!!.countryName}",
                                    provinceLocation = ""
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
                NavHost(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreens(
                            goToPrayTime = { navController.navigate("time") },
                            countryLocation = "",
                            provinceLocation = "",
                        )
                    }
                    composable("time") {
                        PrayerScreens(back = { navController.navigateUp() })
                    }
                    composable(Screen.Read.route) {
                        QuranScreens(
                            goToRead = { surahNumber, juzNumber, pageNumber ->
                                navController.navigate(
                                    Screen.Detail.createRoute(
                                        surahNumber = surahNumber,
                                        juzNumber = juzNumber,
                                        pageNumber = pageNumber
                                    )
                                )
                            },
                        )
                    }
                    composable(Screen.Detail.route, arguments = listOf(
                        navArgument("surahNumber") {
                            type = NavType.IntType
                            defaultValue = -1
                        },
                        navArgument("juzNumber") {
                            type = NavType.IntType
                            defaultValue = -1
                        },
                        navArgument("pageNumber") {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
                    ) {
                        val surahNumber = it.arguments?.getInt("surahNumber") ?: -1
                        val juzNumber = it.arguments?.getInt("juzNumber") ?: -1
                        val pageNumber = it.arguments?.getInt("pageNumber") ?: -1
                        AyatScreens(
                            goBack = { navController.navigateUp() },
                            surahNumber = surahNumber,
                            juzNumber = juzNumber,
                            pageNumber = pageNumber
                        )
                    }
                    composable("bookmark") {
                        BookmarkScreens(
                            goToRead = { surahNumber, juzNumber, pageNumber ->
                                navController.navigate(
                                    Screen.Detail.createRoute(
                                        surahNumber = surahNumber,
                                        juzNumber = juzNumber,
                                        pageNumber = pageNumber
                                    )
                                )
                            },
                        )
                    }
                    composable("setting") {
                        SettingScreens(goToProfile = { navController.navigate("profile") })
                    }
                    composable("profile") {
                        ProfileScreens(goBack = { navController.navigateUp() })
                    }
                }
            }
        }
    }
}


data class BottomNavItem(
    val label: String,
    val iconBottom: ImageVector,
    val route: String
)

val bottomNavItemList: List<BottomNavItem> = listOf(
    BottomNavItem("Home", Icons.Default.Home, Screen.Home.route),
    BottomNavItem("Read", Icons.Default.Search, Screen.Read.route),
    BottomNavItem("Bookmarks", Icons.Default.Favorite, Screen.Bookmark.route),
    BottomNavItem("Settings", Icons.Default.Settings, Screen.Setting.route)
)