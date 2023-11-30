package com.olimhouse.qooraanapp.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.olimhouse.qooraanapp.data.database.QoranDatabase


@Composable
fun PageScreens(
    modifier: Modifier = Modifier,
    goToRead: (surahNumber: Int?, juzNumber: Int?, pageNumber: Int?, index: Int?) -> Unit
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10))
//                    .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            goToRead.invoke(null, null, it.page, null)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    ListItem(
                        headlineContent = {
                            Text(
                                text = "Page ${it.page} | Surah ${it.surahNameEn}",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        trailingContent = {

                        },
                        supportingContent = {
                            Text(
                                text = "${it.surahNameAr} | Surah ${it.surahNumber}",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 2.dp)
                }
            }
        }
    }
}