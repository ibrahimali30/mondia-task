package com.ibrahim.mondia_task.network.call


import com.ibrahim.mondia_task.network.response.NetworkResponse
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.util.concurrent.Executors
import java.util.concurrent.Future


class NetworkCaller<T>(
     private val urlConnection: HttpURLConnection,
     private val mapper: (data: String) -> T
) {

    fun callNetwork(): NetworkResponse<T> {
        val responseCallBack = NetworkResponse<T>()
        val service = Executors.newCachedThreadPool()
        val future: Future<*> = service.submit {

            try {
                val `in`: InputStream = urlConnection.getInputStream()
                val isw = InputStreamReader(`in`)
                val data = isw.readText()

                responseCallBack.sucess.invoke(mapper(data))

            }catch (e: Exception) {
                if (e.message != "thread interrupted")
                responseCallBack.error.invoke(e)
            } finally {
                urlConnection?.disconnect()
            }

        }

        responseCallBack.cancelCall = {
            future.cancel(true)
        }

        return responseCallBack
    }

}
