package com.ibrahim.mondia_task.data.model

data class Song(
    val titel: String = "",
    val name: String = "",
    val albumName: String = "",
    val cover: String = ""
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
