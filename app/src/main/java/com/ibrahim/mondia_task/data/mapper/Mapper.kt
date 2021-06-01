package com.ibrahim.mondia_task.data.mapper


import com.ibrahim.mondia_task.data.model.Song
import com.ibrahim.mondia_task.data.model.SongsResponse
import com.ibrahim.mondia_task.data.model.TokenResponse
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception



fun mapToTokenResponse(s: String): TokenResponse {
    JSONObject(s).apply {
        val accessToken = getString("accessToken")
        val tokenType = getString("tokenType")
        val expiresIn = getLong("expiresIn")
        return TokenResponse(accessToken, tokenType, expiresIn)
    }

}

fun mapToSongs(it: String): SongsResponse {
    val songsList = SongsResponse()
    val jsonArray = JSONArray(it)
    if (jsonArray.length() == 0) return songsList
    for (position in 0..jsonArray.length()-1){
        (jsonArray.get(position) as JSONObject).apply {

            try {
                val title = getString("title")
                val name = getJSONObject("mainArtist").getString("name")
                val album = getJSONObject("release").getString("title")
                val cover = getJSONObject("cover").getString("medium")

                val genres = getJSONArray("genres")
                val genre = if (genres.length() == 0){
                    ""
                }else{
                    genres.get(0).toString()
                }

                songsList.add(Song(title, name, album, cover, genre))
            }catch (e:Exception){}

        }
    }

    return songsList

}
