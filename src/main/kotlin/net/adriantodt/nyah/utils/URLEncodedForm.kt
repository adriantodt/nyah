package net.adriantodt.nyah.utils


data class URLEncodedForm(private val parts: Map<String, String>) {
    constructor(pair: Pair<String, String>) : this(mapOf(pair))
    constructor(vararg pairs: Pair<String, String>) : this(mapOf(*pairs))

    val contentTypeHeader = "application/x-www-form-urlencoded"

    fun body(): String {
        return parts.map { (k, v) -> "${k.urlEncode()}=${v.urlEncode()}" }.joinToString("&")
    }
}
