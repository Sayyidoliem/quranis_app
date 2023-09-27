package com.example.quranisapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "surahBookmark")
data class SurahBookmark(
    val surahNameEn : String? = "",
    val surahNameAr : String? = "",
    val totalAyah : Int? = 0,
    val juzNumber : Int? = 0,
    val surahDescend : String? = "",
    val surahNumber : Int? = 0,
    @PrimaryKey(autoGenerate = true)val id: Int? = null,
)
