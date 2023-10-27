package com.example.quranisapp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compose.QURANISAppTheme
import com.example.quranisapp.Screen.PrayerScreens
import com.example.quranisapp.Screen.QuranScreens
import com.example.quranisapp.bottomscreens.BookmarkScreens
import com.example.quranisapp.bottomscreens.DiscoverScreens
import com.example.quranisapp.bottomscreens.HomeScreens
import com.example.quranisapp.bottomscreens.SettingScreens
import com.example.quranisapp.drawerscreens.AppInfoScreens
import com.example.quranisapp.navigation.Screen
import com.example.quranisapp.tabrowscreens.AyatScreens
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
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
            var currentDestination = navBackStackEntry?.destination?.route

            val navDrawerState = DrawerState(initialValue = DrawerValue.Closed)

            val scope = rememberCoroutineScope()

            ModalNavigationDrawer(
                drawerState = navDrawerState,
                gesturesEnabled = true,
                drawerContent = {
                    ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.surface) {
                        Text(
                            modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 16.dp),
                            text = "QURANIS",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        navigationdrawerList.map { item ->
                            NavigationDrawerItem(
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                                label = { Text(text = item.label) },
                                selected = currentDestination == item.route,
                                onClick = {
                                    currentDestination = item.route
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        restoreState = true
                                        launchSingleTop = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = item.icon),
                                        contentDescription = ""
                                    )
                                }
                            )
                        }
                        Divider(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                            thickness = 1.dp
                        )
                        secondaryNavItemList.map { item ->
                            NavigationDrawerItem(
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                                label = { Text(text = item.label) },
                                selected = currentDestination == item.route,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        restoreState = true
                                        launchSingleTop = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = item.icon),
                                        contentDescription = ""
                                    )
                                }
                            )
                        }
                    }
                }
            ) {
                Scaffold(
                    modifier = Modifier,
                    bottomBar = {
                        if (bottomNavItemList.any {
                                it.route == currentRoute
                            }
                        ) NavigationBar(
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
                    },
                ) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Screen.Read.route
                    ) {
                        composable("home") {
                            HomeScreens(
                                goToPrayer = { navController.navigate("prayer") },
                                pindah = { navController.navigate(it) },
                                openDrawer = { scope.launch { navDrawerState.open() } }
                            )
                        }
                        composable(Screen.Prayer.route) {
                            PrayerScreens(openDrawer = { scope.launch { navDrawerState.open() }})
                        }
                        composable(Screen.Discover.route) {
                            DiscoverScreens(openDrawer = { scope.launch { navDrawerState.open() } })
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
                                openDrawer = { scope.launch { navDrawerState.open() } }
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
                                modifier = Modifier,
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
                            SettingScreens(openDrawer = {scope.launch { navDrawerState.open() } })
                        }
                        composable(Screen.Qiblat.route) {
                            FindQiblat(openDrawer = { scope.launch { navDrawerState.open() } })
                        }
                        composable(Screen.Info.route) {
                            AppInfoScreens(openDrawer = { scope.launch { navDrawerState.open() } })
                        }
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
    BottomNavItem("Home", Icons.Default.Home, Screen.Read.route),
    BottomNavItem("Qori", Icons.Default.Home, Screen.Discover.route),
)
data class NavItem(
    val route: String,
    val label: String,
    val icon: Int
)
val navigationdrawerList: List<NavItem> = listOf(
    NavItem(Screen.Read.route, "Quran", R.drawable.baseline_home_24),
    NavItem(Screen.Discover.route,"Qori", R.drawable.baseline_play_circle_24),
    NavItem(Screen.Qiblat.route, "Qiblat Compass", R.drawable.baseline_navigation_24),
    NavItem(Screen.Prayer.route, "Prayer Time", R.drawable.baseline_access_time_24),
    NavItem(Screen.Setting.route, "Settings", R.drawable.baseline_settings_24)
)
val secondaryNavItemList: List<NavItem> = listOf(
    NavItem("dowloaded", "Downloaded", R.drawable.baseline_download_24),
    NavItem("a", "Downloaded", R.drawable.alarm),
    NavItem(Screen.Info.route, "App Info", R.drawable.baseline_info_24)
)