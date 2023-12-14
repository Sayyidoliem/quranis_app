package com.olimhouse.qooraanapp.screen

import android.os.Build.VERSION
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.olimhouse.qooraanapp.BuildConfig
import com.olimhouse.qooraanapp.R
import com.olimhouse.qooraanapp.data.kotpref.SettingPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInfoScreens(openDrawer: () -> Unit) {
    val versionApp = BuildConfig.VERSION_NAME
    val sdkVersion = VERSION.SDK_INT
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when (SettingPreferences.isSelectedLanguage) {
                            SettingPreferences.INDONESIA -> {
                                "Info Aplikasi"
                            }

                            else -> {
                                "App Info"
                            }
                        },
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { openDrawer() }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null,
                        )
                    }
                },
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item{
                Image(
                    modifier= Modifier
                        .size(300.dp)
                        .clip(shape = RoundedCornerShape(50.dp)),
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    text = "QOORAAN",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily(Font(R.font.brunoace))
                )
                Text(
                    text = when (SettingPreferences.isSelectedLanguage) {
                        SettingPreferences.INDONESIA -> {
                            "Versi $versionApp"
                        }

                        else -> {
                            "Version $versionApp"
                        }
                    }
                )
                Text(
                    text = when (SettingPreferences.isSelectedLanguage) {
                        SettingPreferences.INDONESIA -> {
                            "Versi SDK : $sdkVersion "
                        }

                        else -> {
                            "SDK Version : $sdkVersion"
                        }
                    }
                )
                Spacer(modifier = Modifier.padding(16.dp))
            }
        }
    }
}