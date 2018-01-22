package de.turbocache3000.polizei

import de.turbocache3000.polizei.alexa.impl.FlashBriefingConverterImpl
import de.turbocache3000.polizei.config.Configuration
import de.turbocache3000.polizei.database.impl.DynamoDbDatabaseImpl
import de.turbocache3000.polizei.log.impl.ConsoleLogger
import de.turbocache3000.polizei.scraper.impl.NewsScraperImpl
import java.net.URI

fun main(args: Array<String>) {
    val newsScraper = NewsScraperImpl(URI.create(Configuration.websiteURI), ConsoleLogger)

    val index = newsScraper.scrapeNewsIndex()

    val first = index.entries.firstOrNull()
    if (first != null) {
        val news = newsScraper.scrapeNews(first)
        val flashBriefingConverter = FlashBriefingConverterImpl()
        val database = DynamoDbDatabaseImpl(ConsoleLogger, Configuration.region, Configuration.tableName, flashBriefingConverter)
        database.write(news)
    }
}