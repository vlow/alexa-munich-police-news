package de.turbocache3000.polizei.config

import com.amazonaws.regions.Regions

/**
 * Global configuration.
 */
object Configuration {
    /**
     * URI of the Polizei website which contains the news.
     */
    val websiteURI = "https://www.polizei.bayern.de/muenchen/news/presse/aktuell/"

    /**
     * Name of the table in the DynamoDb.
     */
    val tableName = "polizeiPressemeldungen"

    /**
     * AWS region for the DynamoDb.
     */
    val region = Regions.EU_WEST_1
}