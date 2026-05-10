package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import com.example.app.ui.theme.*
import com.example.app.data.PoemRepository

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val poems = PoemRepository.loadPoems(this)
        val poets = PoemRepository.loadPoets(this)
        val specialPoems = PoemRepository.loadSpecialPoems(this)

        setContent {
            var darkTheme by remember { mutableStateOf(false) }
            var language by remember { mutableStateOf("kn") } // "kn" or "en"

            AppTheme(darkTheme = darkTheme) {
                PoemApp(
                    poems = poems,
                    poets = poets,
                    specialPoems = specialPoems,
                    context = this,
                    language = language,
                    darkTheme = darkTheme,
                    onLanguageChange = { language = it },
                    onThemeToggle = { darkTheme = !darkTheme }
                )
            }
        }
    }
}
