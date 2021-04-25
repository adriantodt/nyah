package net.adriantodt.nyah.utils

import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandler
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KProperty

private typealias RequestBuilder = HttpRequest.Builder.() -> Unit

private typealias CompletableResponse<T> = CompletableFuture<HttpResponse<T>>

inline fun <T> HttpClient.send(bodyHandler: BodyHandler<T>, block: RequestBuilder): HttpResponse<T> {
    return send(HttpRequest.newBuilder().also(block).build(), bodyHandler)
}

inline fun <T> HttpClient.sendAsync(bodyHandler: BodyHandler<T>, block: RequestBuilder): CompletableResponse<T> {
    return sendAsync(HttpRequest.newBuilder().also(block).build(), bodyHandler)
}

fun String.urlEncode(charset: Charset = Charsets.UTF_8): String = URLEncoder.encode(this, charset)

val <T> Optional<T>.orNull: T? get() = orElse(null)

operator fun <T> CompletableFuture<T>.getValue(thisObj: Any?, property: KProperty<*>): T = join()
