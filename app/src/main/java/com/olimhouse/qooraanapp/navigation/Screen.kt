package com.olimhouse.qooraanapp.navigation

sealed class Screen(val route : String){
    object OnBoarding : Screen("onboarding")
    object Read : Screen("index")
    object Detail: Screen("read?&surahNumber={surahNumber}&juzNumber={juzNumber}&pageNumber={pageNumber}&index={index}") {
        fun createRoute(
            surahNumber : Int?,
            juzNumber: Int?,
            pageNumber: Int?,
            index : Int?
        ): String{
            return "read?&surahNumber=${surahNumber}&juzNumber=${juzNumber}&pageNumber=${pageNumber}&index=${index}"
        }
    }
    object Setting: Screen("setting")
    object Discover: Screen("discover")
    object Info: Screen("info")
    object Feedback: Screen("feedback")
    object Qiblat : Screen("qiblat")
    object Prayer : Screen("prayer")
}
