package com.olimhouse.qooraanapp.screen//package com.example.quranisapp.tabrowscreens
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.MoreVert
//import androidx.compose.material.icons.filled.Share
//import androidx.compose.material.icons.outlined.Edit
//import androidx.compose.material.icons.outlined.Email
//import androidx.compose.material.icons.outlined.Settings
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.DropdownMenu
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.colorResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.quranisapp.R
//import com.example.quranisapp.bottomscreens.FavoriteButton
//import com.example.quranisapp.data.database.QoranDatabase
//import kotlinx.coroutines.flow.collectLatest
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SurahPerJuzScreens(onClick: () -> Unit,goBack:()->Unit) {
//
//    val context = LocalContext.current
//    val dao = QoranDatabase.getInstance(context).dao()
//    val list = dao.getSurahjuz()
//
//    var surahName by remember {
//        mutableStateOf("")
//    }
//
//    var expanded by remember { mutableStateOf(false) }
//
//    LaunchedEffect(key1 = true) {
//        list.collectLatest {
//            val name = it[0].juzNumber
//            surahName = "${name}"
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                navigationIcon = {
//                    IconButton(onClick = { goBack() }) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowBack,
//                            contentDescription = "",
//                            tint = Color.White
//                        )
//                    }
//                },
//                title = { Text(text = "Juz ${surahName}", color = Color.White) },
//                colors = TopAppBarDefaults.smallTopAppBarColors(
//                    containerColor = colorResource(
//                        id = R.color.blue
//                    )
//                ),
//                actions = {
//                    Icon(
//                        imageVector = Icons.Default.Share,
//                        contentDescription = "",
//                        tint = Color.White,
//                        modifier = Modifier.clickable { })
//
//                    Icon(
//                        imageVector = Icons.Default.MoreVert,
//                        contentDescription = "",
//                        tint = Color.White,
//                        modifier = Modifier.clickable { expanded = true })
//                    DropdownMenu(
//                        expanded = expanded,
//                        onDismissRequest = { expanded = false }
//                    ) {
//                        DropdownMenuItem(
//                            text = { Text("Change Language") },
//                            onClick = { /* Handle edit! */ },
//                            leadingIcon = {
//                                Icon(
//                                    Icons.Outlined.Edit,
//                                    contentDescription = null
//                                )
//                            })
//                        DropdownMenuItem(
//                            text = { Text("Settings") },
//                            onClick = { /* Handle settings! */ },
//                            leadingIcon = {
//                                Icon(
//                                    Icons.Outlined.Settings,
//                                    contentDescription = null
//                                )
//                            })
//                        DropdownMenuItem(
//                            text = { Text("Send Feedback") },
//                            onClick = { /* Handle send feedback! */ },
//                            leadingIcon = {
//                                Icon(
//                                    Icons.Outlined.Email,
//                                    contentDescription = null
//                                )
//                            },
//                            trailingIcon = { Text("F11", textAlign = TextAlign.Center) })
//                    }
//                }
//            )
//        }
//    ) {
//        list.collectAsState(initial = emptyList()).let { state ->
//            LazyColumn(Modifier.padding(it)) {
//                items(state.value){
//                    Card(modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp, vertical = 8.dp)
//                        .clip(RoundedCornerShape(10))
//                        .clickable {
//                            onClick()
//                        }
//                    ){
//                        Box(Modifier.fillMaxSize()) {
//                            Row {
//                                Card(
//                                    modifier = Modifier
//                                        .padding(12.dp)
//                                        .size(34.dp),
//                                    colors = CardDefaults.cardColors(
//                                        containerColor = Color.LightGray
//                                    )
//                                ) {
//                                    Box(Modifier.fillMaxSize()) {
//                                        Text(text = "${it.surahNumber}", modifier = Modifier.align(
//                                            Alignment.Center))
//                                    }
//                                }
//                                Column(modifier = Modifier.padding(8.dp)) {
//                                    Text(text = "${it.surahNameEN} | ${it.surahNameAr}",fontSize = 18.sp, fontWeight = FontWeight.Bold)
//                                    Text(text = "${it.numberOfAyah} Ayat")
//                                    Text(text = "Juz ${it.juzNumber} | ${it.surahDescendPlace}")
//                                }
//                                Box(modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(16.dp), Alignment.TopEnd){
//                                    FavoriteButton(modifier = Modifier.align(Alignment.TopEnd))
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}