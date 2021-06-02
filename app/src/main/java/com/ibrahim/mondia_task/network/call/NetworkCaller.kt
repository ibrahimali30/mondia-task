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

    /**
     * @param urlConnection contains all the headers and query params
     * that was handled from th @NetworkBuild
     * @param mapper is a function that transform the json string to the generic type
     * is passed upon building the @NetworkCaller<T> from repo
     */

    fun callNetwork(): NetworkResponse<T> {
        val responseCallBack = NetworkResponse<T>()
        val service = Executors.newCachedThreadPool()
        val future: Future<*> = service.submit {

            try {
                val `in`: InputStream = urlConnection.getInputStream()
                val isw = InputStreamReader(`in`)
                val data = isw.readText()

                responseCallBack.successResponse.invoke(mapper(data))

            }catch (e: Exception) {
                if (e.message != "thread interrupted")
                responseCallBack.errorResponse.invoke(e)
            } finally {
                urlConnection?.disconnect()
            }

        }

        //cancel network upon request
        responseCallBack.cancelCall = {
            future.cancel(true)
        }

        return responseCallBack
    }

}
