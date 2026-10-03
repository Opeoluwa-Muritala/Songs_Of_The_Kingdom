package com.example.songsofthekingdom

import com.example.songsofthekingdom.data.CatalogValidator
import com.example.songsofthekingdom.data.Hymn
import com.example.songsofthekingdom.data.HymnCatalog
import com.example.songsofthekingdom.data.HymnSection
import com.example.songsofthekingdom.data.RightsStatus
import com.example.songsofthekingdom.data.filterHymns
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogTest {
    private val publicDomainHymn = Hymn(
        id = "amazing-grace",
        number = 59,
        title = "Amazing Grace",
        author = "John Newton",
        sections = listOf(HymnSection("Verse 1", "Amazing grace")),
        rightsStatus = RightsStatus.PUBLIC_DOMAIN,
        sourceName = "Hymnary.org",
        sourceUrl = "https://hymnary.org/text/amazing_grace_how_sweet_the_sound",
        creditLine = "Public domain. Source: Hymnary.org.",
        verifiedOn = "2026-10-03",
    )

    @Test
    fun validPublicDomainLyricsAreAccepted() {
        assertTrue(CatalogValidator.validate(HymnCatalog(listOf(publicDomainHymn))).isValid)
    }

    @Test
    fun lyricsWithoutRightsEvidenceAreRejected() {
        val unsafe = publicDomainHymn.copy(
            rightsStatus = RightsStatus.METADATA_ONLY,
            sourceUrl = null,
            creditLine = null,
        )
        assertFalse(CatalogValidator.validate(HymnCatalog(listOf(unsafe))).isValid)
    }

    @Test
    fun nonHymnaryAndNonHttpsSourcesAreRejected() {
        assertFalse(CatalogValidator.isSafeSourceUrl("http://hymnary.org/text/example"))
        assertFalse(CatalogValidator.isSafeSourceUrl("https://example.com/text/example"))
        assertTrue(CatalogValidator.isSafeSourceUrl("https://www.hymnary.org/text/example"))
    }

    @Test
    fun searchMatchesTitleAuthorAndExactNumber() {
        val second = Hymn("abide-with-me", 109, "Abide with Me", author = "Henry Lyte")
        val songs = listOf(publicDomainHymn, second)
        assertEquals(listOf(publicDomainHymn), filterHymns(songs, "grace"))
        assertEquals(listOf(second), filterHymns(songs, "Lyte"))
        assertEquals(listOf(second), filterHymns(songs, "109"))
    }
}
