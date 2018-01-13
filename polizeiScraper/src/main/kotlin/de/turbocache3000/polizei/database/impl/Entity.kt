package de.turbocache3000.polizei.database.impl

/**
 * See https://developer.amazon.com/docs/flashbriefing/flash-briefing-skill-api-feed-reference.html#json-text-multi-item-example
 */
data class Entity(
        val uid: String,
        val updateDate: String,
        val titleText: String,
        val mainText: String,
        val redirectionUrl: String
)