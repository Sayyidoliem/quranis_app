package com.example.quranisapp.bottomscreens

import android.Manifest
import android.location.Geocoder
import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quranisapp.R
import com.example.quranisapp.navigation.Screen
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreens(
    goToPrayer: () -> Unit,
    pindah: (String) -> Unit
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

    LaunchedEffect(key1 = locationPermission.allPermissionsGranted) {
        locationState.emit(locationService.getCurrentLocation())
    }


    val itemsList = listOf("Fajar", "Dzuhur", "Ashar", "Maghrib", "Isya")
    var selectedItem by remember {
        mutableStateOf(itemsList[0])
    }
    var textField by remember {
        mutableStateOf("")
    }

    val navCotroller: NavHostController = rememberNavController()
    val navBackStackEntry by navCotroller.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val navDrawerState = DrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())


    val api = ApiInterface.createApi()
    val prayerTime = remember {
        mutableStateListOf<Time?>()
    }

    var cityLocation by remember { mutableStateOf("") }
    var provinceLocation by remember { mutableStateOf("") }

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
                            navCotroller.navigate(item.route) {
                                popUpTo(navCotroller.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                restoreState = true
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(imageVector = item.icon, contentDescription = "") }
                    )
                }
                Divider(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp), thickness = 1.dp)
                secondaryNavItemList.map { item ->
                    NavigationDrawerItem(
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        label = { Text(text = item.label) },
                        selected = currentDestination == item.route,
                        onClick = {
                            navCotroller.navigate(item.route) {
                                popUpTo(navCotroller.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                restoreState = true
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(painter = painterResource(id = item.icon), contentDescription = "") }
                    )
                }
            }
        }
    ) {
        Scaffold(
//            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { navDrawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    title = { Text(text = "Home", color = MaterialTheme.colorScheme.onPrimary) },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
//                    scrollBehavior = scrollBehavior
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /*TODO*/ },
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) {
                    Icon(Icons.Filled.Add, "Localized description")
                }
            },
        ) { it ->
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
//            item {
//                        Text(
//                            fontSize = 40.sp,
//                    modifier = Modifier.padding(horizontal = 16.dp),
//                    text = buildAnnotatedString {
//                        append("Hello,\n")
//                        withStyle(
//                            style = SpanStyle(fontWeight = FontWeight.SemiBold)
//                        ) {
//                            append("Sayyid")
//                        }
//                    }
//                )
//            }
                item {
                    OutlinedTextField(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                        value = textField,
                        onValueChange = { textField = it },
                        shape = RoundedCornerShape(8.dp),
                        label = { Text(text = "Search") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_search_24),
                                contentDescription = ""
                            )
                        })

                }
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        items(customCardListItem) { itemCard ->
                            Card(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .size(120.dp),
                                onClick = {
                                    pindah(itemCard.route)
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Column {
                                    Icon(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .align(Alignment.CenterHorizontally),
                                        painter = painterResource(id = itemCard.icon),
                                        tint = MaterialTheme.colorScheme.onSecondary,
                                        contentDescription = "",
                                    )
                                    Text(
                                        text = itemCard.label,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSecondary,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                //prayer timings
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Prayer Timings",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                            Text(text = "All", color = MaterialTheme.colorScheme.onBackground)
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
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
                            .clickable { goToPrayer() },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 16.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(
                                    modifier = Modifier
                                        .padding(vertical = 30.dp)
                                        .padding(start = 20.dp)
                                ) {
                                    Text(
                                        text = "Next Prayer",
                                        fontSize = 20.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = "11.30",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    when {
                                        prayerTime.isNotEmpty() -> {
                                            Spacer(modifier = Modifier.size(20.dp))
                                            Text(
                                                text = "${prayerTime[0]?.gregorian}",
                                                fontSize = 20.sp
                                            )
                                            Spacer(modifier = Modifier.size(20.dp))
                                            Text(
                                                modifier = Modifier.width(160.dp),
                                                text = "$cityLocation, $provinceLocation",
                                                fontSize = 20.sp,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }

                                        else -> {
                                            Spacer(modifier = Modifier.size(20.dp))
                                            Text(
                                                modifier = Modifier.width(160.dp),
                                                text = cityLocation,
                                                fontSize = 20.sp,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
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
                        }
                    }
                }
                //today goals
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Today Goals",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                            Text(text = "All", color = MaterialTheme.colorScheme.onBackground)
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(10),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column {
                            LazyRow(Modifier.padding(16.dp)) {
                                item() {
                                    itemsList.forEachIndexed { index, item ->
                                        FilterChip(
                                            modifier = Modifier.padding(horizontal = 6.dp), // gap between items
                                            selected = (item == selectedItem),
                                            onClick = {
                                                selectedItem = item
                                            },
                                            label = {
                                                Text(
                                                    text = item,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            },
                                            colors = FilterChipDefaults.filterChipColors(
                                                labelColor = MaterialTheme.colorScheme.onSurface,
                                                selectedLabelColor = MaterialTheme.colorScheme.tertiary,
                                                selectedLeadingIconColor = MaterialTheme.colorScheme.secondary,
                                                selectedContainerColor = MaterialTheme.colorScheme.secondary.copy(
                                                    alpha = 0.1f
                                                )
                                            ),
                                            leadingIcon = if (item == selectedItem) {
                                                {
                                                    Icon(
                                                        imageVector = Icons.Default.Done,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.tertiary,
                                                        modifier = Modifier.size(
                                                            FilterChipDefaults.IconSize
                                                        )
                                                    )
                                                }
                                            } else {
                                                null
                                            }
                                        )
                                    }
                                }
                            }
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Icon(
                                    modifier = Modifier.align(Alignment.CenterEnd),
                                    painter = painterResource(id = R.drawable.alarm),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    contentDescription = ""
                                )
                            }
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "12.03.2023",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = "11.30",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.CenterEnd),
                                )
                            }
                        }
                    }
                }
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = "Quran Playlist",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                            Text(text = "All", color = MaterialTheme.colorScheme.onBackground)
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(10)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Box(Modifier.fillMaxSize()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(
                                    modifier = Modifier
                                        .padding(vertical = 30.dp)
                                        .padding(start = 20.dp)
                                ) {
                                    Text(text = "#01", fontSize = 20.sp)
                                    Text(
                                        text = "Al-Insyiqaq",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                    )
                                    Spacer(modifier = Modifier.size(20.dp))
                                    Text(text = "Sound Name", fontSize = 20.sp)
                                    Spacer(modifier = Modifier.size(20.dp))
                                    Text(text = "Artist Sound", fontSize = 20.sp)
                                }
                                Image(
                                    painter = painterResource(id = R.drawable.ic_launcher_background),
                                    contentDescription = "",
                                    Modifier
                                        .weight(9f / 16f, fill = false)
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                }
                items(5) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(10)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Box(Modifier.fillMaxSize()) {
                            Row {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_launcher_background),
                                    contentDescription = ""
                                )
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = "Surah Name",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(text = "Juz")
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp), Alignment.TopEnd
                                ) {
                                    FavoriteButton()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class CustomCardList(
    val label: String,
    val icon: Int,
    val route: String
)

val customCardListItem: List<CustomCardList> = listOf(
    CustomCardList("Quran", R.drawable.menu_book, Screen.Read.route),
    CustomCardList("Qiblat", R.drawable.open_mic, "qiblat"),
    CustomCardList("Tajwid", R.drawable.paper, ""),
    CustomCardList("Alarm", R.drawable.alarm, "")
)

data class NavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

val navigationdrawerList: List<NavItem> = listOf(
    NavItem("home", "Home", Icons.Default.Home),
    NavItem("search", "Read Quran", Icons.Default.Search),
    NavItem("setting", "Account", Icons.Default.AccountCircle)
)

data class SecondaryNavItem(
    val route: String,
    val label: String,
    val icon: Int
)

val secondaryNavItemList : List<SecondaryNavItem> = listOf(
    SecondaryNavItem("dowloaded", "Downloaded", R.drawable.alarm),
    SecondaryNavItem("a", "Downloaded", R.drawable.alarm),
    SecondaryNavItem("about", "App Info", R.drawable.alarm)
)