package de.turbocache3000.polizei.database.impl

import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import de.turbocache3000.polizei.alexa.api.FlashBriefingConverter
import de.turbocache3000.polizei.alexa.api.FlashBriefingEntry
import de.turbocache3000.polizei.database.api.Database
import de.turbocache3000.polizei.log.api.Logger
import de.turbocache3000.polizei.scraper.api.News

class DynamoDbDatabaseImpl(
        private val logger: Logger,
        region: Regions,
        private val tableName: String,
        private val flashBriefingConverter: FlashBriefingConverter
) : Database {
    private val mapper = ObjectMapper().registerModule(KotlinModule())
    private val client = AmazonDynamoDBClientBuilder.standard().withRegion(region).build()

    private val hardcodedId = "0"

    private object Columns {
        const val id = "id"
        const val content = "content"
    }

    override fun write(news: List<News>) {
        val entities = news.flatMap { newsEntry -> flashBriefingConverter.convert(newsEntry) }
        val json = mapper.writeValueAsString(entities)

        logger.debug("Writing JSON to database: {}", json)
        client.putItem(tableName, mapOf(
                Columns.id to AttributeValue().withN(hardcodedId),
                Columns.content to AttributeValue().withS(json)
        ))
        logger.debug("Wrote JSON to database")
    }
}