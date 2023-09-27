@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.quranisapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quranisapp.bottomscreens.FavoriteButton


@Composable
fun SearchScreens() {

    var itemList = listOf("Trending","Discover","Top Chart")
    var selectedItem by remember {
        mutableStateOf(0)
    }

    LazyColumn {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(10))
                    .background(colorResource(id = R.color.purple_200))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier
                        .padding(vertical = 30.dp)
                        .padding(start = 20.dp)) {
                        Text(text = "#01", color = Color.White, fontSize = 20.sp)
                        Text(
                            text = "Al-Insyiqaq",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.size(20.dp))
                        Text(text = "Sound Name", color = Color.White, fontSize = 20.sp)
                        Spacer(modifier = Modifier.size(20.dp))
                        Text(text = "Jakarta, Indonesia", color = Color.White, fontSize = 20.sp)
                    }
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "",
                        Modifier
                            .weight(9f / 16f, fill = false)
                            .fillMaxWidth()
                    )
                }
            }
        }
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Read Quran", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                    Text(text = "All")
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
                }
            }
        }
        items(1){
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(10))
                .background(colorResource(id = R.color.purple_200))
            ){
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = ""
                    )
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "Surah Name",fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Juz")
                    }
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), Alignment.TopEnd){
                        FavoriteButton(modifier = Modifier.align(Alignment.TopEnd))
                    }
                }

            }
        }
        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Quran Playlist", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                    Text(text = "All")
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
                }
            }
        }
        items(5){
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(10))
                .background(colorResource(id = R.color.purple_200))
            ){
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = ""
                    )
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "Surah Name",fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Juz")
                    }
                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), Alignment.TopEnd){
                        FavoriteButton(modifier = Modifier.align(Alignment.TopEnd))
                    }
                }
            }
        }
    }
}


//Row() {
//    Card(Modifier.width(44.dp).height(10.dp).padding(8.dp).background(color = Color.Black, shape = RoundedCornerShape(size = 17.dp))) {}
//    Card(Modifier.width(10.dp).height(10.dp).padding(8.dp).background(color = Color.Black, shape = RoundedCornerShape(size = 17.dp))) {}
//    Card(Modifier.width(10.dp).height(10.dp).padding(8.dp).background(color = Color(0xBD2D5EAC), shape = RoundedCornerShape(size = 17.dp))) {}
//}