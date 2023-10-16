package com.example.quranisapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.QURANISAppTheme
import com.example.quranisapp.splashscreens.GetStarted1
import com.example.quranisapp.splashscreens.GetStarted2
import com.example.quranisapp.splashscreens.GetStarted3
import com.example.quranisapp.splashscreens.SignScreens

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QURANISAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController: NavHostController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable("start1") {
                            GetStarted1(
                                goTo2 = { navController.navigate("start2") },
                                goTo3 = { navController.navigate("start3") }
                            )
                        }
                        composable("start2") {
                            GetStarted2(goTo3 = { navController.navigate("start3") })
                        }
                        composable("start3") {
                            GetStarted3(goToSign = { navController.navigate("sign") })
                        }
                        composable("sign"){
                            SignScreens(goTo4 = {navController.navigate("home")})
                        }
                        composable("home"){
                            MainActivity()
                        }
                    }
                }
            }
        }
    }
}