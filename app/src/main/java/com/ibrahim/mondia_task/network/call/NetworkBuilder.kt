package com.ibrahim.mondia_task.network.call

import android.net.Uri
import com.ibrahim.mondia_task.data.model.NetworkResponseModel
import java.net.HttpURLConnection
import java.net.URL

class NetworkBuilder<T: NetworkResponseModel>{

    lateinit var url: URL
    lateinit var urlConnection: HttpURLConnection

    private var type: Class<T>? = null
    private var queryParams: HashMap<String, String> = hashMapOf()
    private var headers: HashMap<String, String> = hashMapOf()
    private var path: String = "v2/api/sayt/flat"
    private var method: String = "GET"
    private var baseUrl = "https://staging-gateway.mondiamedia.com/"


    fun setType(type: Class<T>) = apply { this.type = type }
    fun addHeader(key: String, value: String) = apply { headers[key] = value }
    fun addQueryParams(key: String, value: String) = apply { queryParams[key] = value }
    fun setPath(path: String) = apply { this.path = path }
    fun setMethod(method: String) = apply { this.method = method }
    fun baseUrl(model: String) = apply { this.baseUrl = baseUrl }

    private fun buildUrl() {
        var requestUrl = baseUrl+path
        val uri = Uri.Builder()
            .encodedPath(requestUrl)
        queryParams.forEach {
            uri.appendQueryParameter(it.key, it.value);
        }
        url = URL(uri.build().toString())
    }


    fun build(): Executer2<T> {
        buildUrl()
        urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.requestMethod = method

        headers.forEach {
            urlConnection!!.addRequestProperty(it.key, it.value)
        }

        return Executer2(
            urlConnection!!,
            type as Class<T>
        )
    }


}