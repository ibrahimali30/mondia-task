package com.ibrahim.mondia_task.data.model

data class Song(
    val titel: String = "",
    val name: String = "",
    val albumName: String = "",
    val cover: String = ""
): ResponseType



class Test : ArrayList<Song>(), ResponseType

interface ResponseType