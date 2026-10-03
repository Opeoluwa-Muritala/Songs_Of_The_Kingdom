package com.example.songsofthekingdom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.songsofthekingdom.ui.SongsApp
import com.example.songsofthekingdom.ui.theme.SongsOfTheKingdomTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            SongsOfTheKingdomTheme(
                preference = state.preferences.theme,
                dynamicColor = state.preferences.dynamicColor,
            ) {
                SongsApp(
                    state = state,
                    onSearch = viewModel::search,
                    onToggleFavorite = viewModel::toggleFavorite,
                    onSongOpened = viewModel::recordOpened,
                    onThemeChanged = viewModel::setTheme,
                    onTextScaleChanged = viewModel::setTextScale,
                    onReduceMotionChanged = viewModel::setReduceMotion,
                    onDynamicColorChanged = viewModel::setDynamicColor,
                )
            }
        }
    }
}
