package net.adriantodt.nyah.config

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class NyahConfig(val urls: List<String>, val torrentClient: TorrentClientConfig) {
    companion object {
        fun load(): NyahConfig {
            return Json.decodeFromString(File("nyah.json").readText())
        }
    }
}

