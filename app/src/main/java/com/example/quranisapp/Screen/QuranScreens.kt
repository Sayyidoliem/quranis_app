package com.example.quranisapp.Screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.QURANISAppTheme
import com.example.quranisapp.bottomscreens.BookmarkScreens
import com.example.quranisapp.data.database.QoranDatabase
import com.example.quranisapp.data.database.SurahSearch
import com.example.quranisapp.navigation.Screen
import com.example.quranisapp.tabrowscreens.JuzScreens
import com.example.quranisapp.tabrowscreens.PageScreens
import com.example.quranisapp.tabrowscreens.SurahScreens
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
fun QuranScreens(
    goToRead: (surahNumber: Int?, juzNumber: Int?, pageNumber: Int?) -> Unit,
    openDrawer: () -> Unit,
) {
    QURANISAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val tabList = listOf("Surah", "Page", "Juz", "Bookmarks")
            val scope = rememberCoroutineScope()
            val pageState = rememberPagerState(
                initialPage = 0,
                pageCount = { tabList.size },
            )

            val navController: NavHostController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            var currentRoute: String? = navBackStackEntry?.destination?.route
            var currentDestination = navBackStackEntry?.destination?.route

            var textSearchBar by rememberSaveable { mutableStateOf("") }
            var searchBar by rememberSaveable { mutableStateOf(false) }

            val context = LocalContext.current
            val searchResult = remember {
                mutableStateListOf<SurahSearch>()
            }

            Scaffold(
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
                                    bottomNavigate(navController, item.route)
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
            ) { it ->
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(it)
                        .background(color = MaterialTheme.colorScheme.background)
                ) {
                    DockedSearchBar(
                        query = textSearchBar,
                        onQueryChange = { textSearchBar = it },
                        onSearch = { query ->
                            val dao = QoranDatabase.getInstance(context).dao()
                            searchResult.clear()
                            scope.launch {
                                dao.getSurahBySearch(query).collectLatest { list ->
                                    searchResult.addAll(list)
                                }
                            }
                        },
                        active = searchBar,
                        placeholder = { Text(text = "Search ") },
                        onActiveChange = { searchBar = it },
                        trailingIcon = {
                            if (searchBar) {
                                IconButton(onClick = {
                                    if (textSearchBar.isNotEmpty()) textSearchBar =
                                        "" else searchBar = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null
                                    )
                                }
                            } else IconButton(onClick = { searchBar = true }) {
                                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                            }
                        },
                        leadingIcon = {
                            if (searchBar) {
                                IconButton(onClick = { searchBar = !searchBar }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            } else IconButton(onClick = { openDrawer() }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = ""
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    ) {
                        LazyColumn {
                            items(searchResult) {
                                val resultText = "${it.surahNameEmlay}"
                                ListItem(
                                    headlineContent = { Text(resultText) },
                                    supportingContent = { Text("${it.numberOfAyah} Ayat || ${it.surahDescendPlace}") },
                                    leadingContent = {
                                        Text(text = "${it.surahNumber}")
                                    },
                                    modifier = Modifier
                                        .clickable {
                                            textSearchBar = resultText
                                            searchBar = false
                                            goToRead.invoke(it.surahNumber, null, null)
                                        }
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                    TabRow(
                        selectedTabIndex = pageState.currentPage,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tabList.forEachIndexed { index, text ->
                            Tab(
                                modifier = Modifier
                                    .height(56.dp)
                                    .background(color = MaterialTheme.colorScheme.background),
                                selected = index == pageState.currentPage,
                                onClick = {
                                    scope.launch {
                                        pageState.animateScrollToPage(index)
                                    }
                                }) {
                                Text(text = text)
                            }
                        }
                    }
                    HorizontalPager(
                        state = pageState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        when (page) {
                            0 -> SurahScreens(
                                Modifier.fillMaxSize(),
                                goToRead = goToRead
                            )

                            1 -> PageScreens(
                                Modifier.fillMaxSize(),
                                goToRead = goToRead
                            )

                            2 -> JuzScreens(
                                Modifier.fillMaxSize(),
                                goToRead = goToRead
                            )

                            3 -> BookmarkScreens(
                                Modifier.fillMaxSize(),
                                goToRead = goToRead
                            )
                        }
                    }
                }
            }
        }
    }
}

fun bottomNavigate(
    navController: NavHostController,
    route: String
) {
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState =
                true //buat menyimpan state mutable di setiap screen
        }
        restoreState = true
        launchSingleTop =
            true//buat klo back, langsung keluar app(bukan ke home)
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