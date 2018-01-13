package de.turbocache3000.polizei.util

import java.io.PrintWriter
import java.io.StringWriter

fun Throwable.stacktraceAsString(): String {
    return StringWriter().use { sw ->
        PrintWriter(sw).use { pw ->
            printStackTrace(pw)
        }
        sw.toString()
    }
}