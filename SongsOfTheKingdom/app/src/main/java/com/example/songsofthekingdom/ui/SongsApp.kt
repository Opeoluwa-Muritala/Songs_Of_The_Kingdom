package com.example.songsofthekingdom.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.songsofthekingdom.AppUiState
import com.example.songsofthekingdom.data.Hymn
import com.example.songsofthekingdom.data.ThemePreference

private enum class AppSection(val label: String, val icon: ImageVector) {
    LIBRARY("Library", Icons.Outlined.LibraryMusic),
    FAVORITES("Favorites", Icons.Outlined.FavoriteBorder),
    SETTINGS("Settings", Icons.Outlined.Settings),
}

@Composable
fun SongsApp(
    state: AppUiState,
    onSearch: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onSongOpened: (String) -> Unit,
    onThemeChanged: (ThemePreference) -> Unit,
    onTextScaleChanged: (Float) -> Unit,
    onReduceMotionChanged: (Boolean) -> Unit,
    onDynamicColorChanged: (Boolean) -> Unit,
) {
    var sectionName by rememberSaveable { mutableStateOf(AppSection.LIBRARY.name) }
    var selectedId by rememberSaveable { mutableStateOf<String?>(null) }
    val selectedSong = state.hymns.firstOrNull { it.id == selectedId }
    val section = AppSection.valueOf(sectionName)
    val useRail = androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp >= 700

    BackHandler(enabled = selectedSong != null) { selectedId = null }

    val content: @Composable () -> Unit = {
        val screen: @Composable () -> Unit = {
            when {
                selectedSong != null -> SongDetailScreen(
                    hymn = selectedSong,
                    isFavorite = selectedSong.id in state.preferences.favoriteIds,
                    textScale = state.preferences.textScale,
                    onBack = { selectedId = null },
                    onToggleFavorite = { onToggleFavorite(selectedSong.id) },
                )
                section == AppSection.LIBRARY -> LibraryScreen(
                    state = state,
                    onSearch = onSearch,
                    onSongClick = {
                        selectedId = it.id
                        onSongOpened(it.id)
                    },
                )
                section == AppSection.FAVORITES -> FavoritesScreen(
                    hymns = state.hymns.filter { it.id in state.preferences.favoriteIds },
                    onSongClick = {
                        selectedId = it.id
                        onSongOpened(it.id)
                    },
                )
                else -> SettingsScreen(
                    preferences = state.preferences,
                    onThemeChanged = onThemeChanged,
                    onTextScaleChanged = onTextScaleChanged,
                    onReduceMotionChanged = onReduceMotionChanged,
                    onDynamicColorChanged = onDynamicColorChanged,
                )
            }
        }
        val destinationKey = section.name + ":" + selectedId
        if (state.preferences.reduceMotion) {
            Crossfade(targetState = destinationKey, label = "screen") { destination ->
                key(destination) { screen() }
            }
        } else {
            AnimatedContent(
                targetState = destinationKey,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen",
            ) { destination ->
                key(destination) { screen() }
            }
        }
    }

    if (selectedSong != null) {
        content()
    } else if (useRail) {
        Row(Modifier.fillMaxSize()) {
            NavigationRail {
                AppSection.entries.forEach { item ->
                    NavigationRailItem(
                        selected = section == item,
                        onClick = { sectionName = item.name },
                        icon = { Icon(item.icon, null) },
                        label = { Text(item.label) },
                    )
                }
            }
            Box(Modifier.weight(1f)) { content() }
        }
    } else {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                NavigationBar {
                    AppSection.entries.forEach { item ->
                        NavigationBarItem(
                            selected = section == item,
                            onClick = { sectionName = item.name },
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(item.label) },
                        )
                    }
                }
            },
        ) { padding -> Box(Modifier.padding(padding)) { content() } }
    }
}
