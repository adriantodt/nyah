package net.adriantodt.nyah

import mu.KLogging
import net.adriantodt.nyah.config.NyahConfig
import net.adriantodt.nyah.persistence.NyahPersistence
import net.adriantodt.nyah.utils.sendAsync
import org.jsoup.parser.Parser
import org.jsoup.parser.XmlTreeBuilder
import java.net.URI
import java.net.http.HttpClient
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import javax.inject.Singleton
import java.net.http.HttpResponse.BodyHandlers.ofString as stringHandler

@Singleton
class Nyah {
    companion object : KLogging()

    private val httpClient: HttpClient = HttpClient.newHttpClient()
    private val xmlParser = Parser(XmlTreeBuilder())

    private val config = NyahConfig.load()
    private val persistence = NyahPersistence.load()
    private val torrentClient = config.torrentClient.create(httpClient)

    fun trigger(): CompletableFuture<Void> {
        return config.urls
            .map { this.parseTorrents(it).thenComposeAsync(this::addTorrents) }
            .toTypedArray()
            .let { CompletableFuture.allOf(*it) }
    }

    private fun addTorrents(torrents: List<TorrentLink>): CompletableFuture<Boolean> {
        val links = torrents.mapTo(mutableSetOf(), TorrentLink::link)

        links.removeAll(persistence.knownLinks)
        persistence.knownLinks.addAll(links)
        persistence.save()

        return torrentClient.addTorrents(links.toList())
    }

    private fun parseTorrents(url: String): CompletableFuture<List<TorrentLink>> {
        return this.httpClient
            .sendAsync(stringHandler()) { uri(URI.create(url)) }
            .thenApplyAsync { handleXml(it.body(), url) }
    }

    private fun handleXml(body: String, url: String): List<TorrentLink> {
        return this.xmlParser
            .parseInput(body, url)
            .select("item")
            .map {
                TorrentLink(
                    it.selectFirst("title").text(),
                    it.selectFirst("link").text()
                )
            }
            .filter { persistence.knownLinks.contains(it.link) }
            .distinctBy { it.link }
    }

    private data class TorrentLink(val title: String, val link: String)
}

