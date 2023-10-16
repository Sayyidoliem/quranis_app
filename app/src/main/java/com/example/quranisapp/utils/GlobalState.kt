package com.example.quranisapp.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.quranisapp.data.kotpref.SettingPreferences

object GlobalState {
    var isDarkMode by mutableStateOf(SettingPreferences.isDarkMode)

    var isVisibleTranslate by mutableIntStateOf(SettingPreferences.isSelectedLanguage)

    var isSelectedQori by mutableStateOf(SettingPreferences.selectedQori)
}