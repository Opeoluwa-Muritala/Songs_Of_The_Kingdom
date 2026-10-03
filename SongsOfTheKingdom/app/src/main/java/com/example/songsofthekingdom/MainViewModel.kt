package com.example.songsofthekingdom

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.songsofthekingdom.data.Hymn
import com.example.songsofthekingdom.data.HymnRepository
import com.example.songsofthekingdom.data.PreferencesRepository
import com.example.songsofthekingdom.data.ThemePreference
import com.example.songsofthekingdom.data.UserPreferences
import com.example.songsofthekingdom.data.filterHymns
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AppUiState(
    val loading: Boolean = true,
    val hymns: List<Hymn> = emptyList(),
    val visibleHymns: List<Hymn> = emptyList(),
    val query: String = "",
    val preferences: UserPreferences = UserPreferences(),
    val errorMessage: String? = null,
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val hymnRepository = HymnRepository(application)
    private val preferencesRepository = PreferencesRepository(application)
    private val catalog = MutableStateFlow<List<Hymn>>(emptyList())
    private val query = MutableStateFlow("")
    private val loading = MutableStateFlow(true)
    private val error = MutableStateFlow<String?>(null)

    val uiState = combine(
        catalog,
        query,
        preferencesRepository.preferences,
        loading,
        error,
    ) { hymns, search, preferences, isLoading, message ->
        AppUiState(
            loading = isLoading,
            hymns = hymns,
            visibleHymns = filterHymns(hymns, search),
            query = search,
            preferences = preferences,
            errorMessage = message,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppUiState())

    init {
        viewModelScope.launch {
            hymnRepository.loadCatalog()
                .onSuccess { catalog.value = it }
                .onFailure { error.value = "The hymn catalog could not be loaded." }
            loading.value = false
        }
    }

    fun search(value: String) {
        query.value = value.take(80)
    }

    fun toggleFavorite(id: String) = viewModelScope.launch { preferencesRepository.toggleFavorite(id) }
    fun recordOpened(id: String) = viewModelScope.launch { preferencesRepository.recordRecent(id) }
    fun setTheme(value: ThemePreference) = viewModelScope.launch { preferencesRepository.setTheme(value) }
    fun setTextScale(value: Float) = viewModelScope.launch { preferencesRepository.setTextScale(value) }
    fun setReduceMotion(value: Boolean) = viewModelScope.launch { preferencesRepository.setReduceMotion(value) }
    fun setDynamicColor(value: Boolean) = viewModelScope.launch { preferencesRepository.setDynamicColor(value) }
}
