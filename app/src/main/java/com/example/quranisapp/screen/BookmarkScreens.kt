package com.example.quranisapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quranisapp.data.database.BookmarkDatabase
import com.example.quranisapp.data.kotpref.LastReadPreferences
import com.example.quranisapp.data.kotpref.SettingPreferences
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BookmarkScreens(
    modifier: Modifier,
    goToRead: (surahNumber: Int?, juzNumber: Int?, pageNumber: Int?, index: Int?) -> Unit
) {
    val context = LocalContext.current
    val bookmarkDao = BookmarkDatabase.getInstance(context).bookmarkDao()
    val list = bookmarkDao.getSurahBookmark()
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    var isSortedDialogOpen by remember {
        mutableStateOf(false)
    }
    val radioOptions = listOf("Surah","Ayat")
    var selectedOption by remember { mutableStateOf(radioOptions[0]) }

    var selectedOptionTemp by remember {
        mutableStateOf(radioOptions[0])
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) {
        Column {
            val date = SimpleDateFormat(
                "dd/MMM/yyyy",
                Locale.getDefault()
            ).format(Date(LastReadPreferences.lastDate))
            when (selectedOption) {
                radioOptions[0] -> {
                    list.collectAsState(initial = emptyList()).let { state ->
                        LazyColumn(
                            Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .fillMaxSize()
                        ) {
                            item {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text =  when (SettingPreferences.isSelectedLanguage) {
                                            SettingPreferences.INDONESIA -> {
                                                "Terakhir Dibaca"
                                            }

                                            else -> {
                                                "Last Read"
                                            }
                                        },
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                    IconButton(
                                        onClick = { isSortedDialogOpen = true },
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Sort,
                                            contentDescription = "",
                                        )
                                    }
                                }
                                when {
                                    LastReadPreferences.lastSurahName.isNotEmpty() -> {
                                        ListItem(
                                            headlineContent = {
                                                Text(
                                                    text = LastReadPreferences.lastSurahName,
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
                                                            text = LastReadPreferences.lastSurahNumber.toString(),
                                                            modifier = Modifier.align(Alignment.Center)
                                                        )
                                                    }
                                                }
                                            },
                                            trailingContent = { Text(text = date.toString()) },
                                            supportingContent = { Text(text = "Ayat ${LastReadPreferences.lastAyaNo}") },
                                            modifier = Modifier.clickable {
                                                goToRead.invoke(
                                                    LastReadPreferences.lastSurahNumber,
                                                    null,
                                                    null,
                                                    LastReadPreferences.index
                                                )
                                            }
                                        )
                                    }
                                    else -> {
                                        ListItem(
                                            headlineContent = {
                                                Text(
                                                    text =  when (SettingPreferences.isSelectedLanguage) {
                                                        SettingPreferences.INDONESIA -> {
                                                            "Kamu belum membaca ayat apapun"
                                                        }

                                                        else -> {
                                                            "You doesn't read anything ayat"
                                                        }
                                                    },
                                                    style = MaterialTheme.typography.titleLarge
                                                )
                                            },
                                            modifier = Modifier.clickable {
                                                goToRead.invoke(
                                                    LastReadPreferences.lastSurahNumber,
                                                    null,
                                                    null,
                                                    LastReadPreferences.index
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                            item {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Text(
                                        text =  when (SettingPreferences.isSelectedLanguage) {
                                            SettingPreferences.INDONESIA -> {
                                                "Penanda Surat"
                                            }

                                            else -> {
                                                "Surah Bookmarks"
                                            }
                                        },
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.align(Alignment.CenterStart)
                                    )
                                    IconButton(
                                        onClick = { isSortedDialogOpen = true },
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.RestoreFromTrash,
                                            contentDescription = "",
                                        )
                                    }
                                }
                            }
                            items(state.value) {
                                Box(Modifier.fillMaxSize()) {
                                    ListItem(
                                        headlineContent = {
                                            Text(
                                                "${it.surahNameEn} | ${it.surahNameAr}",
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                        },
                                        overlineContent = {
                                            Text(
                                                text = "Juz ${it.juzNumber} | ${it.surahDescend}",
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        },
                                        supportingContent = { Text("${it.totalAyah} Ayat") },
                                        trailingContent = {
                                            IconButton(onClick = {
                                                scope.launch {
                                                    bookmarkDao.deleteSurahBookmark(surahBookmark = it)
                                                    val result = snackbarHostState
                                                        .showSnackbar(
                                                            message =  when (SettingPreferences.isSelectedLanguage) {
                                                                SettingPreferences.INDONESIA -> {
                                                                    "Hapus penanda"
                                                                }

                                                                else -> {
                                                                    "Remove bookmark"
                                                                }
                                                            },
                                                            actionLabel =  when (SettingPreferences.isSelectedLanguage) {
                                                                SettingPreferences.INDONESIA -> {
                                                                    "Kembalikan"
                                                                }

                                                                else -> {
                                                                    "Undo"
                                                                }
                                                            },
                                                            // Defaults to SnackbarDuration.Short
                                                            duration = SnackbarDuration.Short
                                                        )
                                                    when (result) {
                                                        SnackbarResult.ActionPerformed -> {
                                                            /* Handle snackbar action performed */
                                                            bookmarkDao.insertSurahBookmark(
                                                                surahBookmark = it
                                                            )
                                                        }

                                                        SnackbarResult.Dismissed -> {
                                                            /* Handle snackbar dismissed */
                                                            bookmarkDao.deleteSurahBookmark(
                                                                surahBookmark = it
                                                            )

                                                        }
                                                    }
                                                }
                                            }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = null
                                                )
                                            }
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
                                                        text = "${it.surahNumber}",
                                                        modifier = Modifier.align(Alignment.Center)
                                                    )
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                            .clickable {
                                                goToRead.invoke(
                                                    it.surahNumber,
                                                    null,
                                                    null,
                                                    null
                                                )
                                            }
                                    )
                                }
                                Divider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp)
                            }
                        }
                    }
                }
                else -> {
                    bookmarkDao.getAllBookmarks().collectAsState(initial = emptyList()).let { state ->
                        LazyColumn(
                            Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .padding(it)
                                .fillMaxSize()
                        ) {
                            item {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text =  when (SettingPreferences.isSelectedLanguage) {
                                            SettingPreferences.INDONESIA -> {
                                                "Terakhir Dibaca"
                                            }

                                            else -> {
                                                "Last Read"
                                            }
                                        },
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                    IconButton(
                                        onClick = { isSortedDialogOpen = true },
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Sort,
                                            contentDescription = "",
                                        )
                                    }
                                }
                                when {
                                    LastReadPreferences.lastSurahName.isNotEmpty() -> {
                                        ListItem(
                                            headlineContent = {
                                                Text(
                                                    text = LastReadPreferences.lastSurahName,
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
                                                            text = LastReadPreferences.lastSurahNumber.toString(),
                                                            modifier = Modifier.align(Alignment.Center)
                                                        )
                                                    }
                                                }
                                            },
                                            trailingContent = { Text(text = date.toString()) },
                                            supportingContent = { Text(text = "Ayat ${LastReadPreferences.lastAyaNo}") },
                                            modifier = Modifier.clickable {
                                                goToRead.invoke(
                                                    LastReadPreferences.lastSurahNumber,
                                                    null,
                                                    null,
                                                    LastReadPreferences.index
                                                )
                                            }
                                        )
                                    }
                                    else -> {
                                        ListItem(
                                            headlineContent = {
                                                Text(
                                                    text =  when (SettingPreferences.isSelectedLanguage) {
                                                        SettingPreferences.INDONESIA -> {
                                                            "Kamu belum membaca ayat apapun"
                                                        }

                                                        else -> {
                                                            "You doesn't read anything ayat"
                                                        }
                                                    },
                                                    style = MaterialTheme.typography.titleLarge
                                                )
                                            },
                                            modifier = Modifier.clickable {
                                                goToRead.invoke(
                                                    LastReadPreferences.lastSurahNumber,
                                                    null,
                                                    null,
                                                    LastReadPreferences.index
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                            item {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Text(
                                        text =  when (SettingPreferences.isSelectedLanguage) {
                                            SettingPreferences.INDONESIA -> {
                                                "Penanda Ayat"
                                            }

                                            else -> {
                                                "Ayat Bookmarks"
                                            }
                                        },
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                    IconButton(
                                        onClick = { isSortedDialogOpen = true },
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.RestoreFromTrash,
                                            contentDescription = "",
                                        )
                                    }
                                }
                            }
                            items(state.value) {
                                ListItem(
                                    headlineContent = {
                                        Text(
                                            it.surahName!!,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                    },
                                    supportingContent = {
                                        Text(
                                            "Ayat ${it.ayahNumber}",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    },
                                    trailingContent = {
                                        IconButton(onClick = {
                                            scope.launch {
                                                bookmarkDao.deleteBookmark(bookmark = it)
                                                val result = snackbarHostState
                                                    .showSnackbar(
                                                        message =  when (SettingPreferences.isSelectedLanguage) {
                                                            SettingPreferences.INDONESIA -> {
                                                                "Hapus bookmark"
                                                            }

                                                            else -> {
                                                                "Remove bookmark"
                                                            }
                                                        },
                                                        actionLabel =  when (SettingPreferences.isSelectedLanguage) {
                                                            SettingPreferences.INDONESIA -> {
                                                                "Kembalikan"
                                                            }

                                                            else -> {
                                                                "Undo"
                                                            }
                                                        },
                                                        // Defaults to SnackbarDuration.Short
                                                        duration = SnackbarDuration.Short
                                                    )
                                                when (result) {
                                                    SnackbarResult.ActionPerformed -> {
                                                        /* Handle snackbar action performed */
                                                        bookmarkDao.insertBookmark(bookmark = it)
                                                    }

                                                    SnackbarResult.Dismissed -> {
                                                        /* Handle snackbar dismissed */
                                                        bookmarkDao.deleteBookmark(bookmark = it)

                                                    }
                                                }
                                            }
                                        }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = null
                                            )
                                        }
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
                                                    text = "${it.surahNumber}",
                                                    modifier = Modifier.align(Alignment.Center)
                                                )
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .clickable {}
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp), thickness = 2.dp
                                )
                            }
                        }
                    }
                }
            }
        }

    }
    if (isSortedDialogOpen) {
        AlertDialog(
            onDismissRequest = { isSortedDialogOpen = !isSortedDialogOpen },
            title = { Text(text = "Select Bookmarks ") },
            text = {
                Column {
                    radioOptions.forEach { option ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (option == selectedOptionTemp),
                                onClick = { selectedOptionTemp = option }
                            )
                            TextButton(onClick = { selectedOptionTemp = option }) {
                                Text(
                                    text = option,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }

                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    isSortedDialogOpen = !isSortedDialogOpen
                    selectedOption = selectedOptionTemp
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { isSortedDialogOpen = !isSortedDialogOpen }) {
                    Text("Dismiss")
                }
            }
        )
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

