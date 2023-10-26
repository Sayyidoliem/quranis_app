package com.example.quranisapp.tabrowscreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quranisapp.data.database.QoranDatabase
import com.example.quranisapp.utils.Converters.mapToJuzIndexing

@Composable
fun JuzScreens(
    modifier: Modifier = Modifier,
    goToRead: (surahNumber: Int?, juzNumber: Int?, pageNumber: Int?) -> Unit
) {
    val context = LocalContext.current
    val dao = QoranDatabase.getInstance(context).dao()
    val list = dao.getJuz()

    var expanded by remember { mutableStateOf(false) }

    list.collectAsState(initial = emptyList()).let { state ->
        val juzByIndexSurah = state.value.mapToJuzIndexing()
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            items(juzByIndexSurah ?: emptyList()) { juzMap ->
                JuzCardItem(
                    juzNumber = juzMap.juzNumber!!,
                    surahList = juzMap.surahList,
                    surahNumberList = juzMap.surahNumberList,
                    goToRead = { surahNumber ->
                        goToRead(surahNumber, juzMap.juzNumber, null)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JuzCardItem(
    juzNumber: Int,
    surahList: List<String?>,
    surahNumberList: List<Int?>,
    goToRead: (Int?) -> Unit,

    ) {
    var isSurahListShowed by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(10)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        onClick = {
            if (surahNumberList.isNotEmpty()) {
                goToRead.invoke(surahNumberList.first()!!)
            }
        }
    ) {
        Box(Modifier.fillMaxWidth()) {
            Row {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterVertically),
                    text = "Juz $juzNumber",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), Alignment.TopEnd
                ) {
                    IconButton(
                        onClick = {
                            isSurahListShowed = !isSurahListShowed
                        },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = if (!isSurahListShowed) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                            contentDescription = "",
                        )
                    }
                }
            }
        }
        AnimatedVisibility(visible = isSurahListShowed) {
            JuzCardMiniItem(
                surahList = surahList,
                surahNumberList = surahNumberList,
                onItemClick = { surahNumber ->
                    goToRead(surahNumber)
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JuzCardMiniItem(
    surahList: List<String?>,
    surahNumberList: List<Int?>,
    onItemClick: (Int?) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (surahList.isNotEmpty() && surahNumberList.isNotEmpty()) {
            for (index in surahList.indices) {
                Card(
                    modifier = Modifier.padding(8.dp),
                    onClick = { onItemClick(surahNumberList[index]) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "${surahNumberList[index]}.",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "${surahList[index]}",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )

                    }
                }
            }
        }
    }
}