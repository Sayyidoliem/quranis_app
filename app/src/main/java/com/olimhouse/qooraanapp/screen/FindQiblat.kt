package com.olimhouse.qooraanapp.screen

import android.app.Activity
import android.content.Intent
import android.view.ViewGroup
import android.widget.LinearLayout
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.just.agentweb.AgentWeb

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindQiblat(
    openDrawer: () -> Unit
) {
    val activity = LocalContext.current as Activity
    val context = LocalContext.current
    val url = "https://qiblafinder.withgoogle.com/intl/id/"
    var expanded by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }
    var textSubject by rememberSaveable { mutableStateOf("") }
    var textMessage by rememberSaveable { mutableStateOf(("")) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = "Qiblat",
                    color = MaterialTheme.colorScheme.onPrimary
                )
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
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Share") },
                            onClick = {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "Qiblat Finder by : $url")
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, null)
                                context.startActivity(shareIntent)
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Share,
                                    contentDescription = null
                                )
                            })
                        DropdownMenuItem(
                            text = { Text("Send Feedback") },
                            onClick = {
                                openDialog = true
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Email,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = { Text("F11", textAlign = TextAlign.Center) })
                    }
                }
            )
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(//ambil view yg ada di xml buat compose
                factory = { context ->
                    LinearLayout(context).apply {
                        layoutParams = ViewGroup.LayoutParams(//parameter kayak width n height
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        AgentWeb.with(activity)
                            .setAgentWebParent(this, this.layoutParams)
                            .useDefaultIndicator()
                            .createAgentWeb()
                            .ready()
                            .go(url)//masukin urlnya
                    }
                }
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
                        title = { Text(text = "Send E-mail To Developer") },
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
                    Column(verticalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxSize()) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()

                                .padding(horizontal = 16.dp),
                            value = "sayyid.olim12@gmail.com",
                            onValueChange = {},
                            label = { Text("To") })
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                ,
                            value = textSubject,
                            onValueChange = { textSubject = it },
                            placeholder = { Text(text = "example : bugs, criticism, suggestions") },
                            label = { Text("Subject") })
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(2f)
                                .padding(horizontal = 16.dp),
                            value = textMessage,
                            onValueChange = { textMessage = it },
                            label = { Text("Message") },
                        )
                        Button(
                            onClick = { /* Handle send email */ },
                            Modifier
                                .align(Alignment.End)
                                .padding(end = 16.dp, bottom = 16.dp, top = 8.dp)
                        ) {
                            Text("Send")
                        }
                    }
                }
            }

        }
    }
}