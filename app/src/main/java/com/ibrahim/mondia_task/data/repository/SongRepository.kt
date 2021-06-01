package com.ibrahim.mondia_task.data.repository

import com.ibrahim.mondia_task.base.Universals
import com.ibrahim.mondia_task.data.model.SongsResponse
import com.ibrahim.mondia_task.data.model.TokenResponse
import com.ibrahim.mondia_task.network.call.NetworkBuilder
import com.ibrahim.mondia_task.network.response.NetworkResponse

class SongRepository {

    fun getSongsList(query: String): NetworkResponse<SongsResponse> {
        val builder = NetworkBuilder<SongsResponse>()
            .addHeader("Authorization", "Bearer ${Universals.token}")
            .addQueryParams("query", query)
            .setType(SongsResponse::class.java)
            .setPath("v2/api/sayt/flat")
            .setMethod("GET")

        return builder.build().callNetwork()
    }

    fun getToken(): NetworkResponse<TokenResponse> {

        val builder = NetworkBuilder<TokenResponse>()
            .setType(TokenResponse::class.java)
            .addHeader("X-MM-GATEWAY-KEY","Ge6c853cf-5593-a196-efdb-e3fd7b881eca")
            .setPath("v0/api/gateway/token/client")
            .setMethod("POST")

        return builder.build().callNetwork()
    }

}


