package com.olimhouse.qooraanapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.olimhouse.qooraanapp.R
import com.olimhouse.qooraanapp.data.kotpref.Qories
import com.olimhouse.qooraanapp.data.kotpref.SettingPreferences
import com.olimhouse.qooraanapp.utils.GlobalState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreens(openDrawer: () -> Unit) {
    var showQoriDialog by remember { mutableStateOf(false) }
    var showTranslateDialog by remember { mutableStateOf(false) }
    val radioOptions = listOf("Indonesian", "English")
    var selectedOption by remember { mutableIntStateOf(SettingPreferences.isSelectedLanguage) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when (SettingPreferences.isSelectedLanguage) {
                            SettingPreferences.INDONESIA -> {
                                "Pengaturan"
                            }
                            else -> {
                                "Settings"
                            }
                        }, color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                navigationIcon = {
                    IconButton(onClick = { openDrawer() }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
            )
        }
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            item {
                /*
                Text(
                    text = "Notifications",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(16.dp)
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Email Notifications",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Switch(
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
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Push Notifications",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Switch(
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
                 */
                /*
                Text(
                    text = "Theme",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(16.dp)
                )
                 */
                ListItem(//dark mode
                    headlineContent = {
                        Text(
                            text = when (SettingPreferences.isSelectedLanguage) {
                                SettingPreferences.INDONESIA -> {
                                    "Mode Gelap"
                                }

                                else -> {
                                    "Dark Mode"
                                }
                            },
                        )
                    },
                    supportingContent = {
                        Text(
                            text = when (SettingPreferences.isSelectedLanguage) {
                                SettingPreferences.INDONESIA -> {
                                    "Mengubah tampilan aplikasi ke tema gelap"
                                }

                                else -> {
                                    "Change the appearance of the app to a dark theme"
                                }
                            },
                        )
                    },
                    trailingContent = {
                        Switch(
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
                )
                Divider()
                ListItem(//fokus read
                    headlineContent = {
                        Text(
                            text =
                            when (SettingPreferences.isSelectedLanguage) {
                                SettingPreferences.INDONESIA -> {
                                    "Mode Fokus Baca"
                                }

                                else -> {
                                    "Focus Read Mode"
                                }
                            },
                        )
                    },
                    supportingContent = {
                        Text(
                            text = when (SettingPreferences.isSelectedLanguage) {
                                SettingPreferences.INDONESIA -> {
                                    "Sembunyikan terjemahan dan widget agar fokus dalam membaca ayat"
                                }

                                else -> {
                                    "Hide translations and widgets to focus on reading the verse"
                                }
                            },
                        )
                    },
                    trailingContent = {
                        Switch(
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
                )
                Divider()
                ListItem(//translate
                    headlineContent = {
                        Text(
                            text =
                            when (SettingPreferences.isSelectedLanguage) {
                                SettingPreferences.INDONESIA -> {
                                    "Ganti Bahasa"
                                }

                                else -> {
                                    "Change Language"
                                }
                            },
                        )
                    },
                    supportingContent = {
                        Text(
                            text = when (SettingPreferences.isSelectedLanguage) {
                                SettingPreferences.INDONESIA -> {
                                    "Ganti Bahasa untuk pengalaman yang lebih baik"
                                }

                                else -> {
                                    "Changing languages for the best experience"
                                }
                            },
                        )
                    },
                    trailingContent = {
                        IconButton(onClick = { showTranslateDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = "",
                            )
                        }
                    }
                )
                Divider()
                ListItem(//change qori
                    headlineContent = {
                        Text(
                            text = when (SettingPreferences.isSelectedLanguage) {
                                SettingPreferences.INDONESIA -> {
                                    "Ganti Qori"
                                }

                                else -> {
                                    "Change Qori"
                                }
                            },

                            )
                    },
                    supportingContent = {
                        Text(
                            text = when (SettingPreferences.isSelectedLanguage) {
                                SettingPreferences.INDONESIA -> {
                                    "Ganti Suara qori untuk pembacaan Al-Quran"
                                }

                                else -> {
                                    "Changing the voice of the reciter for the Qoran"
                                }
                            },
                        )
                    },
                    trailingContent = {
                        IconButton(onClick = { showQoriDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = ""
                            )
                        }
                    }
                )
            }
            item {
                Row(
                    verticalAlignment = Alignment.Bottom, modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 56.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Bottom)
                            .fillMaxWidth(),
                        text = "QOORAAN",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily(Font(R.font.brunoace))
                    )
                }
            }
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
                    Divider(thickness = 2.dp)
                },
                confirmButton = {
                    Divider(thickness = 2.dp)
                    TextButton(onClick = { showQoriDialog = false }) {
                        Text("Apply")
                    }
                },
            )
        }
        if (showTranslateDialog) {
            AlertDialog(
                onDismissRequest = { showTranslateDialog = false },
                title = { Text(text = "Select Language") },
                icon = { Icon(imageVector = Icons.Default.Language, contentDescription = null)},
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
    }
}