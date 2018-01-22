package de.turbocache3000.polizei.lambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent
import de.turbocache3000.polizei.alexa.impl.FlashBriefingConverterImpl
import de.turbocache3000.polizei.config.Configuration
import de.turbocache3000.polizei.database.impl.DynamoDbDatabaseImpl
import de.turbocache3000.polizei.log.impl.LamdbaLoggerImpl
import de.turbocache3000.polizei.scraper.impl.NewsScraperImpl
import de.turbocache3000.polizei.util.stacktraceAsString
import java.net.URI

class LambdaHandler {
    fun eventHandler(event: ScheduledEvent, context: Context) {
        val logger = LamdbaLoggerImpl(context.logger)

        try {
            val scraper = NewsScraperImpl(URI.create(Configuration.websiteURI), logger)
            val index = scraper.scrapeNewsIndex()
            val first = index.entries.firstOrNull()

            if (first != null) {
                val news = scraper.scrapeNews(first)
                val flashBriefingConverter = FlashBriefingConverterImpl()
                val database = DynamoDbDatabaseImpl(logger, Configuration.region, Configuration.tableName, flashBriefingConverter)

                database.write(news)
            }
        } catch (e: Exception) {
            logger.error("Exception occurred: {}", e.stacktraceAsString())
        }
    }
}