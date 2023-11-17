package com.example.quranisapp.data.database

import androidx.room.Dao
import androidx.room.Query
import com.example.quranisapp.data.database.entities.Qoran
import kotlinx.coroutines.flow.Flow

@Dao
interface QoranDao {

    @Query("SELECT * FROM quran")
    fun getAllQoranAyah(): Flow<List<Qoran>>

    @Query("SELECT * FROM quran WHERE sora = :indexSora")
    fun getAyatSurah(indexSora : Int):Flow<List<Qoran>>

    @Query("SELECT * FROM quran WHERE jozz = :indexJozz")
    fun getAyatJozz(indexJozz : Int):Flow<List<Qoran>>

    @Query("SELECT * FROM quran WHERE page = :indexPage")
    fun getAyatPage(indexPage : Int): Flow<List<Qoran>>

    @Query("SELECT * FROM surah")
    fun getSurah(): Flow<List<Surah>>

    @Query("SELECT * FROM Juz")
    fun getJuz(): Flow<List<Juz>>

    @Query("SELECT * FROM Page")
    fun getPage(): Flow<List<Page>>

    @Query("SELECT * FROM surahjuz")
    fun getSurahjuz(): Flow<List<SurahJuz>>

    @Query("SELECT * FROM SurahSearch where sora_name_emlaey like '%'|| :soraNameEmlay || '%' OR aya_text_emlaey like '%'|| :soraNameEmlay || '%'")
    fun getSurahBySearch(soraNameEmlay:String):Flow<List<SurahSearch>>
    @Query("SELECT * FROM AyatSearch where aya_text_emlaey like '%'|| :ayatNameEmlay || '%' OR translation_en like '%'|| :ayatNameEmlay || '%' OR aya_text like '%'|| :ayatNameEmlay || '%'")
    fun getAyatBySearch(ayatNameEmlay:String):Flow<List<AyatSearch>>
}