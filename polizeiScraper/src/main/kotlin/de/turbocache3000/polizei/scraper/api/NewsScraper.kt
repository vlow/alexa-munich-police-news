package de.turbocache3000.polizei.scraper.api

import java.net.URI
import java.time.LocalDate

/**
 * Scrapes the Polizei website for news.
 */
interface NewsScraper {
    /**
     * Creates a news index.
     */
    fun scrapeNewsIndex(): NewsIndex

    /**
     * Scrapes the news entries from the given index entry.
     */
    fun scrapeNews(entry: NewsIndexEntry): News
}

/**
 * News index. Consists of index [entries].
 */
data class NewsIndex(
        val entries: List<NewsIndexEntry>
)

/**
 * News index entry. Consists of a [title], a [uri] to the full news, and the [addendum] flag.
 * The [addendum] flag indicates, whether the index entry is an addendum to a previous news index.
 */
data class NewsIndexEntry(
        val title: String,
        val uri: URI,
        val addendum: Boolean
)

/**
 * News. Consists of a [uri], a [title], a [date] and news [entries].
 */
data class News(
        val uri: URI,
        val title: String,
        val date: LocalDate,
        val entries: List<NewsEntry>
)

/**
 * A news entry. Consists of a [title] and a [body].
 */
data class NewsEntry(
        val title: String,
        val body: String
)