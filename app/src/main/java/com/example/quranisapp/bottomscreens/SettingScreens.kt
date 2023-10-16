package com.example.quranisapp.bottomscreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quranisapp.R
import com.example.quranisapp.data.kotpref.SettingPreferences
import com.example.quranisapp.utils.GlobalState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreens(goToProfile : () -> Unit ) {
    var checked by remember { mutableStateOf(false) }
    var checked1 by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Settings", color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                actions = {
                    IconButton(onClick = { goToProfile() }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                }
            )
        }
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                Card(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = "Notifications",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.blue),
                        modifier = Modifier.padding(16.dp)
                    )
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Email Notifications",
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        Switch(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            checked = checked,
                            onCheckedChange = {
                                checked = it
                            },
                            thumbContent = if (checked) {
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
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Push Notifications",
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        Switch(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            checked = checked1,
                            onCheckedChange = {
                                checked1 = it
                            },
                            thumbContent = if (checked1) {
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
                    Text(
                        text = "Theme",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.blue),
                        modifier = Modifier.padding(16.dp)
                    )
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(modifier = Modifier.align(Alignment.CenterStart), text = "Dark Mode")
                        Switch(
                            modifier = Modifier.align(Alignment.CenterEnd),
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
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(text = "Change Colors",modifier = Modifier.align(Alignment.CenterStart))
                        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_open_in_new_24),
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(text = "Change Qori",modifier = Modifier.align(Alignment.CenterStart))
                        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_open_in_new_24),
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Change Language",
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        Image(
                            painterResource(id = R.drawable.baseline_translate_24),
                            contentDescription = "",
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                    Text(
                        text = "Connected Account",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.blue),
                        modifier = Modifier.padding(16.dp)
                    )
                    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.bi_google),
                            contentDescription = ""
                        )
                        Text(modifier = Modifier.padding(start = 16.dp), text = "Google")
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}