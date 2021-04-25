package net.adriantodt.nyah.torrents

import java.util.concurrent.CompletableFuture

interface TorrentClient {
    fun addTorrents(urls: List<String>): CompletableFuture<Boolean>
}
