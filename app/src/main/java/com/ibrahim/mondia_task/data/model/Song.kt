package com.ibrahim.mondia_task.data.model

data class Song(
    val title: String = "",
    val artistName: String = "",
    val albumName: String = "",
    val cover: String = "",
    val genre: String = ""
){
    fun getCoverPath(): String {
        return when {
            cover.startsWith("//") -> cover.replaceFirst("//","http://")
            cover.startsWith("https//") -> cover
            cover.startsWith("http//") ->cover
            else -> cover
        }
    }
}
