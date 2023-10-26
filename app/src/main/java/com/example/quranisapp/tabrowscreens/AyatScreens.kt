package com.example.quranisapp.tabrowscreens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.material3.OutlinedButton
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
import com.example.quranisapp.data.kotpref.Qories
import com.example.quranisapp.data.kotpref.SettingPreferences
import com.example.quranisapp.read.component.SpannableText
import com.example.quranisapp.service.player.MyPlayerServices
import com.example.quranisapp.utils.Converters
import com.example.quranisapp.utils.Converters.toAnnotatedString
import com.example.quranisapp.utils.GlobalState
import com.example.quranisapp.utils.TajweedHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import snow.player.PlayMode
import snow.player.PlayerClient
import snow.player.audio.MusicItem
import snow.player.playlist.Playlist

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
            juzNumber != -1 && surahNumber != -1 -> {
                dao.getAyatJozz(juzNumber)
            }

            surahNumber != -1 -> {
                dao.getAyatSurah(surahNumber)
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

    var footNoteState = remember { mutableStateOf("") }

    val scaffoldState = rememberBottomSheetScaffoldState()

    val playerClient = remember {
        PlayerClient.newInstance(context, MyPlayerServices::class.java)
    }

    val changeLanguageState by remember {
        mutableStateOf(SettingPreferences.isSelectedLanguage)
    }

    val lazyColumnState = rememberLazyListState()

    val _currentPlayedAyah by remember {
        mutableStateOf("")
    }

    var showQoriDialog by remember {
        mutableStateOf(false)
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, footNoteState.value)
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, null)
                                context.startActivity(shareIntent)
                            },
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Text(text = "Share")
                        }
                        Button(
                            onClick = {
                                scope.launch {
                                    if (scaffoldState.bottomSheetState.hasExpandedState) {
                                        scaffoldState.bottomSheetState.show()
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Text(text = "Close")
                        }
                    }

                }
            }
        },
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
                        text = surahNameEn,
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
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "$surahNameEn | $surahNameAr | $surahNameId | Juz $juzSurah | $descendPlace"
                                    )
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
                            text = { Text(text = "Play Qori") },
                            onClick = {
//                            playerClient.stop()
//                            val formatSurahNumber =
//                                Converters.convertNumberToThreeDigits(it.surahNumber!!)
//                            val formatAyahNumber =
//                                Converters.convertNumberToThreeDigits(it.ayatNumber!!)
//                            val musicItem = createMusicItem(
//                                title = "${it.surahNameEn}: ${it.ayatNumber}",
//                                surahNumber = formatSurahNumber,
//                                ayahNumber = formatAyahNumber
//                            )
//                            playerClient.connect { _ ->
//                                Toast.makeText(context, "Play", Toast.LENGTH_SHORT)
//                                    .show()
//                                playerClient.playMode = PlayMode.SINGLE_ONCE
//                                val qoriPlaylist =
//                                    createSinglePlaylist(musicItem = musicItem)
//                                playerClient.setPlaylist(qoriPlaylist!!, true)
//                            }
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
            LaunchedEffect(key1 = true) {
                if (surahNumber != -1 && juzNumber != -1) {
                    delay(600)
                    val indexBySurahNumber =
                        state.value.indexOfFirst { it.surahNumber == surahNumber }
                    lazyColumnState.scrollToItem(indexBySurahNumber ?: 0)
                }
            }
            LazyColumn(
                Modifier
                    .padding(it)
                    .background(MaterialTheme.colorScheme.background),
                state = lazyColumnState
            ) {
                items(state.value) {
                    //card info per surat
                    if (it.ayatNumber == 1) {
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
                                    text = "${it.surahNameEn}",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                )
                                surahNameEn = it.surahNameEn!!
                                surahNameAr = it.surahNameAr!!
                                surahNameId = it.surahNameId!!
                                juzSurah = it.juzNumber!!
                                descendPlace = it.surahDescendPlace!!
                                Row(
                                    Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "${it.surahNameAr}",
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
                                        text = "${it.surahNameId}",
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
                                        text = "${it.surahDescendPlace}",
                                        color = MaterialTheme.colorScheme.onPrimary,
                                    )
                                    Spacer(modifier = Modifier.padding(4.dp))
                                    Text(text = "·", color = MaterialTheme.colorScheme.onPrimary)
                                    Spacer(modifier = Modifier.padding(4.dp))
                                    Text(
                                        text = "${it.ayatNumber} Ayat",
                                        color = MaterialTheme.colorScheme.onPrimary,
                                    )
                                }
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp),
                                    text = "· $bismillahText ·",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                )
                            }
                        }
                    }
                    //list per item
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
                                text = TajweedHelper.getTajweed(
                                    context = context,
                                    s = Regex("\\d+\$").replace(it.ayatText!!, ""),
                                ).toAnnotatedString(MaterialTheme.colorScheme.primary),
                                fontSize = 20.sp,
                                textAlign = TextAlign.End,
                                letterSpacing = 3.sp,
                                lineHeight = 36.sp
                            )
                            Spacer(modifier = Modifier.padding(5.dp))
                            SpannableText(
                                modifier = Modifier.align(Alignment.Start),
                                text = it.tranlateId ?: " ",
                                onClick = { _ ->
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
                                        onClick = {
                                            playerClient.stop()
                                            val formatSurahNumber =
                                                Converters.convertNumberToThreeDigits(it.surahNumber!!)
                                            val formatAyahNumber =
                                                Converters.convertNumberToThreeDigits(it.ayatNumber!!)
                                            val musicItem = createMusicItem(
                                                title = "${it.surahNameEn}: ${it.ayatNumber}",
                                                surahNumber = formatSurahNumber,
                                                ayahNumber = formatAyahNumber
                                            )
                                            playerClient.connect { _ ->
                                                Toast.makeText(context, "Play", Toast.LENGTH_SHORT)
                                                    .show()
                                                playerClient.playMode = PlayMode.SINGLE_ONCE
                                                val qoriPlaylist =
                                                    createSinglePlaylist(musicItem = musicItem)
                                                playerClient.setPlaylist(qoriPlaylist!!, true)
                                            }
                                        }
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
                                                putExtra(Intent.EXTRA_TEXT, "${it.ayatText}")
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
                                        footNoteState.value = it.footnotesId.toString()
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
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    text = "Change Qori"
                                )
                                IconButton(onClick = {
                                    showQoriDialog = true
                                    showDialogSetting = false
                                }, modifier = Modifier.fillMaxWidth()) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_open_in_new_24),
                                        contentDescription = ""
                                    )
                                }
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
            if (showQoriDialog) {
                AlertDialog(
                    modifier = Modifier.height(424.dp),
                    onDismissRequest = { showQoriDialog = false },
                    title = { Text(text = "Select Qori") },
                    text = {
                        LazyColumn {
                            items(Qories.values()) { qories ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = qories == GlobalState.isSelectedQori,
                                        onClick = {
                                            SettingPreferences.selectedQori = qories
                                            GlobalState.isSelectedQori = qories
                                        }
                                    )
                                    TextButton(onClick = {
                                        SettingPreferences.selectedQori = qories
                                        GlobalState.isSelectedQori = qories
                                    }) {
                                        Text(
                                            text = qories.qoriName,
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                }
                            }

                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showQoriDialog = false }) {
                            Text("Apply")
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
        .setUri("https://everyayah.com/data/${SettingPreferences.selectedQori.id}/${surahNumber}${ayahNumber}.mp3")
        .setArtist(SettingPreferences.selectedQori.qoriName)
        .build()
}

private fun createSinglePlaylist(
    musicItem: MusicItem
): Playlist? {
    return Playlist.Builder().append(musicItem).build()
}

private fun createSurahPlaylist(
    musicItem: MusicItem
): Playlist? {
    return Playlist.Builder().append(musicItem).build()
}