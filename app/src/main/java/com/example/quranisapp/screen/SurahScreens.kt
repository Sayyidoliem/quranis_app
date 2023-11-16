package com.example.quranisapp.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.quranisapp.data.database.BookmarkDatabase
import com.example.quranisapp.data.database.QoranDatabase
import com.example.quranisapp.data.database.entities.SurahBookmark
import kotlinx.coroutines.launch

@Composable
fun SurahScreens(
    modifier: Modifier = Modifier,
    goToRead: (surahNumber: Int?, juzNumber: Int?, pageNumber: Int?, index: Int?) -> Unit
) {

    val context = LocalContext.current
    val dao = QoranDatabase.getInstance(context).dao()
    val list = dao.getSurah()

    list.collectAsState(initial = emptyList()).let { state ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {
            items(state.value) { surah ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10))
//                    .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            goToRead.invoke(surah.surahNumber, null, null, null)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )

                ) {
                    ListItem(
                        headlineContent = {
                            Text(
                                text = "${surah.surahNameEn} | ${surah.surahNameAr}",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        leadingContent = {
                            Card(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .size(34.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Box(Modifier.fillMaxSize()) {
                                    Text(
                                        text = "${surah.surahNumber}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        },
                        supportingContent = {
                            Text(
                                text = "${surah.numberOfAyah} Ayat | Juz ${surah.juzNumber} | ${surah.surahDescendPlace}",
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        trailingContent = {
                            SurahFavoriteButton(
                                modifier = Modifier,
                                totalAyah = surah.numberOfAyah,
                                surahNumber = surah.surahNumber,
                                juzNumber = surah.juzNumber,
                                surahNameEn = surah.surahNameEn,
                                surahNameAr = surah.surahNameAr,
                                surahDescend = surah.surahDescendPlace,
                            )
                        }
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 2.dp)
                }
            }
        }
    }
}

@Composable
fun SurahFavoriteButton(
    modifier: Modifier = Modifier,
    surahNumber: Int?,
    surahNameEn: String?,
    surahNameAr: String?,
    totalAyah: Int?,
    juzNumber: Int?,
    surahDescend: String?,
) {
    val context = LocalContext.current
    val dao = BookmarkDatabase.getInstance(context).bookmarkDao()
    val scope = rememberCoroutineScope()
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        isFavorite = dao.selectedFavoriteButton(surahNumber!!)
    }

    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            scope.launch {
                if (isFavorite) {
                    dao.deleteSurahBookmark(
                        SurahBookmark(
                            surahNameEn!!,
                            surahNameAr!!,
                            totalAyah!!,
                            juzNumber!!,
                            surahDescend,
                            surahNumber
                        )
                    )
                    isFavorite = false
                    return@launch
                }
                dao.insertSurahBookmark(
                    SurahBookmark(
                        surahNameEn!!,
                        surahNameAr!!,
                        totalAyah!!,
                        juzNumber!!,
                        surahDescend!!,
                        surahNumber
                    )
                )
                isFavorite = true
            }
        }
    ) {
        Icon(
            modifier = modifier.graphicsLayer {
                scaleX = 1.3f
                scaleY = 1.3f
            },
            imageVector = if (isFavorite) {
                Icons.Filled.BookmarkAdded
            } else {
                Icons.Default.BookmarkAdd
            },
            contentDescription = null
        )
    }
}