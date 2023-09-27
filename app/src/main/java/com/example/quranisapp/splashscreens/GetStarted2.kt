package com.example.quranisapp.splashscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quranisapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetStarted2(
    goTo3: () -> Unit,
) {
    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text(text = "QURANIS") }) }) {
        Column(Modifier.padding(it).fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Image(modifier = Modifier.fillMaxWidth(), painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "")
            Text(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), textAlign = TextAlign.Center, text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 34.sp)) {
                    append("Lorem Ipsum\n")
                }
                withStyle(style = SpanStyle(fontSize = 18.sp)) {
                    append("Lorem Ipsum DOlor SIt Amet")
                }
            })
            Button(modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp), onClick = { goTo3() }) {
                Text(text = "Next", modifier = Modifier.padding(5.dp))
            }
        }
    }
}