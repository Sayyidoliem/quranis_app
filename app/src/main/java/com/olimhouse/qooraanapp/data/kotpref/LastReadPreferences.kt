package com.olimhouse.qooraanapp.data.kotpref

import com.chibatching.kotpref.KotprefModel

object LastReadPreferences : KotprefModel() {

    var lastSurahNumber by intPref(-1)
    var lastSurahName by stringPref("")
    var lastAyaNo by intPref(-1)
    var lastDate by longPref(-1)
    var index by intPref(-1)
}