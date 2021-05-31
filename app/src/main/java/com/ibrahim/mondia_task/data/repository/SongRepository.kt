package com.ibrahim.mondia_task.data.repository

import com.ibrahim.mondia_task.data.model.ResponseType
import com.ibrahim.mondia_task.data.model.Song
import com.ibrahim.mondia_task.data.model.Test
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class SongRepository {


    fun getSongsList(): NetworkResponse<Test> {
        val executer = Executer<Test>(Test::class.java)

        val networkResponse = executer.test()
        val response = NetworkResponse<Song>()

        networkResponse.register({
            response.sucess.invoke(Song())
        },{
            response.invokeError(it)
        })

        return networkResponse
    }

    class Executer<T: ResponseType>(val type: Class<T>) {


        fun test(): NetworkResponse<T> {
            val responseCallBack = NetworkResponse<T>()

            thread {
                val url: URL
                var urlConnection: HttpURLConnection? = null
                try {
                    url = URL("http://staging-gateway.mondiamedia.com/v2/api/sayt/flat?query=te")
                    urlConnection = url.openConnection() as HttpURLConnection

                    urlConnection.setRequestProperty(
                        "Authorization",
                        "Bearer Cbfb4f2b3-4500-4be4-a075-9c9330e578b0"
                    );
                    val `in`: InputStream = urlConnection.getInputStream()
                    val isw = InputStreamReader(`in`)
                    var data = isw.readText()
                    responseCallBack.sucess.invoke(data.mapToType(type))
                } catch (e: Exception) {
                    responseCallBack.error.invoke(e)
                    e.printStackTrace()
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect()
                    }
                }
            }

            return responseCallBack
        }

    }

    data class NetworkResponse<T: Any>(
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
}


private fun <T> String.mapToType(klass: Any): T {

    klass == Test::class.java
    klass is Test

    return when(klass){
        Test::class.java -> {
            mapToSongs(this) as T
        }
        else -> {
            null as T
        }
    }
}

fun <T> mapToSongs(it: String): T {
    val songsList = Test()
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

    return songsList as T

}


inline fun <reified T> checkType(obj: Object, contract: T) {
    if (obj is T) {
        // object implements the contract type T
    }
}