package de.turbocache3000.polizei.log.impl

object StringFormatter {
    /**
     * Fills strings with {} placeholders. Takes the placeholders from the [parameters] array.
     */
    fun format(message: String, vararg parameters: Any?): String {
        return buildString {
            var i = 0
            var argumentIndex = 0

            while (i < message.length) {
                val char = message[i]

                // If it starts with {
                if (char == '{') {
                    // And it's not the end of the string
                    if (i < message.length - 1) {
                        val nextChar = message[i + 1]
                        // And is followed by }
                        if (nextChar == '}') {
                            // And we have an argument left
                            if (argumentIndex < parameters.size) {
                                // Replace {} with an argument
                                append(parameters[argumentIndex])
                                argumentIndex++
                                // And skip the }
                                i += 2
                                continue
                            }
                        }
                    }
                }

                // No parameter replacement necessary, copy original string
                append(char)
                i += 1
            }
        }
    }
}