package com.example.songsofthekingdom.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.songsofthekingdom.data.ThemePreference
import com.example.songsofthekingdom.data.UserPreferences

@Composable
fun SettingsScreen(
    preferences: UserPreferences,
    onThemeChanged: (ThemePreference) -> Unit,
    onTextScaleChanged: (Float) -> Unit,
    onReduceMotionChanged: (Boolean) -> Unit,
    onDynamicColorChanged: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        SettingGroup("Appearance") {
            Text("Theme", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ThemePreference.entries.forEach { theme ->
                    FilterChip(
                        selected = preferences.theme == theme,
                        onClick = { onThemeChanged(theme) },
                        label = { Text(theme.name.lowercase().replaceFirstChar(Char::uppercase)) },
                    )
                }
            }
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Device colors", style = MaterialTheme.typography.titleMedium)
                    Text("Use the Android wallpaper palette when available.", style = MaterialTheme.typography.bodySmall)
                }
                Switch(checked = preferences.dynamicColor, onCheckedChange = onDynamicColorChanged)
            }
        }
        HorizontalDivider()
        SettingGroup("Reading") {
            Text("Text size", style = MaterialTheme.typography.titleMedium)
            Text("Preview at " + (preferences.textScale * 100).toInt() + "%", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Slider(
                value = preferences.textScale,
                onValueChange = onTextScaleChanged,
                valueRange = .85f..1.45f,
                steps = 5,
            )
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Reduce motion", style = MaterialTheme.typography.titleMedium)
                    Text("Uses simple fades instead of spatial transitions.", style = MaterialTheme.typography.bodySmall)
                }
                Switch(checked = preferences.reduceMotion, onCheckedChange = onReduceMotionChanged)
            }
        }
        HorizontalDivider()
        SettingGroup("About this library") {
            Text(
                "Songs are available offline. Full lyrics appear only when the exact version is verified as public domain or separately licensed.",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                "Hymnary.org is credited per song as the verification source. No songs are fetched or scraped while you use the app.",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun SettingGroup(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(title.uppercase(), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        content()
    }
}
