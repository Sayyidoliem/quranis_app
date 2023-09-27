package com.example.quranisapp.tabrowscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quranisapp.R
import com.example.quranisapp.data.database.QoranDatabase
import kotlinx.coroutines.flow.collectLatest

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

    LaunchedEffect(key1 = true){
        list.collectLatest {
            val nameId = it[0].surahNameId
            surahNameId = "$nameId"
        }
    }

    LaunchedEffect(key1 = true){
        list.collectLatest {
            val descend = it[0].surahDescendPlace
            descendPlace = "$descend"
        }
    }

    LaunchedEffect(key1 = true){
        list.collectLatest {
            val juz = it[0].juzNumber
            juzSurah = juz!!
        }
    }

    LaunchedEffect(key1 = true){
        list.collectLatest {
            val juz = it[0].ayatNumber
            totalAyah = juz!!
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                },
                title = { Text(text = surahNameEn, color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = colorResource(
                        id = R.color.blue
                    )
                ),
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier.clickable { })
                    }
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "",
                            tint = Color.White)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Change Language") },
                            onClick = { /* Handle edit! */ },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Edit,
                                    contentDescription = null
                                )
                            })
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = { /* Handle settings! */ },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Settings,
                                    contentDescription = null
                                )
                            })
                        DropdownMenuItem(
                            text = { Text("Footnote") },
                            onClick = { /* Handle settings! */ },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Info,
                                    contentDescription = null
                                )
                            })
                        DropdownMenuItem(
                            text = { Text("Send Feedback") },
                            onClick = { /* Handle send feedback! */ },
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
                    .background(colorResource(id = R.color.white_background))
            ) {
                item {
                    val bismillahText =
                        when (surahNumber) {
                            1,9 -> {
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
                            containerColor = colorResource(id = R.color.blue)
                        )
                    ) {
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = surahNameEn,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                            Row(
                                Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(8.dp)) {
                                Text(
                                    text = surahNameAr,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 19.sp
                                )
                                Spacer(modifier = Modifier.padding(4.dp))
                                Text(
                                    text = "·",
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.padding(4.dp))
                                Text(
                                    text = surahNameId,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                )
                            }
                            Divider(
                                Modifier.padding(horizontal = 56.dp, vertical = 16.dp),
                                thickness = 2.dp,
                                color = Color.White
                            )
                            Row(Modifier.align(Alignment.CenterHorizontally)) {
                                Text(
                                    text = "Juz ${juzSurah}",
                                    color = Color.White,
                                )
                                Spacer(modifier = Modifier.padding(4.dp))
                                Text(text = "·", color = Color.White)
                                Spacer(modifier = Modifier.padding(4.dp))
                                Text(
                                    text = descendPlace,
                                    color = Color.White,
                                )
                                Spacer(modifier = Modifier.padding(4.dp))
                                Text(text = "·", color = Color.White)
                                Spacer(modifier = Modifier.padding(4.dp))
                                Text(
                                    text = "${totalAyah} Ayat",
                                    color = Color.White,
                                )
                            }
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                text = "· ${bismillahText} ·",
                                color = Color.White,
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
                                containerColor = Color.White
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
                                text = "${it.ayatText}",
                                fontSize = 20.sp,
                                textAlign = TextAlign.End,
                                letterSpacing = 1.sp,
                                lineHeight = 28.sp
                            )
                            Spacer(modifier = Modifier.padding(5.dp))
                            Text(
                                text = "${it.tranlateId}",
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                    Divider(modifier = Modifier.fillMaxWidth())
                }
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