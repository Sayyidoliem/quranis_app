package com.example.quranisapp.splashscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun SignScreens(
    goTo4: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = R.drawable.logo_quranis),
            contentDescription = ""
        )
        Text(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(), textAlign = TextAlign.Center, text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 34.sp)) {
                append("QURANIS\n")
            }
        })
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp, horizontal = 56.dp)
                .fillMaxWidth(), onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(text = "Sign In", modifier = Modifier.padding(5.dp))
        }
        Button(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(horizontal = 56.dp)
            .fillMaxWidth(), onClick = { }) {
            Text(text = "Sign Up", modifier = Modifier.padding(5.dp))
        }
        TextButton(onClick = { goTo4() }) {
            Text(text = "skip for now")
        }
    }
}