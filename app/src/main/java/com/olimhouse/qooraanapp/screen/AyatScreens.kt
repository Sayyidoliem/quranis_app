package com.olimhouse.qooraanapp.screen

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.olimhouse.qooraanapp.data.database.AyatSearch
import com.olimhouse.qooraanapp.data.database.BookmarkDatabase
import com.olimhouse.qooraanapp.data.database.QoranDatabase
import com.olimhouse.qooraanapp.data.database.entities.Bookmark
import com.olimhouse.qooraanapp.data.database.entities.SurahBookmark
import com.olimhouse.qooraanapp.data.kotpref.LastReadPreferences
import com.olimhouse.qooraanapp.data.kotpref.Qories
import com.olimhouse.qooraanapp.data.kotpref.SettingPreferences
import com.olimhouse.qooraanapp.read.component.SpannableText
import com.olimhouse.qooraanapp.service.player.MyPlayerServices
import com.olimhouse.qooraanapp.utils.Converters
import com.olimhouse.qooraanapp.utils.Converters.toAnnotatedString
import com.olimhouse.qooraanapp.utils.GlobalState
import com.olimhouse.qooraanapp.utils.TajweedHelper
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
    pageNumber: Int,
    index: Int
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val dao = QoranDatabase.getInstance(context).dao()
    val bookmarkDao = BookmarkDatabase.getInstance(context).bookmarkDao()
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

    val lazyColumnState = rememberLazyListState()

    LaunchedEffect(Unit) {
        delay(500)
        if (index == -1) {
            return@LaunchedEffect
        }
        lazyColumnState.animateScrollToItem(index)
    }

    val radioOptions = listOf("Indonesian", "English")
    var selectedOption by remember { mutableIntStateOf(SettingPreferences.isSelectedLanguage) }
    var showTranslateDialog by remember { mutableStateOf(false) }
    var showDialogSetting by remember { mutableStateOf(false) }

    val footNoteState = remember { mutableStateOf("") }

    val playerClient = remember {
        PlayerClient.newInstance(context, MyPlayerServices::class.java)
    }

    val playQori = mutableListOf<MusicItem>()

    var showQoriDialog by remember { mutableStateOf(false) }

    var isQoriPlay by rememberSaveable { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    var textSearchBar by rememberSaveable { mutableStateOf("") }
    var searchBar by remember { mutableStateOf(false) }
    var isShowSearchBar by remember { mutableStateOf(false) }

    var isBottomSheetShow by remember { mutableStateOf(false) }

    val modalBottomState = rememberModalBottomSheetState()

    val searchResult = remember {
        mutableStateListOf<AyatSearch>()
    }

    val fabVisibility by remember { derivedStateOf { lazyColumnState.isScrollInProgress } }

    var openFeedBackDialog by remember { mutableStateOf(false) }
    var textSubject by rememberSaveable { mutableStateOf("") }
    var textMessage by rememberSaveable { mutableStateOf(("")) }

    Scaffold(
        /*
        isQoriPlay = true
        list.collectAsState(initial = emptyList()).let { state ->
            Column {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("$surahNameEn\n")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("qoriName")
                        }
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = { playerClient.skipToPrevious() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_skip_previous_24),
                            contentDescription = null,
                            modifier = Modifier.size(120.dp)
                        )
                    }
                    IconButton(onClick = {
                        playerClient.stop()
                        state.value.forEach {
                            val formatSurahNumber =
                                Converters.convertNumberToThreeDigits(it.surahNumber!!)
                            val formatAyahNumber =
                                Converters.convertNumberToThreeDigits(it.ayatNumber!!)
                            val musicItem = createMusicItem(
                                title = "${it.surahNameEn}: ${it.ayatNumber}",
                                surahNumber = formatSurahNumber,
                                ayahNumber = formatAyahNumber
                            )
                            playQori.add(musicItem)
                        }

                        playerClient.connect { _ ->
                            Toast.makeText(context, "Play", Toast.LENGTH_SHORT)
                                .show()
                            playerClient.playMode = PlayMode.PLAYLIST_LOOP
                            val qoriPlaylist =
                                createSurahPlaylist(playQori = playQori)
                            playerClient.setPlaylist(qoriPlaylist, true)
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_play_circle_24),
                            contentDescription = null,
                            modifier = Modifier.size(300.dp)
                        )
                    }
                    IconButton(onClick = { playerClient.skipToNext() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_skip_next_24),
                            contentDescription = null,
                            modifier = Modifier.size(120.dp)
                        )
                    }
                    IconButton(onClick = {
                        playerClient.playPause(); isPlayerPlaying = playerClient.isPlaying
                    }) {
                        Icon(
                            imageVector = if (!isPlayerPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(120.dp)
                        )
                    }
                }
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
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                if (!bottomSheetState.isVisible) {
                                    isQoriPlay = false
                                    playerClient.stop()
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
         */
        topBar = {
            when {
                searchBar -> {
                    DockedSearchBar(
                        query = textSearchBar,
                        onQueryChange = { textSearchBar = it; searchBar = true },
                        onSearch = { query ->
                            searchResult.clear()
                            scope.launch {
                                dao.getAyatBySearch(query).collectLatest { list ->
                                    searchResult.addAll(list)
                                }
                            }
                        },
                        active = isShowSearchBar,
                        placeholder = { Text(text = "Search ") },
                        onActiveChange = { isShowSearchBar = true },
                        tonalElevation = 24.dp,
                        trailingIcon = {
                            when {
                                searchBar -> {
                                    IconButton(onClick = {
                                        if (textSearchBar.isNotEmpty())
                                            textSearchBar = "" else isShowSearchBar = false
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = null
                                        )
                                    }
                                }

                                isShowSearchBar -> {
                                    IconButton(onClick = {
                                        searchBar = !searchBar; isShowSearchBar = false
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = null
                                        )
                                    }
                                }

                                else -> IconButton(onClick = { searchBar = true }) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        leadingIcon = {
                            if (searchBar) {
                                IconButton(onClick = {
                                    searchBar = !searchBar; isShowSearchBar = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    ) {
                        LazyColumn {
                            items(searchResult) {
                                ListItem(headlineContent = { Text(text = "${it.numberOfAyah}") },
                                    leadingContent = { Text(text = "${it.ayatText}") })
                            }
                        }
                    }
                }

                else -> {
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
                        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                        actions = {
                            IconButton(onClick = { searchBar = true }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
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
                                        scope.launch {
                                            isBottomSheetShow = true
                                            modalBottomState.show()
                                        }
                                        expanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Outlined.Info,
                                            contentDescription = null
                                        )
                                    })
                                DropdownMenuItem(
                                    text = { Text(text = "Play Surah") },
                                    onClick = {
                                        isQoriPlay = true
                                        expanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.PlayCircle,
                                            contentDescription = null
                                        )
                                    })
                                DropdownMenuItem(
                                    text = { Text("Send Feedback") },
                                    onClick = {
                                        openFeedBackDialog = true
                                        expanded = false
                                    },
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
            }
        },
        bottomBar = {
            when {
                isQoriPlay -> {
                    BottomAppBar {
                        list.collectAsState(initial = emptyList()).let { state ->
                            state.value
                            ListItem(
                                headlineContent = {
                                    Text(text = "$surahNameEn\n")
                                },
                                leadingContent = {
                                    AsyncImage(
                                        model = GlobalState.isSelectedQori.qoriImage,
                                        contentDescription = null,
                                        modifier = Modifier.size(56.dp)
                                    )
                                },
                                overlineContent = {
                                    Text(text = "${GlobalState.isSelectedQori}")
                                },
                                trailingContent = {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        IconButton(onClick = { playerClient.skipToPrevious() }) {
                                            Icon(
                                                imageVector = Icons.Default.SkipPrevious,
                                                contentDescription = null,
                                                modifier = Modifier.size(56.dp)
                                            )
                                        }
                                        IconButton(onClick = {
                                            playerClient.stop()
                                            state.value.forEach {
                                                val formatSurahNumber =
                                                    Converters.convertNumberToThreeDigits(it.surahNumber!!)
                                                val formatAyahNumber =
                                                    Converters.convertNumberToThreeDigits(it.ayatNumber!!)
                                                val musicItem = createMusicItem(
                                                    title = "${it.surahNameEn}: ${it.ayatNumber}",
                                                    surahNumber = formatSurahNumber,
                                                    ayahNumber = formatAyahNumber
                                                )
                                                playQori.add(musicItem)
                                            }

                                            playerClient.connect { _ ->
                                                Toast.makeText(context, "Play", Toast.LENGTH_SHORT)
                                                    .show()
                                                playerClient.playMode = PlayMode.LOOP
                                                val qoriPlaylist =
                                                    createSurahPlaylist(playQori = playQori)
                                                playerClient.setPlaylist(qoriPlaylist, true)
                                            }
                                        }) {
                                            Icon(
                                                imageVector = if (isQoriPlay) Icons.Default.PlayCircle else Icons.Default.Pause,
                                                contentDescription = null,
                                                modifier = Modifier.size(56.dp)
                                            )
                                        }
                                        IconButton(onClick = { playerClient.skipToNext() }) {
                                            Icon(
                                                imageVector = Icons.Default.SkipNext,
                                                contentDescription = null,
                                                modifier = Modifier.size(56.dp)
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !fabVisibility,
                enter = slideInVertically {
                    with(density) { 40.dp.roundToPx() }
                } + fadeIn(),
                exit = slideOutVertically {
                    with(density) { 40.dp.roundToPx() }
                } + fadeOut()
            ) {
                when {
                    isQoriPlay -> {
                        FloatingActionButton(onClick = {
                            isQoriPlay = false; playerClient.stop()
                        }) {
                            Icon(imageVector = Icons.Default.Stop, contentDescription = null)
                        }
                    }

                    else -> {
                        FloatingActionButton(onClick = { isQoriPlay = true }) {
                            Icon(imageVector = Icons.Default.PlayCircle, contentDescription = null)
                        }
                    }
                }
            }

        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                itemsIndexed(state.value) { index, qoran ->
                    LastReadPreferences.apply {
                        lastSurahName = qoran.surahNameEn!!
                        lastSurahNumber = qoran.surahNumber!!
                        lastAyaNo = qoran.ayatNumber!!
                        lastDate = System.currentTimeMillis()
                        this.index = index
                    }
                    //card info per surat
                    if (qoran.ayatNumber == 1) {
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
                                    text = "${qoran.surahNameEn}",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                )
                                surahNameEn = qoran.surahNameEn!!
                                surahNameAr = qoran.surahNameAr!!
                                surahNameId = qoran.surahNameId!!
                                juzSurah = qoran.juzNumber!!
                                descendPlace = qoran.surahDescendPlace!!
                                Row(
                                    Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "${qoran.surahNameAr}",
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
                                        text = "${qoran.surahNameId}",
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
                                        text = "${qoran.surahDescendPlace}",
                                        color = MaterialTheme.colorScheme.onPrimary,
                                    )
                                    Spacer(modifier = Modifier.padding(4.dp))
                                    Text(text = "·", color = MaterialTheme.colorScheme.onPrimary)
                                    Spacer(modifier = Modifier.padding(4.dp))
                                    Text(
                                        text = "${qoran.ayatNumber} Ayat",
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
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = Icons.Default.SkipPrevious,
                                        contentDescription = null,
                                        modifier = Modifier.size(120.dp)
                                    )
                                }
                                IconButton(onClick = {
                                    isQoriPlay = true
                                    playerClient.stop()
                                    state.value.forEach { qoran ->
                                        val formatSurahNumber =
                                            Converters.convertNumberToThreeDigits(qoran.surahNumber!!)
                                        val formatAyahNumber =
                                            Converters.convertNumberToThreeDigits(qoran.ayatNumber!!)
                                        val musicItem = createMusicItem(
                                            title = "${qoran.surahNameEn}: ",
                                            surahNumber = formatSurahNumber,
                                            ayahNumber = formatAyahNumber
                                        )
                                        playQori.add(musicItem)
                                    }

                                    playerClient.connect { _ ->
                                        //biar klo play berhentiin sebelummnya
                                        Toast.makeText(context, "Play", Toast.LENGTH_SHORT)
                                            .show()
                                        playerClient.playMode = PlayMode.PLAYLIST_LOOP
                                        val qoriPlaylist =
                                            createSurahPlaylist(playQori = playQori)
                                        playerClient.setPlaylist(qoriPlaylist!!, true)
                                    }
                                }) {
                                    Icon(
                                        imageVector = if (isQoriPlay) Icons.Default.Pause else Icons.Default.PlayCircle,
                                        contentDescription = null,
                                        modifier = Modifier.size(300.dp)
                                    )
                                }
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = Icons.Default.SkipNext,
                                        contentDescription = null,
                                        modifier = Modifier.size(120.dp)
                                    )
                                }
                            }
                        }
                    }
                    //list per item
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
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
                                    text = "${qoran.ayatNumber}",
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                        Column(modifier = Modifier.padding(16.dp)) {
                            val lineHeight = GlobalState.ayahTextSize.sp * 1.3
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = TajweedHelper.getTajweed(
                                    context = context,
                                    s = Regex("\\d+\$").replace(qoran.ayatText!!, ""),
                                ).toAnnotatedString(MaterialTheme.colorScheme.primary),
                                fontSize = GlobalState.ayahTextSize.sp,
                                textAlign = TextAlign.End,
                                letterSpacing = 3.sp,
                                lineHeight = lineHeight
                            )
                            Spacer(modifier = Modifier.padding(5.dp))
                            SpannableText(
                                modifier = Modifier.align(Alignment.Start),
                                text =
                                if (SettingPreferences.isVisibleTranslate) {
                                    ""
                                } else {
                                    when (SettingPreferences.isSelectedLanguage) {
                                        SettingPreferences.ENGLISH -> {
                                            qoran.tranlateEn ?: ""
                                        }

                                        else -> {
                                            qoran.tranlateId ?: " "
                                        }
                                    }
                                },
                                onClick = { _ ->
                                    when (SettingPreferences.isSelectedLanguage) {
                                        SettingPreferences.INDONESIA -> {
                                            footNoteState.value = qoran.footnotesId.toString()
                                        }

                                        else -> {
                                            footNoteState.value = qoran.footnotesEn.toString()
                                        }
                                    }
                                    scope.launch {
                                        modalBottomState.show()
                                        isBottomSheetShow = true
                                    }
                                }
                            )
                            if (!SettingPreferences.isVisibleTranslate) {
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
                                                    Converters.convertNumberToThreeDigits(qoran.surahNumber!!)
                                                val formatAyahNumber =
                                                    Converters.convertNumberToThreeDigits(qoran.ayatNumber!!)
                                                val musicItem = createMusicItem(
                                                    title = "${qoran.surahNameEn}: ${qoran.ayatNumber}",
                                                    surahNumber = formatSurahNumber,
                                                    ayahNumber = formatAyahNumber
                                                )
                                                playerClient.connect { _ ->
                                                    Toast.makeText(
                                                        context,
                                                        "Play",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                    playerClient.playMode = PlayMode.SINGLE_ONCE
                                                    val qoriPlaylist =
                                                        createSinglePlaylist(musicItem = musicItem)
                                                    playerClient.setPlaylist(qoriPlaylist!!, true)
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PlayCircle,
                                                contentDescription = null
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                Toast.makeText(
                                                    context,
                                                    "Saved!!!",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                                scope.launch {
                                                    bookmarkDao.insertBookmark(
                                                        Bookmark(
                                                            surahName = qoran.surahNameEn,
                                                            surahNumber = qoran.surahNumber,
                                                            ayahNumber = qoran.ayatNumber
                                                        )
                                                    )
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Bookmark,
                                                contentDescription = null
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                val sendIntent: Intent = Intent().apply {
                                                    action = Intent.ACTION_SEND
                                                    putExtra(Intent.EXTRA_TEXT, "${qoran.ayatText}")
                                                    type = "text/plain"
                                                }
                                                val shareIntent =
                                                    Intent.createChooser(sendIntent, null)
                                                context.startActivity(shareIntent)
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Share,
                                                contentDescription = null
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                when (SettingPreferences.isSelectedLanguage) {
                                                    SettingPreferences.INDONESIA -> {
                                                        footNoteState.value =
                                                            qoran.footnotesId.toString()
                                                    }

                                                    else -> {
                                                        footNoteState.value =
                                                            qoran.footnotesEn.toString()
                                                    }
                                                }
                                                scope.launch {
                                                    isBottomSheetShow = true
                                                    modalBottomState.show()
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.Info,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Divider(modifier = Modifier.fillMaxWidth())
                }
            }
            if (showTranslateDialog) {
                AlertDialog(
                    onDismissRequest = { showTranslateDialog = false },
                    title = { Text(text = "Select Language") },
                    text = {
                        Column {
                            radioOptions.forEachIndexed { index, option ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (index == selectedOption),
                                        onClick = {
                                            selectedOption = index
                                            SettingPreferences.isSelectedLanguage = index
                                        }
                                    )
                                    TextButton(onClick = {
                                        selectedOption = index
                                        SettingPreferences.isSelectedLanguage = index
                                    }) {
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
                        TextButton(onClick = { showTranslateDialog = false }) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showTranslateDialog = false }) {
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
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Dark Mode")
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
                            }
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Focus Read")
                                Switch(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    checked = GlobalState.isVisibleTranslate,
                                    onCheckedChange = { isChecked ->
                                        GlobalState.isVisibleTranslate = isChecked
                                        SettingPreferences.isVisibleTranslate = isChecked
                                    },
                                    thumbContent = if (GlobalState.isVisibleTranslate) {
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
                            }
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    text = "Change Qori"
                                )
                                IconButton(onClick = {
                                    showQoriDialog = true
                                    showDialogSetting = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.OpenInNew,
                                        contentDescription = ""
                                    )
                                }
                            }
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    text = "Change Language"
                                )
                                IconButton(onClick = {
                                    showTranslateDialog = true
                                    showDialogSetting = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.OpenInNew,
                                        contentDescription = ""
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showDialogSetting = false }) {
                            Text("Confirm")
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
            if (isBottomSheetShow) {
                ModalBottomSheet(
                    sheetState = modalBottomState,
                    onDismissRequest = {
                        scope.launch {
                            modalBottomState.hide()
                            isBottomSheetShow = false
                        }
                    }
                ) {
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
                                            isBottomSheetShow = false
                                            modalBottomState.hide()
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
                }
            }
            if (openFeedBackDialog) {
                Dialog(
                    properties = DialogProperties(
                        usePlatformDefaultWidth = false
                    ),
                    onDismissRequest = { !openFeedBackDialog }
                ) {
                    // In order to make the dialog full screen, we need to use
                    // Modifier.fillMaxSize()
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text(text = "Send E-mail To Developer") },
                                navigationIcon = {
                                    IconButton(onClick = { openFeedBackDialog = false }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = null
                                        )
                                    }
                                },
                            )
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it)
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                verticalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()

                                        .padding(horizontal = 16.dp),
                                    value = "sayyid.olim12@gmail.com",
                                    onValueChange = {},
                                    label = { Text("To") })
                                OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    value = textSubject,
                                    onValueChange = { textSubject = it },
                                    placeholder = { Text(text = "example : bugs, criticism, suggestions") },
                                    label = { Text("Subject") })
                                OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(2f)
                                        .padding(horizontal = 16.dp),
                                    value = textMessage,
                                    onValueChange = { textMessage = it },
                                    label = { Text("Message") },
                                )
                                Button(
                                    onClick = { /* Handle send email */ },
                                    Modifier
                                        .align(Alignment.End)
                                        .padding(end = 16.dp, bottom = 16.dp, top = 8.dp)
                                ) {
                                    Text("Send")
                                }
                            }
                        }
                    }

                }
            }
        }
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
    playQori: List<MusicItem>
): Playlist {
    return Playlist.Builder().appendAll(playQori).build()
}

@Composable
fun AyatFavoriteButton(
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