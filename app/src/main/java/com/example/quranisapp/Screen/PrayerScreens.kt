package com.example.quranisapp.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quranisapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerScreens(
    back: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = { IconButton(onClick = { back() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "", tint = Color.White)
                }},
                title = { Text(text = "Prayer Timings", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.blue)),
                )
        }
    ) {
        LazyColumn(Modifier.padding(it).background(colorResource(id = R.color.white_background))) {
            //next prayer
            item{
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(10)),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.white)
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(
                            modifier = Modifier
                                .padding(vertical = 30.dp)
                                .padding(start = 20.dp)
                        ) {
                            Text(text = "Next Prayer", fontSize = 20.sp,fontWeight = FontWeight.SemiBold, color = colorResource(id = R.color.blue))
                            Text(
                                text = "11.30",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Blue
                            )
                            Spacer(modifier = Modifier.size(20.dp))
                            Text(text = "00.01.54", fontSize = 20.sp)
                            Spacer(modifier = Modifier.size(20.dp))
                            Text(text = "Jakarta, Indonesia",  fontSize = 20.sp)
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
            //today pray
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(10)),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.white)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 30.dp, horizontal = 20.dp)
                    ) {
                        Text(text = "Today Pray", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = colorResource(id = R.color.blue))
                        Spacer(modifier = Modifier.padding(8.dp))
                        Button(onClick = { /*TODO*/ }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)) {
                            Icon(imageVector = Icons.Default.Done, contentDescription = "")
                            Text(text = "Shubuh", modifier = Modifier.padding(start = 16.dp))
                        }
                        Button(onClick = { /*TODO*/ }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)) {
                            Text(text = "Dzhuhur", modifier = Modifier.padding(start = 16.dp))
                        }
                        Button(onClick = { /*TODO*/ }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)) {
                            Text(text = "Ashar", modifier = Modifier.padding(start = 16.dp))
                        }
                        Button(onClick = { /*TODO*/ }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)) {
                            Text(text = "Maghrib", modifier = Modifier.padding(start = 16.dp))
                        }
                        Button(onClick = { /*TODO*/ }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)) {
                            Text(text = "Isya", modifier = Modifier.padding(start = 16.dp))
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                    }
                }
            }
            //read hadist
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(10)),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.white)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 30.dp, horizontal = 20.dp)
                    ) {
                        Text(text = "Studying Hadist", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = colorResource(id = R.color.blue))
                        Spacer(modifier = Modifier.padding(8.dp))
                        Button(onClick = { /*TODO*/ }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)) {
                            Icon(imageVector = Icons.Default.Done, contentDescription = "", )
                            Text(text = "Hadist", modifier = Modifier.padding(start = 16.dp))
                        }
                        Button(onClick = { /*TODO*/ }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)) {
                            Text(text = "Hadist", modifier = Modifier.padding(start = 16.dp))
                        }
                        Button(onClick = { /*TODO*/ }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)) {
                            Text(text = "Hadist", modifier = Modifier.padding(start = 16.dp))
                        }
                        Button(onClick = { /*TODO*/ }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)) {
                            Text(text = "Hadist", modifier = Modifier.padding(start = 16.dp))
                        }
                        Button(onClick = { /*TODO*/ }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)) {
                            Text(text = "Hadist", modifier = Modifier.padding(start = 16.dp))
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                        Row {
                            Text(text = "Last Check:  ", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = "9.03.2023", fontSize = 20.sp,fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}