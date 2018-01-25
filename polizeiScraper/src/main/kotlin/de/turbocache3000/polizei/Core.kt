package de.turbocache3000.polizei

import de.turbocache3000.polizei.alexa.impl.FlashBriefingConverterImpl
import de.turbocache3000.polizei.config.Configuration
import de.turbocache3000.polizei.database.impl.DynamoDbDatabaseImpl
import de.turbocache3000.polizei.log.api.Logger
import de.turbocache3000.polizei.scraper.api.News
import de.turbocache3000.polizei.scraper.impl.NewsScraperImpl
import java.net.URI

class Core {

    /**
     * Updates the DynamoDB database entry to contain the most recent press announcements
     * on the official police website.
     *
     * All addenda are merged with the most recent full announcement. The resulting order
     * will reflect the order on the website: newest addendum to full announcement.
     */
    fun run(logger: Logger) {
        val newsScraper = NewsScraperImpl(URI.create(Configuration.websiteURI), logger)

        // Fetch the index, which contains the most recent press announcements sub-page locations
        val index = newsScraper.scrapeNewsIndex()

        // Get all addenda and stop after the most recent press announcement sub-pages
        val combinedNews = mutableListOf<News>()
        for (entry in index.entries) {
            combinedNews.add(newsScraper.scrapeNews(entry))
            if (!entry.addendum) {
                break
            }
        }

        // Write the result to the database
        val flashBriefingConverter = FlashBriefingConverterImpl(logger)
        val database = DynamoDbDatabaseImpl(logger, Configuration.region, Configuration.tableName, flashBriefingConverter)
        database.write(combinedNews)
    }
}