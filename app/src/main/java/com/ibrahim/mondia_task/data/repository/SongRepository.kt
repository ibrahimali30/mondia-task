package com.ibrahim.mondia_task.data.repository

import com.ibrahim.mondia_task.data.model.NetworkResponseModel
import com.ibrahim.mondia_task.data.model.Song
import com.ibrahim.mondia_task.data.model.SongsResponse
import com.ibrahim.mondia_task.data.model.TokenResponse
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class SongRepository {


    fun getSongsList(): NetworkResponse<SongsResponse> {
        val executer = Executer(
            SongsResponse::class.java,
            hashMapOf(Pair("query","te")),
            hashMapOf(Pair("Authorization","Bearer Cbfb4f2b3-4500-4be4-a075-9c9330e578b0"))
        )

        val networkResponse = executer.test()

        return networkResponse
    }


    fun getToken(): NetworkResponse<TokenResponse> {
        val executer = Executer(
            TokenResponse::class.java,
            headers = hashMapOf(Pair("X-MM-GATEWAY-KEY","Ge6c853cf-5593-a196-efdb-e3fd7b881eca")),
            path = "v0/api/gateway/token/client",
            method = "POST"
        )

        val networkResponse = executer.test()

        return networkResponse
    }



}

    data class NetworkResponse<T: NetworkResponseModel>(
        val sucess: SuccessResponseCallBack<T> = SuccessResponseCallBack(),
        val error: ErrorResponseCallBack = ErrorResponseCallBack()
    ) {

        fun register(function: (sucess: T) -> Unit, function1: (e: java.lang.Exception) -> Unit) {
            sucess.register(function)
            error.register(function1)
        }

        fun invokeError(e: java.lang.Exception) {
            error.invoke(e)
        }

    }

    class SuccessResponseCallBack<T> {
        val functions = mutableListOf<(result: T) -> Unit>()

        fun register(func: (result: T) -> Unit) {
            functions.add(func)
        }

        fun invoke(data: T) {
            functions.forEach {
                it.invoke(data)
            }
        }
    }


    class ErrorResponseCallBack {
        val functions = mutableListOf<(result: java.lang.Exception) -> Unit>()

        fun register(func: (result: Exception) -> Unit) {
            functions.add(func)
        }

        fun invoke(data: java.lang.Exception) {
            functions.forEach {
                it.invoke(data)
            }
        }
    }


 fun <T: NetworkResponseModel>String.mapToType(klass: Class<T>): NetworkResponseModel {

    return when(klass){
        TokenResponse::class.java -> {
            mapToTokenResponse(this)
        }
        SongsResponse::class.java -> {
            mapToSongs(this)
        }
        else -> {
            null as NetworkResponseModel
        }
    }
}

fun mapToTokenResponse(s: String): NetworkResponseModel {
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
    for (position in 0..jsonArray.length()){
        (jsonArray.get(0) as JSONObject).apply {
            val title = getString("title")
            val name = getJSONObject("mainArtist").getString("name")
            val album = getJSONObject("release").getString("title")
            val cover = getJSONObject("cover").getString("medium")
            val v = getJSONArray("genres").toString(1)

            songsList.add(Song(title, name, album))

        }
    }

    return songsList

}
