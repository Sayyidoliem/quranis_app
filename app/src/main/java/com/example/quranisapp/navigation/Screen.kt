package com.example.quranisapp.navigation

sealed class Screen(val route : String){
    object Home : Screen("home")
    object Read : Screen("index")
    object Time: Screen("time")
    object Detail: Screen("read?&surahNumber={surahNumber}&juzNumber={juzNumber}&pageNumber={pageNumber}") {
        fun createRoute(
            surahNumber : Int?,
            juzNumber: Int?,
            pageNumber: Int?
        ): String{
            return "read?&surahNumber=${surahNumber}&juzNumber=${juzNumber}&pageNumber=${pageNumber}"
        }
    }
    object Bookmark: Screen("bookmark")
    object Setting: Screen("setting")
    object Discover: Screen("discover")
}
