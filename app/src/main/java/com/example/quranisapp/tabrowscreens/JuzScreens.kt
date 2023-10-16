package com.example.quranisapp.tabrowscreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            items(state.value) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(10))
                        .clickable {
                            goToRead.invoke(null, it.juzNumber, null)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(Modifier.fillMaxSize()) {
                        Row {
                            Text(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.CenterVertically),
                                text = "Juz ${it.juzNumber}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp), Alignment.TopEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .clickable { expanded = true }
                                        .align(Alignment.TopEnd))
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(it.surahNameEn) },
                                        onClick = { },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Outlined.ArrowForward,
                                                contentDescription = null
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}