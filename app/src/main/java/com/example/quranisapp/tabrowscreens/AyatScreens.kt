package com.example.quranisapp.tabrowscreens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quranisapp.R
import com.example.quranisapp.data.database.BookmarkDatabase
import com.example.quranisapp.data.database.QoranDatabase
import com.example.quranisapp.data.database.entities.Bookmark
import com.example.quranisapp.data.database.entities.SurahBookmark
import com.example.quranisapp.data.kotpref.SettingPreferences
import com.example.quranisapp.read.component.SpannableText
import com.example.quranisapp.service.player.MyPlayerServices
import com.example.quranisapp.utils.GlobalState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import snow.player.PlayerClient
import snow.player.audio.MusicItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyatScreens(
    goBack: () -> Unit,
    surahNumber: Int,
    juzNumber: Int,
    pageNumber: Int
) {
    val context = LocalContext.current
    val dao = QoranDatabase.getInstance(context).dao()
    val bookmarkDao = BookmarkDatabase.getInstance(context).dao()
    val list =
        when {
            surahNumber != -1 -> {
                dao.getAyatSurah(surahNumber)
            }

            juzNumber != -1 -> {
                dao.getAyatJozz(juzNumber)
            }

            else -> {
                dao.getAyatPage(pageNumber)
            }
        }

    var surahNameEn by remember {
        mutableStateOf("")
    }

    var surahNameAr by remember {
        mutableStateOf("")
    }

    var surahNameId by remember {
        mutableStateOf("")
    }

    var descendPlace by remember {
        mutableStateOf("")
    }

    var juzSurah by remember {
        mutableStateOf(0)
    }

    var totalAyah by remember {
        mutableStateOf(0)
    }

    var expanded by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        list.collectLatest {
            val name = it[0].surahNameEn
            surahNameEn = "$name"
        }
    }

    LaunchedEffect(key1 = true) {
        list.collectLatest {
            val nameAr = it[0].surahNameAr
            surahNameAr = "$nameAr"
        }
    }

    LaunchedEffect(key1 = true) {
        list.collectLatest {
            val nameId = it[0].surahNameId
            surahNameId = "$nameId"
        }
    }

    LaunchedEffect(key1 = true) {
        list.collectLatest {
            val descend = it[0].surahDescendPlace
            descendPlace = "$descend"
        }
    }

    LaunchedEffect(key1 = true) {
        list.collectLatest {
            val juz = it[0].juzNumber
            juzSurah = juz!!
        }
    }

    LaunchedEffect(key1 = true) {
        list.collectLatest {
            val juz = it[0].ayatNumber
            totalAyah = juz!!
        }
    }

    val radioOptions = listOf("English", "Indonesia")
    var selectedOption by remember { mutableStateOf(radioOptions[0]) }
    var showDialog by remember { mutableStateOf(false) }
    var showDialogSetting by remember { mutableStateOf(false) }

    val footNoteState = remember { mutableStateOf("") }

    val scaffoldState = rememberBottomSheetScaffoldState()

    val playerClient = remember {
        PlayerClient.newInstance(context, MyPlayerServices::class.java)
    }

    val changeLanguageState by remember {
        mutableStateOf(SettingPreferences.isSelectedLanguage)
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp, //karena awalnya tidak naik bottom sheetnya
        sheetContent = {
            LazyColumn {
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Footnote",
                            modifier = Modifier.padding(start = 16.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }
                    Text(text = footNoteState.value, modifier = Modifier.padding(16.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                if (scaffoldState.bottomSheetState.hasExpandedState) {
                                    scaffoldState.bottomSheetState.show()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    ) {
                        Text(text = "Close")
                    }
                }
            }
        },
        containerColor = Color.White,
        sheetContainerColor = Color.White,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                title = {
                    Text(
                        text = "${surahNumber}. ${surahNameEn}",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Change Language") },
                            onClick = {
                                showDialog = true
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_translate_24),
                                    contentDescription = null
                                )
                            })
                        DropdownMenuItem(
                            text = { Text("Share") },
                            onClick = {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "$surahNameEn | $surahNameAr | $surahNameId | Juz $juzSurah | $descendPlace" )
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, null)
                                context.startActivity(shareIntent)
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Share,
                                    contentDescription = null
                                )
                            })
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = {
                                showDialogSetting = true
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Settings,
                                    contentDescription = null
                                )
                            })
                        DropdownMenuItem(
                            text = { Text("Footnote") },
                            onClick = {
                                scope.launch { scaffoldState.bottomSheetState.expand() }
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Info,
                                    contentDescription = null
                                )
                            })
                        DropdownMenuItem(
                            text = { Text("Send Feedback") },
                            onClick = { expanded = false },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Email,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = { Text("F11", textAlign = TextAlign.Center) })
                    }
                }
            )
        }
    ) {
        list.collectAsState(initial = emptyList()).let { state ->
            LazyColumn(
                Modifier
                    .padding(it)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                item {
                    val bismillahText =
                        when (surahNumber) {
                            1, 9 -> {
                                "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ"
                            }

                            else -> {
                                "بِسْمِ اللهِ الرَّحْمَنِ الرَّحِيْمِ"
                            }
                        }
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = surahNameEn,
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                            Row(
                                Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = surahNameAr,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 19.sp
                                )
                                Spacer(modifier = Modifier.padding(4.dp))
                                Text(
                                    text = "·",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.padding(4.dp))
                                Text(
                                    text = surahNameId,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                )
                            }
                            Divider(
                                Modifier.padding(horizontal = 56.dp, vertical = 16.dp),
                                thickness = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                            Row(Modifier.align(Alignment.CenterHorizontally)) {
                                Text(
                                    text = "Juz ${juzSurah}",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                                Spacer(modifier = Modifier.padding(4.dp))
                                Text(text = "·", color = MaterialTheme.colorScheme.onPrimary)
                                Spacer(modifier = Modifier.padding(4.dp))
                                Text(
                                    text = descendPlace,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                                Spacer(modifier = Modifier.padding(4.dp))
                                Text(text = "·", color = MaterialTheme.colorScheme.onPrimary)
                                Spacer(modifier = Modifier.padding(4.dp))
                                Text(
                                    text = "${totalAyah} Ayat",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                text = "· ${bismillahText} ·",
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        }
                    }
                }
                items(state.value) {
                    Row(Modifier.fillMaxWidth()) {
                        Card(
                            modifier = Modifier
                                .padding(12.dp)
                                .size(34.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = CircleShape
                        ) {
                            Box(Modifier.fillMaxSize()) {
                                Text(
                                    text = "${it.ayatNumber}",
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = Regex("\\d+\$").replace(it.ayatText!!, ""),
                                fontSize = 20.sp,
                                textAlign = TextAlign.End,
                                letterSpacing = 3.sp,
                                lineHeight = 36.sp
                            )
                            Spacer(modifier = Modifier.padding(5.dp))
                            SpannableText(
                                modifier = Modifier.align(Alignment.Start),
                                text = it.tranlateId ?: " ",
                                onClick = { footnotenumber ->
                                    footNoteState.value = it.footnotesId!!
                                    scope.launch { scaffoldState.bottomSheetState.expand() }
                                }
                            )
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                            ) {
                                Row(modifier = Modifier.align(Alignment.End)) {
                                    IconButton(
                                        onClick = { /*TODO*/ }
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_play_circle_24),
                                            contentDescription = null
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            Toast.makeText(context, "Saved!!!", Toast.LENGTH_SHORT)
                                                .show()
                                            scope.launch {
                                                bookmarkDao.insertBookmark(
                                                    Bookmark(
                                                        surahName = it.surahNameEn,
                                                        surahNumber = it.surahNumber,
                                                        ayahNumber = it.ayatNumber
                                                    )
                                                )
                                            }
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_bookmarks_24),
                                            contentDescription = null
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            val sendIntent: Intent = Intent().apply {
                                                action = Intent.ACTION_SEND
                                                putExtra(Intent.EXTRA_TEXT, "${it.ayatText}" )
                                                type = "text/plain"
                                            }
                                            val shareIntent = Intent.createChooser(sendIntent, null)
                                            context.startActivity(shareIntent)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Share,
                                            contentDescription = null
                                        )
                                    }
                                    IconButton(onClick = {
                                        footNoteState.value = it.footnotesId!!
                                        scope.launch { scaffoldState.bottomSheetState.expand() }
                                    }) {
                                        Icon(
                                            Icons.Outlined.Info,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Divider(modifier = Modifier.fillMaxWidth())
                }
            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = "Select Language") },
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
                                        selected = (option == selectedOption),
                                        onClick = { selectedOption = option }
                                    )
                                    Text(
                                        text = option,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Dismiss")
                        }
                    }
                )
            }
            if (showDialogSetting) {
                AlertDialog(
                    onDismissRequest = { showDialogSetting = false },
                    title = { Text(text = "Change Settings") },
                    text = {
                        Column {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Switch(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    checked = GlobalState.isDarkMode,
                                    onCheckedChange = { isChecked ->
                                        GlobalState.isDarkMode = isChecked
                                        SettingPreferences.isDarkMode = isChecked
                                    },
                                    thumbContent = if (GlobalState.isDarkMode) {
                                        {
                                            Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(SwitchDefaults.IconSize),
                                            )
                                        }
                                    } else {
                                        null
                                    }
                                )
                                Text(text = "Dark Mode", modifier = Modifier.padding(start = 8.dp))
                            }
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Switch(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    checked = GlobalState.isDarkMode,
                                    onCheckedChange = { isChecked ->
                                        GlobalState.isDarkMode = isChecked
                                        SettingPreferences.isDarkMode = isChecked
                                    },
                                    thumbContent = if (GlobalState.isDarkMode) {
                                        {
                                            Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(SwitchDefaults.IconSize),
                                            )
                                        }
                                    } else {
                                        null
                                    }
                                )
                                Text(text = "Focus Mode", modifier = Modifier.padding(start = 8.dp))
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showDialogSetting = false }) {
                            Text("Close")
                        }
                    },
                )
            }
        }
    }
}

