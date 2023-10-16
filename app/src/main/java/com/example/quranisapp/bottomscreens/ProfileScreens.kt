@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.quranisapp.bottomscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.quranisapp.R

@Composable
fun ProfileScreens(back: () -> Unit) {
    var textfield by remember {
        mutableStateOf("")
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Profile", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { back() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = colorResource(id = R.color.blue) )
            )
        }
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(it)
                .background(colorResource(id = R.color.white_background))
        ) {
            item {
                Card(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.white)
                    )
                ) {
                    Box(Modifier.fillMaxWidth()) {
                        Image(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = ""
                        )
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(modifier = Modifier.align(Alignment.CenterStart)) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "")
                            Spacer(modifier = Modifier.padding(end = 8.dp))
                            Text(text = "Username")
                        }
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(modifier = Modifier.align(Alignment.CenterStart)) {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = "")
                            Spacer(modifier = Modifier.padding(end = 8.dp))
                            Text(text = "Phone Number")
                        }
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(modifier = Modifier.align(Alignment.CenterStart)) {
                            Icon(imageVector = Icons.Default.Email, contentDescription = "")
                            Spacer(modifier = Modifier.padding(end = 8.dp))
                            Text(text = "Email")
                        }
                    }
                    Button(
                        onClick = { /*TODO*/ }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(text = "Edit", textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}