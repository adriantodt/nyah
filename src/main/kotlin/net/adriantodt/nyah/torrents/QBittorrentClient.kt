package net.adriantodt.nyah.torrents

import net.adriantodt.nyah.utils.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpResponse.BodyHandlers.discarding
import java.util.concurrent.CompletableFuture
import java.net.http.HttpRequest.BodyPublishers.ofString as stringPublisher

class QBittorrentClient(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    username: String,
    password: String
) : TorrentClient {
    private val loginCookie by getLoginCookie(username, password)

    override fun addTorrents(urls: List<String>): CompletableFuture<Boolean> {
        return this.httpClient
            .sendAsync(discarding()) {
                uri(URI.create("$baseUrl/api/v2/torrents/add"))
                val form = MultipartForm("urls" to urls.joinToString("\n"))

                header("Cookie", "SID=$loginCookie")
                header("Content-Type", form.contentTypeHeader)
                POST(stringPublisher(form.body()))
            }
            .thenApply {
                it.statusCode() == 200
            }
    }

    private fun getLoginCookie(username: String, password: String): CompletableFuture<String?> {
        return this.httpClient
            .sendAsync(discarding()) {
                uri(URI.create("$baseUrl/api/v2/torrents/add"))
                val form = URLEncodedForm("username" to username, "password" to password)

                header("Referer", baseUrl)
                header("Content-Type", form.contentTypeHeader)
                POST(stringPublisher(form.body()))
            }
            .thenApply {
                it.headers()
                    .firstValue("Set-Cookie")
                    .orNull
                    ?.parseSetCookies()
                    ?.get("SID")
            }
    }

    private fun String.parseSetCookies(): Map<String, String> {
        return this.split(";")
            .associate {
                val (key, value) = it.split("=", limit = 2)
                key.trim() to value.trim()
            }
    }
}
