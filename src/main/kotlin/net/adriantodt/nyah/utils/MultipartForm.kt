package net.adriantodt.nyah.utils

import java.lang.System.currentTimeMillis

data class MultipartForm(private val parts: Map<String, String>) {
    constructor(pair: Pair<String, String>) : this(mapOf(pair))
    constructor(vararg pairs: Pair<String, String>) : this(mapOf(*pairs))

    private val boundary = "---${currentTimeMillis()}---"

    val contentTypeHeader by lazy { "multipart/form-data; boundary=$boundary" }

    fun body(): String {
        return buildString {
            for ((name, value) in parts) {
                append("--$boundary")
                append(NEW_LINE)

                append("Content-Disposition: form-data; name=\"$name\"")
                append(NEW_LINE)
                append(NEW_LINE)

                append(value)
                append(NEW_LINE)
            }

            // finish
            append("$NEW_LINE--$boundary--$NEW_LINE")
        }
    }

    companion object {
        private const val NEW_LINE = "\r\n"
    }
}
