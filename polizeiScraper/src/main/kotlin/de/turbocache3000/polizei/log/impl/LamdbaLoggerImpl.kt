package de.turbocache3000.polizei.log.impl

import com.amazonaws.services.lambda.runtime.LambdaLogger
import de.turbocache3000.polizei.log.api.Logger

class LamdbaLoggerImpl(
        private val lambdaLogger: LambdaLogger
) : Logger {
    override fun log(message: String, vararg parameters: Any?) {
        lambdaLogger.log(StringFormatter.format(message, *parameters))
    }
}