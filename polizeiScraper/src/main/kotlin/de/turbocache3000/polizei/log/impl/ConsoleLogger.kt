package de.turbocache3000.polizei.log.impl

import de.turbocache3000.polizei.log.api.Logger
import java.time.ZonedDateTime

object ConsoleLogger : Logger {
    override fun log(message: String, vararg parameters: Any?) {
        println(ZonedDateTime.now().toString() + " " + StringFormatter.format(message, *parameters))
    }
}