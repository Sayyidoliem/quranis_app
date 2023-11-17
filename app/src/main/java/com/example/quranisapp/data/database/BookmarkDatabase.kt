package com.example.quranisapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quranisapp.data.database.entities.Bookmark
import com.example.quranisapp.data.database.entities.SurahBookmark

@Database(
    entities = [Bookmark::class,SurahBookmark::class],
    version = 2
)
abstract class BookmarkDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        @Volatile
        private var INSTANCE: BookmarkDatabase? = null

        fun getInstance(context: Context): BookmarkDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }
        }

        private fun buildDatabase(context: Context): BookmarkDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                BookmarkDatabase::class.java,
                "bookmark.db"
            ).fallbackToDestructiveMigration().build()
        }
    }
}