package com.ibrahim.mondia_task.data.model

import org.json.JSONObject
import java.io.Serializable

data class Song(
    val title: String = "",
    val artistName: String = "",
    val albumName: String = "",
    val cover: String = "",
    val genre: String = "",
    val type: String
): Serializable{
    fun getCoverPath(): String {
        return when {
            cover.startsWith("//") -> cover.replaceFirst("//","http://")
            cover.startsWith("https//") -> cover
            cover.startsWith("http//") ->cover
            else -> cover
        }
    }
}
