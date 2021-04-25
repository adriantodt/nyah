package net.adriantodt.nyah.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.adriantodt.nyah.torrents.QBittorrentClient
import net.adriantodt.nyah.torrents.TorrentClient
import java.net.http.HttpClient

@Serializable
sealed class TorrentClientConfig {
    abstract val baseUrl: String

    abstract fun create(httpClient: HttpClient): TorrentClient

    @Serializable
    @SerialName("qbittorrent")
    data class QBittorrent(
        override val baseUrl: String,
        val username: String,
        val password: String
    ) : TorrentClientConfig() {
        override fun create(httpClient: HttpClient): TorrentClient {
            return QBittorrentClient(httpClient, baseUrl, username, password)
        }
    }
}
