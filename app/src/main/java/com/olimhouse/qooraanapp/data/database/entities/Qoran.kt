package com.olimhouse.qooraanapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "quran")
data class Qoran(
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    @ColumnInfo(name = "jozz") val juzNumber : Int? = 0,
    @ColumnInfo(name = "sora") val surahNumber : Int? = 0,
    @ColumnInfo(name = "sora_name_en") val surahNameEn : String? = "",
    @ColumnInfo(name = "sora_name_ar") val surahNameAr : String? = "",
    @ColumnInfo(name = "page") val page : Int? = 0,
    @ColumnInfo(name = "aya_no") var ayatNumber : Int? = 0,
    @ColumnInfo(name = "aya_text") val ayatText : String? = "",
    @ColumnInfo(name = "aya_text_emlaey") val ayatTextEmlaey: String? = "",
    @ColumnInfo(name = "translation_id") val tranlateId : String? = "",
    @ColumnInfo(name = "footnotes_id") val footnotesId : String? = "",
    @ColumnInfo(name = "sora_name_id") val surahNameId: String? = "",
    @ColumnInfo(name = "sora_descend_place") val surahDescendPlace  :String? = "",
    @ColumnInfo(name = "sora_name_emlaey") val surahNameEmlaey : String? = "",
    @ColumnInfo(name = "translation_en") val tranlateEn: String? = "",
    @ColumnInfo(name = "footnotes_en") val footnotesEn: String? = ""
)
