package de.turbocache3000.polizei.alexa.api

import de.turbocache3000.polizei.scraper.api.News

/**
 * Converts a [News] object to a list of [FlashBriefingEntry]s for the Alexa device.
 */
interface FlashBriefingConverter {
    /**
     * Converts a [News] object to a list of [FlashBriefingEntry]s for the Alexa device.
     */
    fun convert(news: News): List<FlashBriefingEntry>
}