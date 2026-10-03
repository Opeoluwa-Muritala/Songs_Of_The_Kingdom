package com.example.songsofthekingdom.data

import kotlinx.serialization.Serializable

@Serializable
data class HymnCatalog(val hymns: List<Hymn>)

@Serializable
data class Hymn(
    val id: String,
    val number: Int,
    val title: String,
    val alternateTitles: List<String> = emptyList(),
    val language: String = "English",
    val sections: List<HymnSection> = emptyList(),
    val author: String? = null,
    val translator: String? = null,
    val publicationYear: Int? = null,
    val rightsStatus: RightsStatus = RightsStatus.METADATA_ONLY,
    val sourceName: String? = null,
    val sourceUrl: String? = null,
    val creditLine: String? = null,
    val verifiedOn: String? = null,
) {
    val hasLyrics: Boolean get() = sections.isNotEmpty() && rightsStatus.canDisplayLyrics
}

@Serializable
data class HymnSection(
    val label: String,
    val text: String,
)

@Serializable
enum class RightsStatus(val canDisplayLyrics: Boolean) {
    PUBLIC_DOMAIN(true),
    LICENSED(true),
    METADATA_ONLY(false),
}

data class CatalogValidationResult(val errors: List<String>) {
    val isValid: Boolean get() = errors.isEmpty()
}

object CatalogValidator {
    private val safeId = Regex("[a-z0-9]+(?:-[a-z0-9]+)*")

    fun validate(catalog: HymnCatalog): CatalogValidationResult {
        val errors = mutableListOf<String>()
        val duplicateIds = catalog.hymns.groupingBy { it.id }.eachCount().filterValues { it > 1 }.keys
        val duplicateNumbers = catalog.hymns.groupingBy { it.number }.eachCount().filterValues { it > 1 }.keys
        if (duplicateIds.isNotEmpty()) errors += "Duplicate ids: ${duplicateIds.sorted()}"
        if (duplicateNumbers.isNotEmpty()) errors += "Duplicate numbers: ${duplicateNumbers.sorted()}"

        catalog.hymns.forEach { hymn ->
            val prefix = "${hymn.id}:"
            if (!safeId.matches(hymn.id)) errors += "$prefix invalid id"
            if (hymn.number !in 1..9999) errors += "$prefix invalid number"
            if (hymn.title.isBlank() || hymn.title.length > 160) errors += "$prefix invalid title"
            if (hymn.sections.any { it.label.isBlank() || it.text.isBlank() }) errors += "$prefix empty lyric section"

            if (hymn.sections.isNotEmpty()) {
                if (!hymn.rightsStatus.canDisplayLyrics) errors += "$prefix lyrics are not permitted"
                if (hymn.author.isNullOrBlank()) errors += "$prefix missing author"
                if (hymn.sourceName.isNullOrBlank()) errors += "$prefix missing source name"
                if (!isSafeSourceUrl(hymn.sourceUrl)) errors += "$prefix invalid source URL"
                if (hymn.creditLine.isNullOrBlank()) errors += "$prefix missing credit line"
                if (!hymn.verifiedOn.orEmpty().matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
                    errors += "$prefix invalid verification date"
                }
            }
        }
        return CatalogValidationResult(errors)
    }

    fun isSafeSourceUrl(value: String?): Boolean = value?.let {
        runCatching {
            val uri = java.net.URI(it)
            uri.scheme == "https" &&
                (uri.host == "hymnary.org" || uri.host == "www.hymnary.org") &&
                uri.userInfo == null
        }.getOrDefault(false)
    } ?: false
}

fun filterHymns(hymns: List<Hymn>, query: String): List<Hymn> {
    val needle = query.trim()
    if (needle.isEmpty()) return hymns
    return hymns.filter { hymn ->
        hymn.number.toString() == needle ||
            hymn.title.contains(needle, ignoreCase = true) ||
            hymn.author.orEmpty().contains(needle, ignoreCase = true) ||
            hymn.alternateTitles.any { it.contains(needle, ignoreCase = true) }
    }
}
