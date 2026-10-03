package com.example.songsofthekingdom.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("reader_preferences")

enum class ThemePreference { SYSTEM, LIGHT, DARK }

data class UserPreferences(
    val favoriteIds: Set<String> = emptySet(),
    val recentIds: List<String> = emptyList(),
    val theme: ThemePreference = ThemePreference.SYSTEM,
    val textScale: Float = 1f,
    val reduceMotion: Boolean = false,
    val dynamicColor: Boolean = false,
)

class PreferencesRepository(private val context: Context) {
    val preferences: Flow<UserPreferences> = context.dataStore.data.map(::toPreferences)

    suspend fun toggleFavorite(id: String) = context.dataStore.edit { values ->
        val current = decodeList(values[FAVORITES]).toMutableSet()
        if (!current.add(id)) current.remove(id)
        values[FAVORITES] = current.sorted().joinToString(SEPARATOR)
    }

    suspend fun recordRecent(id: String) = context.dataStore.edit { values ->
        values[RECENTS] = (listOf(id) + decodeList(values[RECENTS])).distinct().take(8).joinToString(SEPARATOR)
    }

    suspend fun setTheme(theme: ThemePreference) = context.dataStore.edit { it[THEME] = theme.name }
    suspend fun setTextScale(scale: Float) = context.dataStore.edit { it[TEXT_SCALE] = scale.coerceIn(.85f, 1.45f) }
    suspend fun setReduceMotion(enabled: Boolean) = context.dataStore.edit { it[REDUCE_MOTION] = enabled }
    suspend fun setDynamicColor(enabled: Boolean) = context.dataStore.edit { it[DYNAMIC_COLOR] = enabled }

    private fun toPreferences(values: Preferences) = UserPreferences(
        favoriteIds = decodeList(values[FAVORITES]).toSet(),
        recentIds = decodeList(values[RECENTS]),
        theme = runCatching { ThemePreference.valueOf(values[THEME].orEmpty()) }.getOrDefault(ThemePreference.SYSTEM),
        textScale = (values[TEXT_SCALE] ?: 1f).coerceIn(.85f, 1.45f),
        reduceMotion = values[REDUCE_MOTION] ?: false,
        dynamicColor = values[DYNAMIC_COLOR] ?: false,
    )

    private fun decodeList(value: String?): List<String> = value.orEmpty().split(SEPARATOR).filter { it.isNotBlank() }

    private companion object {
        const val SEPARATOR = "|"
        val FAVORITES = stringPreferencesKey("favorite_ids")
        val RECENTS = stringPreferencesKey("recent_ids")
        val THEME = stringPreferencesKey("theme")
        val TEXT_SCALE = floatPreferencesKey("text_scale")
        val REDUCE_MOTION = booleanPreferencesKey("reduce_motion")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
    }
}
