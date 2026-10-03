package com.example.songsofthekingdom.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class HymnRepository(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = false }

    suspend fun loadCatalog(): Result<List<Hymn>> = withContext(Dispatchers.IO) {
        runCatching {
            val raw = context.assets.open(CATALOG_FILE).bufferedReader().use { it.readText() }
            require(raw.length <= MAX_CATALOG_BYTES) { "Catalog exceeds the size limit" }
            val catalog = json.decodeFromString<HymnCatalog>(raw)
            val validation = CatalogValidator.validate(catalog)
            require(validation.isValid) { validation.errors.joinToString() }
            catalog.hymns.sortedBy { it.title }
        }
    }

    private companion object {
        const val CATALOG_FILE = "hymns/catalog.json"
        const val MAX_CATALOG_BYTES = 2_000_000
    }
}
