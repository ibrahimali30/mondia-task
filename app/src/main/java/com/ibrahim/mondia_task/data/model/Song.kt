package com.ibrahim.mondia_task.data.model

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
            else -> cover
        }
    }
}
