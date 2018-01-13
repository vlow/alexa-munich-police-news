package de.turbocache3000.polizei.database.api

import de.turbocache3000.polizei.scraper.api.News

interface Database {
    fun write(news: News)
}