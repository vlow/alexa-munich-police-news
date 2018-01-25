package de.turbocache3000.polizei.scraper.impl

import de.turbocache3000.polizei.log.api.Logger
import de.turbocache3000.polizei.scraper.api.*
import net.jodah.failsafe.Failsafe
import net.jodah.failsafe.RetryPolicy
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Whitelist
import java.net.URI
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit


class NewsScraperImpl(
        private val baseURI: URI,
        private val logger: Logger
) : NewsScraper {

    /**
     * The index page title indicating an addendum.
     */
    private val addendumIndexTitle = "^[0-9]*.?[ ]*Nachtrag.*$".toRegex()

    /**
     * List of allowed titles from the index page.
     */
    private val allowedNewsTitles = listOf(
            "^Pressebericht.*$".toRegex(),
            addendumIndexTitle
    )

    /**
     * Extracts the date from a news title.
     */
    private val dateExtractor = """^.*?vom (\d\d.\d\d.\d\d\d\d)$""".toRegex()

    /**
     * Pattern to parse the date string in a [LocalDate].
     */
    private val datePattern = "dd.MM.yyyy"

    private object IndexSelectors {
        /**
         * News title on the index page.
         */
        val TITLE = "a.inhaltDetaillink"
        /**
         * Link to the news details page.
         */
        val LINK = "a.inhaltDetaillinkMehr"
    }

    private object NewsSelectors {
        /**
         * Title of a news details page.
         */
        val TITLE = "h1.inhaltUeberschriftFolgeseitenFettAbstand"
        /**
         * Title of a new entry.
         */
        val ENTRY_TITLE = "div.inhaltUeberschriftFolgeseiten2"
        /**
         * Body of a news entry.
         */
        val ENTRY_BODY = "div.inhaltText"
    }

    override fun scrapeNewsIndex(): NewsIndex {
        logger.debug("Fetching news index from {}", baseURI)
        val doc = fetchDocument(baseURI.toString())

        // Select tds which contains the news
        val elements = doc.select("td:has(${IndexSelectors.TITLE})")
        // Filter tds to only include the ones with a title matching the regexes
        val newsElements = elements.filter { e -> allowedNewsTitles.any { it.matches(clean(e.selectFirst(IndexSelectors.TITLE).text())) } }


        logger.debug("Index has {} news", newsElements.size)
        // Map from document to NewsIndex and NewsIndexEntry
        return NewsIndex(newsElements.map { e ->
            val title = clean(e.select(IndexSelectors.TITLE).text())
            val href = e.select(IndexSelectors.LINK).attr("href")

            NewsIndexEntry(title, baseURI.resolve(href), addendumIndexTitle.matches(title))
        })
    }

    private fun clean(html: String): String {
        return Jsoup.clean(html, Whitelist.none())
    }

    private fun fetchDocument(url: String): Document {
        val retryPolicy = RetryPolicy()
                .withDelay(1, TimeUnit.SECONDS)
                .withMaxRetries(3)

        return Failsafe.with<RetryPolicy>(retryPolicy).get(Callable<Document> {
            Jsoup.connect(url).get()
        })
    }

    override fun scrapeNews(entry: NewsIndexEntry): News {
        logger.debug("Scraping news from {}", entry.uri)
        val doc = fetchDocument(entry.uri.toString())

        val title = clean(doc.select(NewsSelectors.TITLE).text())
        logger.debug("Title: '{}'", title)
        val date = extractDate(title)

        val entryTitles = doc.select(NewsSelectors.ENTRY_TITLE).map { clean(it.text()) }
        val entryBodies = doc.select(NewsSelectors.ENTRY_BODY).map { clean(it.text()) }

        logger.debug("Found {} entry titles and {} entry bodies", entryTitles.size, entryBodies.size)

        // Zip the entry titles and the entry bodies together to create the news entry
        val news = entryTitles.zip(entryBodies).map {
            NewsEntry(it.first, it.second)
        }

        return News(entry.uri, title, date, news)
    }

    /**
     * Extracts a [LocalDate] from the given [title].
     */
    private fun extractDate(title: String): LocalDate {
        logger.debug("Extracting date from title '{}'", title)
        val dateString = dateExtractor.matchEntire(title)?.groups?.get(1)?.value ?: return LocalDate.now()

        logger.debug("Extracted date is '{}'", dateString)
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(datePattern))
    }
}