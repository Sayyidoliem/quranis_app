package com.example.quranisapp.tabrowscreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quranisapp.R
import com.example.quranisapp.bottomscreens.FavoriteButton
import com.example.quranisapp.data.database.QoranDatabase


@Composable
fun PageScreens(
    modifier: Modifier = Modifier,
    goToRead: (surahNumber: Int?, juzNumber: Int?, pageNumber: Int?) -> Unit
//    onClick: (Int?) -> Unit
) {
    val context = LocalContext.current
    val dao = QoranDatabase.getInstance(context).dao()
    val list = dao.getPage()

    list.collectAsState(initial = emptyList()).let { state ->
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            items(state.value) {
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10))
//                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable {
                        goToRead.invoke(null, null, it.page)
                    },
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.white_background)
                    )
                ) {
                    Box(Modifier.fillMaxSize()) {
                        Row {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Page ${it.page} | Surah ${it.surahNumber}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(text = "${it.surahNameEn} | ${it.surahNameAr}")
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp), Alignment.TopEnd
                            ) {
                                FavoriteButton(modifier = Modifier.align(Alignment.TopEnd))
                            }
                        }
                        Divider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 2.dp)
                    }
                }
            }
        }
    }
}