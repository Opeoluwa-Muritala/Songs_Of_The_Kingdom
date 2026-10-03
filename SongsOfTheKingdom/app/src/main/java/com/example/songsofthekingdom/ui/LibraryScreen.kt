package com.example.songsofthekingdom.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.songsofthekingdom.AppUiState
import com.example.songsofthekingdom.data.Hymn

@Composable
fun LibraryScreen(state: AppUiState, onSearch: (String) -> Unit, onSongClick: (Hymn) -> Unit) {
    when {
        state.loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        state.errorMessage != null -> MessageState("The library is unavailable", state.errorMessage)
        else -> LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 24.dp)) {
            item { LibraryHero() }
            val recentHymns = state.preferences.recentIds.mapNotNull { id -> state.hymns.firstOrNull { it.id == id } }
            if (recentHymns.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            "RECENTLY OPENED",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 24.dp),
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            items(recentHymns, key = Hymn::id) { hymn ->
                                Card(
                                    modifier = Modifier.size(width = 176.dp, height = 96.dp).clickable { onSongClick(hymn) },
                                    shape = RoundedCornerShape(20.dp),
                                ) {
                                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                        Text("Hymn " + hymn.number, style = MaterialTheme.typography.labelMedium)
                                        Text(hymn.title, style = MaterialTheme.typography.titleSmall, maxLines = 2)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item {
                OutlinedTextField(
                    value = state.query,
                    onValueChange = onSearch,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                    placeholder = { Text("Title, author, or hymn number") },
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                )
            }
            if (state.visibleHymns.isEmpty()) {
                item { MessageState("No hymns found", "Try a shorter title or an exact hymn number.") }
            } else {
                item {
                    Text(
                        text = state.visibleHymns.size.toString() + " hymns · " +
                            state.visibleHymns.count(Hymn::hasLyrics) + " with lyrics",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    )
                }
                items(state.visibleHymns, key = Hymn::id) { hymn ->
                    HymnRow(hymn, hymn.id in state.preferences.favoriteIds) { onSongClick(hymn) }
                }
            }
        }
    }
}

@Composable
private fun LibraryHero() {
    val accent = MaterialTheme.colorScheme.primary.copy(alpha = .2f)
    Box(Modifier.fillMaxWidth().height(196.dp)) {
        Canvas(Modifier.fillMaxSize()) {
            val step = size.width / 6
            repeat(7) { index ->
                drawCircle(accent, radius = 70f + index * 9f, center = Offset(index * step, size.height * .2f))
            }
        }
        Column(
            modifier = Modifier.align(Alignment.BottomStart).padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("SONGS OF THE KINGDOM", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            Text("Find a hymn\nfor this moment", style = MaterialTheme.typography.displaySmall)
        }
    }
}

@Composable
fun HymnRow(hymn: Hymn, favorite: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 5.dp).clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .55f)),
        shape = RoundedCornerShape(20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Surface(shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                Box(Modifier.size(52.dp), contentAlignment = Alignment.Center) {
                    Text(hymn.number.toString(), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(hymn.title, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(
                    (if (hymn.hasLyrics) "Lyrics available · " else "Catalog entry · ") + hymn.language,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (favorite) Text("♥", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun FavoritesScreen(hymns: List<Hymn>, onSongClick: (Hymn) -> Unit) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
        item { Text("Favorites", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(vertical = 24.dp)) }
        if (hymns.isEmpty()) {
            item { MessageState("A quiet shelf, for now", "Open any hymn and tap the heart to keep it close.") }
        } else {
            items(hymns, key = Hymn::id) { hymn -> HymnRow(hymn, true) { onSongClick(hymn) } }
        }
    }
}

@Composable
private fun MessageState(title: String, message: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(Icons.Outlined.Book, null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
        Text(title, style = MaterialTheme.typography.titleLarge)
        Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
