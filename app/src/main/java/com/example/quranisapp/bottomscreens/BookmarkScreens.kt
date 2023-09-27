package com.example.quranisapp.bottomscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quranisapp.R
import com.example.quranisapp.data.database.BookmarkDatabase
import com.example.quranisapp.tabrowscreens.SurahFavoriteButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreens(goToRead: (surahNumber: Int?, juzNumber: Int?, pageNumber: Int?) -> Unit) {

    val context = LocalContext.current
    val dao = BookmarkDatabase.getInstance(context).dao()
    val list = dao.getSurahBookmark()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
//    openDialog = !openDialog

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Bookmarks", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.blue)),
            )
        }
    ) {
        list.collectAsState(initial = emptyList()).let { state ->
            LazyColumn(
                Modifier
                    .padding(it)
                    .background(colorResource(id = R.color.white_background))
            ) {
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Reading Bookmarks",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(
                                id = R.color.blue
                            )
                        )
                        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                            Text(
                                text = "All", color = colorResource(
                                    id = R.color.blue
                                )
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = ""
                            )
                        }
                    }
                }
                items(state.value) {
                    Box(Modifier.fillMaxSize()) {
                        Row {
                            Card(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .size(34.dp)
                                    .clickable {
                                        goToRead.invoke(it.surahNumber, null, null)
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Box(Modifier.fillMaxSize()) {
                                    Text(
                                        text = "${it.surahNumber}",
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = "${it.surahNameEn} | ${it.surahNameAr}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(text = "${it.totalAyah} Ayat")
                                Text(text = "Juz ${it.juzNumber} | ${it.surahDescend}")
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp), Alignment.TopEnd
                            ) {
                                SurahFavoriteButton(
                                    modifier = Modifier.align(Alignment.TopEnd),
                                    totalAyah = it.totalAyah,
                                    surahNumber = it.surahNumber,
                                    juzNumber = it.juzNumber,
                                    surahNameEn = it.surahNameEn,
                                    surahNameAr = it.surahNameAr,
                                    surahDescend = it.surahDescend,
                                    surahBookmark = it
                                )
                            }
                        }
                    }
                    Divider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp)
                }
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Playlist Bookmarks",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(
                                id = R.color.blue
                            )
                        )
                        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                            Text(
                                text = "All", color = colorResource(
                                    id = R.color.blue
                                )
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = ""
                            )
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
                            containerColor = colorResource(id = R.color.white)
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
                                    FavoriteBookmarksButton(modifier = Modifier.align(Alignment.TopEnd))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    color: Color = Color(0xffE91E63)
) {

    var isFavorite by remember { mutableStateOf(false) }

    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            isFavorite = !isFavorite
        }
    ) {
        Icon(
            tint = color,
            modifier = modifier.graphicsLayer {
                scaleX = 1.3f
                scaleY = 1.3f
            },
            imageVector = if (isFavorite) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = null
        )
    }
}


@Composable
fun FavoriteBookmarksButton(
    modifier: Modifier = Modifier,
    color: Color = Color(0xffE91E63)
) {

    var isFavorite by remember { mutableStateOf(true) }

    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            isFavorite = !isFavorite
        }
    ) {
        Icon(
            tint = color,
            modifier = modifier.graphicsLayer {
                scaleX = 1.3f
                scaleY = 1.3f
            },
            imageVector = if (isFavorite) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = null
        )
    }
}

