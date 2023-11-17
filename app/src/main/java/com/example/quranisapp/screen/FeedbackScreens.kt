package com.example.quranisapp.screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.quranisapp.data.kotpref.SettingPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreens(openDrawer: () -> Unit) {
    var openDialog by remember { mutableStateOf(false) }
    var textSubject by rememberSaveable { mutableStateOf("") }
    var textMessage by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = when (SettingPreferences.isSelectedLanguage) {
                            SettingPreferences.INDONESIA -> {
                                "Umpan Balik"
                            }

                            else -> {
                                "Feedback"
                            }
                        },
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { openDrawer() }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                    }
                },
            )
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Text(
                text = when (SettingPreferences.isSelectedLanguage) {
                    SettingPreferences.INDONESIA -> {
                        "Apakah Anda memiliki kritik, saran, atau umpan balik? Kirimkan pesan kepada kami"
                    }

                    else -> {
                        "Do you have any criticism, suggestions, or feedback? Send us a message"
                    }
                },
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
            Button(onClick = { openDialog = true }) {
                Text(
                    text = when (SettingPreferences.isSelectedLanguage) {
                        SettingPreferences.INDONESIA -> {
                            "Kirim Pesan"
                        }

                        else -> {
                            "Send Message"
                        }
                    },
                )
            }
        }
        if (openDialog) {
            Dialog(
                properties = DialogProperties(
                    usePlatformDefaultWidth = false
                ),
                onDismissRequest = { openDialog = false }
            ) {
                // In order to make the dialog full screen, we need to use
                // Modifier.fillMaxSize()
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = when (SettingPreferences.isSelectedLanguage) {
                                        SettingPreferences.INDONESIA -> {
                                            "Kirim E-mail kepada Developer"
                                        }

                                        else -> {
                                            "Send E-mail To Developer"
                                        }
                                    },
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { openDialog = false }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null
                                    )
                                }
                            },
                        )
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        var emailDeveloper  = "sayyid.olim12@gmail.com"
                        Column(
                            verticalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()

                                    .padding(horizontal = 16.dp),
                                value = emailDeveloper,
                                onValueChange = { emailDeveloper = it},
                                label = {
                                    Text(
                                        when (SettingPreferences.isSelectedLanguage) {
                                            SettingPreferences.INDONESIA -> {
                                                "Kepada"
                                            }

                                            else -> {
                                                "To"
                                            }
                                        },
                                    )
                                },
                            )
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                value = textSubject,
                                onValueChange = { textSubject = it },
                                placeholder = {
                                    Text(
                                        text = when (SettingPreferences.isSelectedLanguage) {
                                            SettingPreferences.INDONESIA -> {
                                                "contoh: bug, kritik, saran"
                                            }

                                            else -> {
                                                "example : bugs, criticism, suggestions"
                                            }
                                        },
                                    )
                                },
                                label = {
                                    Text(
                                        when (SettingPreferences.isSelectedLanguage) {
                                            SettingPreferences.INDONESIA -> {
                                                "Perihal"
                                            }

                                            else -> {
                                                "Subject"
                                            }
                                        },
                                    )
                                }
                            )
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(2f)
                                    .padding(horizontal = 16.dp),
                                value = textMessage,
                                onValueChange = { textMessage = it },
                                label = {
                                    Text(
                                        when (SettingPreferences.isSelectedLanguage) {
                                            SettingPreferences.INDONESIA -> {
                                                "Pesan"
                                            }

                                            else -> {
                                                "Message"
                                            }
                                        },
                                    )
                                },
                            )
                            Button(
                                onClick = {

                                    val i = Intent(Intent.ACTION_SEND)

                                    // on below line we are passing email address,
                                    // email subject and email body
                                    val emailAddress = emailDeveloper
                                    i.putExtra(Intent.EXTRA_EMAIL,emailAddress)
                                    i.putExtra(Intent.EXTRA_SUBJECT,textSubject)
                                    i.putExtra(Intent.EXTRA_TEXT,textMessage)

                                    // on below line we are
                                    // setting type of intent
                                    i.setType("message/rfc822")

                                    // on the below line we are starting our activity to open email application.
                                    context.startActivity(Intent.createChooser(i,"Choose an Email client : "))
                                },
                                Modifier
                                    .align(Alignment.End)
                                    .padding(end = 16.dp, bottom = 16.dp, top = 8.dp)
                            ) {
                                Text(
                                    when (SettingPreferences.isSelectedLanguage) {
                                        SettingPreferences.INDONESIA -> {
                                            "Kirim"
                                        }

                                        else -> {
                                            "Send"
                                        }
                                    },
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}