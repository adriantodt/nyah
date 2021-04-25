package net.adriantodt.nyah.persistence

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class NyahPersistence(val knownLinks: MutableSet<String>) {

    fun save() {
        file.writeText(Json.encodeToString(this))
    }

    companion object {
        private val file = File("nyah_persistence.json")

        fun load(): NyahPersistence {
            return Json.decodeFromString(file.readText())
        }
    }
}