//modal sheet drawer
//
//var openBottomSheet by rememberSaveable { mutableStateOf(false) }
//val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded  = true)
//
//
//
//// Sheet content
//if (openBottomSheet) {
//    ModalBottomSheet(
//        onDismissRequest = { openBottomSheet = false },
//        sheetState = bottomSheetState,
//    ) {
//        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
//            Button(
//                // Note: If you provide logic outside of onDismissRequest to remove the sheet,
//                // you must additionally handle intended state cleanup, if any.
//                onClick = {
//                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
//                        if (!bottomSheetState.isVisible) {
//                            openBottomSheet = false
//                        }
//                    }
//                }
//            ) {
//                Text("Hide Bottom Sheet")
//            }
//        }
//    }
//}

@Composable
fun AyatFavoriteButton(
    modifier: Modifier = Modifier,
    color: Color = Color(0xffE91E63),
    surahNumber: Int?,
    surahNameEn: String?,
    surahNameAr: String?,
    totalAyah: Int?,
    juzNumber: Int?,
    surahDescend: String?,
    surahBookmark: SurahBookmark? = null,
) {
    val context = LocalContext.current
    val dao = BookmarkDatabase.getInstance(context).dao()
    val scope = rememberCoroutineScope()
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        isFavorite = dao.selectedFavoriteButton(surahNumber!!)
    }

    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            scope.launch {
                if (isFavorite && surahBookmark != null) {
                    dao.deleteSurahBookmark(surahBookmark)
                    isFavorite = false
                } else {
                    dao.insertSurahBookmark(
                        SurahBookmark(
                            surahNameEn!!,
                            surahNameAr!!,
                            totalAyah!!,
                            juzNumber!!,
                            surahDescend!!
                        )
                    )
                    isFavorite = true
                }
            }
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

private fun createMusicItem(
    title: String, ayahNumber: String, surahNumber: String
): MusicItem {
    return MusicItem.Builder()
        .setMusicId("$ayahNumber$surahNumber")
        .autoDuration()
        .setTitle(title)
        .setIconUri(SettingPreferences.selectedQori.qoriImage)
        .setUri("https://everyayah.com/data/${SettingPreferences.selectedQori}/$surahNumber$ayahNumber.mp3")
        .setArtist(SettingPreferences.selectedQori.qoriName)
        .build()
}