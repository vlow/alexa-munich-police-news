package de.turbocache3000.polizei.log.api

/**
 * Logger.
 */
interface Logger {
    /**
     * Logs a trace [message] with the given [parameters].
     */
    fun trace(message: String, vararg parameters: Any?) {
        log("TRACE $message", *parameters)
    }

    /**
     * Logs a debug [message] with the given [parameters].
     */
    fun debug(message: String, vararg parameters: Any?) {
        log("DEBUG $message", *parameters)
    }

    /**
     * Logs an info [message] with the given [parameters].
     */
    fun info(message: String, vararg parameters: Any?) {
        log("INFO $message", *parameters)
    }

    /**
     * Logs a warn [message] with the given [parameters].
     */
    fun warn(message: String, vararg parameters: Any?) {
        log("WARN $message", *parameters)
    }

    /**
     * Logs an error [message] with the given [parameters].
     */
    fun error(message: String, vararg parameters: Any?) {
        log("ERROR $message", *parameters)
    }

    /**
     * Logs a [message] with the given [parameters].
     */
    fun log(message: String, vararg parameters: Any?)
}