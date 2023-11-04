package com.example.quranisapp.bottomscreens

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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quranisapp.data.kotpref.Qories
import com.example.quranisapp.data.kotpref.SettingPreferences
import com.example.quranisapp.utils.GlobalState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreens(openDrawer: () -> Unit) {
    var checked by remember { mutableStateOf(false) }
    var checked1 by remember { mutableStateOf(false) }
    var showQoriDialog by remember { mutableStateOf(false) }
    var showTranslateDialog by remember { mutableStateOf(false) }
    val radioOptions = listOf("English", "Indonesia")
    var selectedOption by remember { mutableStateOf(radioOptions[0]) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Settings", color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
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
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                Card(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
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
                    Text(
                        text = "Theme",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(16.dp)
                    )
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            text = "Dark Mode"
                        )
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
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Change Qori",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        IconButton(onClick = { showQoriDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = ""
                            )
                        }
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Change Language",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        IconButton(onClick = { showTranslateDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = "",
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.fillMaxSize()) {
                        Text(
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.Bottom)
                                .fillMaxWidth(),
                            text = "QURANIS",
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
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
                },
                confirmButton = {
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
                text = {
                    Column {
                        radioOptions.forEach { option ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (option == selectedOption),
                                    onClick = { selectedOption = option }
                                )
                                Text(
                                    text = option,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
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