package de.turbocache3000.polizei.lambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent
import de.turbocache3000.polizei.Core
import de.turbocache3000.polizei.alexa.impl.FlashBriefingConverterImpl
import de.turbocache3000.polizei.config.Configuration
import de.turbocache3000.polizei.database.impl.DynamoDbDatabaseImpl
import de.turbocache3000.polizei.log.impl.ConsoleLogger
import de.turbocache3000.polizei.log.impl.LamdbaLoggerImpl
import de.turbocache3000.polizei.scraper.api.News
import de.turbocache3000.polizei.scraper.impl.NewsScraperImpl
import de.turbocache3000.polizei.util.stacktraceAsString
import java.net.URI

/**
 * Entry point for usage as AWS Lambda service
 */
class LambdaHandler {

    @Suppress("UNUSED_PARAMETER", "unused") // entry point for activation external call
    fun eventHandler(event: ScheduledEvent, context: Context) {
        val logger = LamdbaLoggerImpl(context.logger)

        try {
            Core.run(logger)
        } catch (e: Exception) {
            logger.error("Exception occurred: {}", e.stacktraceAsString())
        }
    }
}