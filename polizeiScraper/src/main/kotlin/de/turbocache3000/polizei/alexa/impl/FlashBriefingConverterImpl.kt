package de.turbocache3000.polizei.alexa.impl

import de.turbocache3000.polizei.alexa.api.FlashBriefingConverter
import de.turbocache3000.polizei.alexa.api.FlashBriefingEntry
import de.turbocache3000.polizei.scraper.api.News
import java.time.ZoneOffset
import java.util.*

class FlashBriefingConverterImpl : FlashBriefingConverter {
    override fun convert(news: News): List<FlashBriefingEntry> {
        return news.entries.mapIndexed { index, entry ->
            FlashBriefingEntry(
                    entry.id,
                    news.date.atTime(0, 0, index).toInstant(ZoneOffset.UTC).toString(),
                    entry.title,
                    entry.body,
                    news.uri.toString()
            )
        }
    }
}