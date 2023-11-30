package com.olimhouse.qooraanapp.screen

import android.Manifest
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ShapeLine
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.olimhouse.compose.QOORAANAppTheme
import com.olimhouse.qooraanapp.R
import com.olimhouse.qooraanapp.data.kotpref.SettingPreferences
import com.olimhouse.qooraanapp.utils.GlobalState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class, ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreens(
    goNext: () -> Unit
) {
    val requiredLocation = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    val permissionState = rememberMultiplePermissionsState(permissions = requiredLocation)

    val pagerState = rememberPagerState { 5 }
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage == 4) {
            permissionState.launchMultiplePermissionRequest()
        }
    }
    QOORAANAppTheme {
        Surface {
            Scaffold(
                bottomBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 56.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.weight(1F),
                        ) {
                            if (pagerState.currentPage > 0) {
                                TextButton(
                                    onClick = {
                                        scope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                        }
                                    }
                                ) {
                                    Text(text = "Previous")
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.weight(2F),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            (0..4).forEach { i ->
                                Icon(
                                    imageVector = if (pagerState.currentPage == i) Icons.Default.ShapeLine
                                    else Icons.Default.Circle,
                                    contentDescription = "Step $i",
                                    tint = if (pagerState.currentPage == i) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                            }
                        }
                        TextButton(
                            modifier = Modifier.weight(1F),
                            onClick = {
                                scope.launch {
                                    if (pagerState.currentPage < 4) {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                        return@launch
                                    }
                                    if (pagerState.currentPage == 4) {
                                        goNext()
                                        SettingPreferences.isOnBoarding = false
                                        GlobalState.isOnBoarding = false
                                    }
                                }
                            }
                        ) {
                            Text(
                                text = when {
                                    pagerState.currentPage < 4 -> "Next"
                                    else -> "Finish"
                                }
                            )
                        }

                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    HorizontalPager(
                        state = pagerState,
                        userScrollEnabled = false
                    ) { pageNumber ->
                        when (pageNumber) {
                            0 -> OnBoardingContentWelcome()
                            1 -> OnBoardingContentLocation()
                            2 -> OnBoardingContentTheme()
                            3 -> OnBoardingContentLanguage()
                            4 -> OnBoardingContentComplete()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OnBoardingContentWelcome(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "HELLO",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.height(14.dp))
        Image(
            modifier = Modifier.size(300.dp).clip(shape = RoundedCornerShape(50.dp)),
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "QOORAAN Logo"
        )

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "let's learn what's in the QOORAAN App",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun OnBoardingContentLocation(
    modifier: Modifier = Modifier
) {

    val locationPermission = rememberMultiplePermissionsState(
        permissions = listOf(
            //buat manifest pastiin yg android
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    if (!locationPermission.allPermissionsGranted) {
        LaunchedEffect(key1 = true) {
            locationPermission.launchMultiplePermissionRequest()
        }
    }
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.green_compass),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Qiblat Finder",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.height(12.dp))
        LottieAnimation(
            modifier = Modifier.size(300.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "The QOORAAN feature for find your Qibla cardinal directions",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun OnBoardingContentLanguage(
    modifier: Modifier = Modifier
) {

    var isDialogShowed by remember {
        mutableStateOf(false)
    }
    var languageNameState by remember {
        mutableIntStateOf(
            SettingPreferences.isSelectedLanguage
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Multi Language",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.height(12.dp))
        Icon(
            imageVector = Icons.Default.Language,
            contentDescription = "Language Graphics",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .size(128.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "for a better experience",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Box {
            TextButton(
                onClick = {
                    isDialogShowed = true
                }
            ) {
                Icon(imageVector = Icons.Default.Language, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (languageNameState == 1) "English"
                    else "Indonesia"
                )
                Spacer(modifier = Modifier.width(24.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select Language"
                )
            }
            DropdownMenu(
                expanded = isDialogShowed,
                onDismissRequest = { isDialogShowed = false },
                offset = DpOffset(32.dp, 0.dp)
            ) {
                DropdownMenuItem(
                    text = { Text(text = "Indonesia") },
                    onClick = {
                        isDialogShowed = false
                        SettingPreferences.isSelectedLanguage = SettingPreferences.INDONESIA
                        languageNameState = SettingPreferences.INDONESIA
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = "English") },
                    onClick = {
                        isDialogShowed = false
                        SettingPreferences.isSelectedLanguage = SettingPreferences.ENGLISH
                        languageNameState = SettingPreferences.ENGLISH
                    }
                )
            }
        }
    }
}

@Composable
private fun OnBoardingContentTheme(
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.dark_mode_animation),
    )

    val animationProgress = animateLottieCompositionAsState(composition = composition)

//    LaunchedEffect(GlobalState.isDarkMode){
//        if (GlobalState.isDarkMode) {
//            animationProgress.
//        }else{
//
//        }
//
//    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "QOORAAN App",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.height(12.dp))
        LottieAnimation(
            modifier = Modifier.size(300.dp),
            composition = composition,
//            progress = { animationProgress }
        )
        if (GlobalState.isDarkMode) {
            Icon(
                imageVector = Icons.Default.DarkMode,
                contentDescription = "Dark Mode"
            )
        }
        if (!GlobalState.isDarkMode) {
            Icon(
                imageVector = Icons.Default.LightMode,
                contentDescription = "Dark Mode"
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Can change to dark mode",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Switch(
                checked = GlobalState.isDarkMode,
                onCheckedChange = {
                    GlobalState.isDarkMode = it
                    SettingPreferences.isDarkMode = it
                },
                thumbContent = {
                    if (GlobalState.isDarkMode) {
                        Icon(
                            imageVector = Icons.Default.DarkMode,
                            contentDescription = "Dark Mode"
                        )
                    }
                    if (!GlobalState.isDarkMode) {
                        Icon(
                            imageVector = Icons.Default.LightMode,
                            contentDescription = "Dark Mode"
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun OnBoardingContentComplete(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Finished!!!",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.height(12.dp))
        Image(
            modifier = Modifier.size(256.dp),
            imageVector = Icons.Default.Check,
            contentDescription = "Checkmark"
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Happy using QOORAAN App",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}