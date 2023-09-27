package com.example.quranisapp.Screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.quranisapp.R
import com.example.quranisapp.tabrowscreens.JuzScreens
import com.example.quranisapp.tabrowscreens.PageScreens
import com.example.quranisapp.tabrowscreens.SurahScreens
import com.example.quranisapp.ui.theme.QURANISAppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
fun QuranScreens(
    goToRead: (surahNumber: Int?, juzNumber: Int?, pageNumber: Int?) -> Unit,
//    goToSurah: () -> Unit,
) {
    QURANISAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val tabList = listOf("Surah", "Juz", "Page")
            val scope = rememberCoroutineScope()
            val pageState = rememberPagerState(
                initialPage = 0
            )

            var textfield by remember { mutableStateOf("") }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Read Quran", color = Color.White) },
                        colors = TopAppBarDefaults.smallTopAppBarColors(
                            containerColor = colorResource(
                                id = R.color.blue
                            )
                        ),
                        actions = {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    )
                },
            ) { it ->
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(it)
                        .background(color = colorResource(id = R.color.white_background))
                ) {
                    OutlinedTextField(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                        value = textfield,
                        onValueChange = { textfield = it },
                        shape = RoundedCornerShape(8.dp),
                        label = { Text(text = "Search in here") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_search_24),
                                contentDescription = ""
                            )
                        })
                    TabRow(
                        selectedTabIndex = pageState.currentPage,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tabList.forEachIndexed { index, text ->
                            Tab(
                                modifier = Modifier.height(56.dp).background(color = colorResource(id = R.color.white_background)),
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
                        pageCount = tabList.size,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        when (page) {
                            0 -> SurahScreens(
                                Modifier.fillMaxSize(),
                                goToRead = goToRead)

                            1 -> JuzScreens(
                                Modifier.fillMaxSize(),
                                goToRead = goToRead)

                            2 -> PageScreens(
                                Modifier.fillMaxSize(),
                                goToRead = goToRead)
                        }
                    }
                }
            }
        }
    }
}