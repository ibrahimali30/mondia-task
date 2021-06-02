package com.ibrahim.mondia_task.data.repository

import com.ibrahim.mondia_task.base.Global
import com.ibrahim.mondia_task.data.mapper.mapToSongs
import com.ibrahim.mondia_task.data.mapper.mapToTokenResponse
import com.ibrahim.mondia_task.data.model.SongsResponse
import com.ibrahim.mondia_task.data.model.TokenResponse
import com.ibrahim.mondia_task.network.call.NetworkBuilder
import com.ibrahim.mondia_task.network.response.NetworkResponse

class SongRepository {

    fun getSongsList(query: String): NetworkResponse<SongsResponse> {
        val builder = NetworkBuilder<SongsResponse>()
            .addHeader(Global.token_header_key, Global.token)
            .addQueryParams("query", query)
            .setPath("v2/api/sayt/flat")
            .setMethod("GET")

        val networkResponse = builder.build(::mapToSongs).callNetwork()
        /** no reason for this registration
         * it is just to show that it can handle multiple subscribers
         * in cast extra operations is needed
         * **/
        networkResponse.registerCallBack(
            onSuccess = {
                it.size
            }, onFailure = {
                it.message
            })


        return networkResponse
    }

    fun getToken(): NetworkResponse<TokenResponse> {

        val builder = NetworkBuilder<TokenResponse>()
            .addHeader(Global.gateway_header_key, Global.gateway_header_value)
            .setPath("v0/api/gateway/token/client")
            .setMethod("POST")

        return builder.build(::mapToTokenResponse).callNetwork()
    }
}


