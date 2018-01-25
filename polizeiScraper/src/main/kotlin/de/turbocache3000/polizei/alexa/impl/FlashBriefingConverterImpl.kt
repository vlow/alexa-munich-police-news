package de.turbocache3000.polizei.alexa.impl

import de.turbocache3000.polizei.alexa.api.FlashBriefingConverter
import de.turbocache3000.polizei.alexa.api.FlashBriefingEntry
import de.turbocache3000.polizei.log.api.Logger
import de.turbocache3000.polizei.scraper.api.News
import java.time.LocalDate
import java.time.ZoneOffset

class FlashBriefingConverterImpl(private val logger: Logger) : FlashBriefingConverter {

    companion object {
        const val SECONDS_PER_DAY: Int = 86400
    }

    /**
     * Extracts the entry title from the caption.
     */
    private val titleExtractor = """[0-9]*. (.*)$""".toRegex()

    override fun convert(news: News): List<FlashBriefingEntry> {
        return news.entries.mapIndexed { index, entry ->
            FlashBriefingEntry(
                    entry.id,
                    generateTimestamp(news.date, index),
                    entry.title,
                    createBodyWithTitle(entry.title, entry.body),
                    news.uri.toString()
            )
        }
    }

    /**
     * Adds the text part of the given [entryTitle] to the [body] and returns it as a new string.
     */
    private fun createBodyWithTitle(entryTitle: String, body: String): String {
        logger.debug("Extracting the caption from the entryTitle '{}'", entryTitle)
        val caption = titleExtractor.matchEntire(entryTitle)?.groups?.get(1)?.value ?: return body

        logger.debug("Extracted caption is '{}'", caption)
        return "$caption: $body"
    }

    /**
     * Generates an artificial entry timestamp, based on the index of the entry.
     * As this increases the timestamp by a second for each entry, the newsIndex
     * must not be larger than 86400.
     *
     * The Flash-Briefing-Skill API requires a different timestamp for every news item.
     * The documentation states that this timestamp determines the order of messages
     * (newest to oldest) and will only take the latest 5 messages into account.
     *
     * Alas, this seems only to be the case for the date. A duplicate time results in
     * an error. Apart from that restriction, the time seems to be ignored.
     */
    private fun generateTimestamp(newsDate: LocalDate, newsIndex: Int): String {
        if (newsIndex > SECONDS_PER_DAY) throw IllegalArgumentException("Invalid amount of messages (> $SECONDS_PER_DAY).")
        return newsDate.atTime(0, 0, 0)
                .plusSeconds(newsIndex.toLong())
                .toInstant(ZoneOffset.UTC)
                .toString()
    }
}