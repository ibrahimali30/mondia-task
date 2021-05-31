package com.ibrahim.mondia_task

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test {
            val jsonArray = JSONArray(it)
            for (position in 0..jsonArray.length()){
                (jsonArray.get(0) as JSONObject).apply {
                    val title = getString("title")
                    val name = getJSONObject("mainArtist").getString("name")
                    val album = getJSONObject("release").getString("title")
                    val cover = getJSONObject("cover").getString("medium")
                    val v = getJSONArray("genres").toString(1)

                }
            }
        }

        SongsViewModel().fetchSongsList("te")
    }
}




fun test(function: (result: String) -> Unit) {
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
            function(data)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect()
            }
        }
    }
}
