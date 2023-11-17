package com.example.quranisapp.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.quranisapp.data.kotpref.SettingPreferences

object GlobalState {
    var isDarkMode by mutableStateOf(SettingPreferences.isDarkMode)

    var isVisibleTranslate by mutableStateOf(SettingPreferences.isVisibleTranslate)

    var isSelectedQori by mutableStateOf(SettingPreferences.selectedQori)

    var isDateFormated by mutableLongStateOf(SettingPreferences.dateFormat)

    var isOnBoarding by mutableStateOf(SettingPreferences.isOnBoarding)
}