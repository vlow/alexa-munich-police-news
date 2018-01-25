package de.turbocache3000.polizei

import de.turbocache3000.polizei.log.impl.ConsoleLogger

/**
 * Application entry point for local debugging.
 */
fun main(args: Array<String>) {
    Core.run(ConsoleLogger)
}