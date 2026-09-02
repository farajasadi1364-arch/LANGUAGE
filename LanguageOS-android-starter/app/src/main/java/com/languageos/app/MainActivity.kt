package com.languageos.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.languageos.app.ui.navigation.LanguageOSNavGraph
import com.languageos.app.ui.theme.LanguageOSTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LanguageOSTheme {
                val navController = rememberNavController()
                LanguageOSNavGraph(navController = navController)
            }
        }
    }
}
