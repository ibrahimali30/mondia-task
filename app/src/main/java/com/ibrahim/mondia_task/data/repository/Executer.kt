package com.ibrahim.mondia_task.data.repository

import com.ibrahim.mondia_task.data.mapper.mapToType
import com.ibrahim.mondia_task.data.model.NetworkResponseModel
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class Executer<T: NetworkResponseModel>(
    val type: Class<T>,
    var queryParams: HashMap<String, String> = hashMapOf<String,String>(),
    var headers: HashMap<String, String> = hashMapOf(),
    val path: String = "v2/api/sayt/flat",
    val method: String = "GET"
) {
    val baseUrl = "https://staging-gateway.mondiamedia.com/"


    var requestUrl = baseUrl+path

    lateinit var url: URL
    lateinit var urlConnection: HttpURLConnection

    fun test(): NetworkResponse<T> {
        val responseCallBack = NetworkResponse<T>()

        thread {
            addQueryParams(queryParams)

            try {

                url = URL(requestUrl)
                urlConnection = url.openConnection() as HttpURLConnection
                addHeaderParams(headers)
                urlConnection.requestMethod = method

                val `in`: InputStream = urlConnection.getInputStream()
                val isw = InputStreamReader(`in`)
                val data = isw.readText()
                responseCallBack.sucess.invoke(data.mapToType(type) as T)
            } catch (e: Exception) {
                responseCallBack.error.invoke(e)
                e.printStackTrace()
            } finally {
                urlConnection?.disconnect()
            }
        }

        return responseCallBack
    }

    private fun addHeaderParams(headers: java.util.HashMap<String, String>) {
        headers.forEach {
            urlConnection.setRequestProperty(it.key, it.value)

        }
    }

    private fun addQueryParams(queryParams: java.util.HashMap<String, String>) {
        requestUrl += "?"
        queryParams.forEach {
            requestUrl += "${it.key}=${it.value},"
        }
        requestUrl = requestUrl.dropLast(1)
    }
}