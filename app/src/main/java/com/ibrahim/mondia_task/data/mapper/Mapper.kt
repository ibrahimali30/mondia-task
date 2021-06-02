package com.ibrahim.mondia_task.data.mapper


import com.ibrahim.mondia_task.data.model.Song
import com.ibrahim.mondia_task.data.model.SongsResponse
import com.ibrahim.mondia_task.data.model.TokenResponse
import org.json.JSONArray
import org.json.JSONObject


fun mapToTokenResponse(tokenJson: String): TokenResponse {
    JSONObject(tokenJson).apply {
        val accessToken = getString("accessToken")
        val tokenType = getString("tokenType")
        val expiresIn = getLong("expiresIn")
        return TokenResponse(accessToken, tokenType, expiresIn)
    }
}

fun mapToSongs(songsJson: String): SongsResponse {
    val songsList = SongsResponse()
    val jsonArray = JSONArray(songsJson)
    if (jsonArray.length() == 0) return songsList
    for (position in 0..jsonArray.length()-1){

        (jsonArray.get(position) as JSONObject).apply {
            try {
                val title = getString("title")
                val name = getJSONObject("mainArtist").getString("name")
                val cover = getJSONObject("cover").getString("medium")
                val type = getString("type")

                val album = if (type == "song"){
                    getJSONObject("release").getString("title")
                }else{
                    ""
                }
                val genre = try {
                    getJSONArray("genres").get(0).toString()
                }catch (e: Exception){
                    ""
                }
                songsList.add(Song(title, name, album, cover, genre, type))
            }catch (e:Exception){ }
        }
    }
    return songsList
}
