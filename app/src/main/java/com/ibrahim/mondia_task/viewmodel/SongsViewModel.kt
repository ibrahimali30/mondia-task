package com.ibrahim.mondia_task.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibrahim.mondia_task.base.Global
import com.ibrahim.mondia_task.data.model.SongsResponse
import com.ibrahim.mondia_task.data.model.TokenResponse
import com.ibrahim.mondia_task.network.response.NetworkResponse
import com.ibrahim.mondia_task.data.repository.SongRepository

class SongsViewModel : ViewModel() {

    private var lastNetworkResponse: NetworkResponse<SongsResponse>? = null

    val screenState by lazy { MutableLiveData<SongsListScreenState>() }


    val songRepository = SongRepository()

    fun fetchSongsList(query: String) {
        if (query.length < 2) return
        lastNetworkResponse?.cancelNetworkCall()
        setLoading()

        songRepository.getSongsList(query).also { lastNetworkResponse = it }
            .registerCallBack(
                onSuccess = {
                    screenState.postValue(
                        SongsListScreenState.SuccessAPIResponse(it)
                    )
                },
                onFailure = {
                    screenState.postValue(
                        SongsListScreenState.ErrorLoadingFromApi(error = it, retry = { fetchSongsList(query) })
                    )
                })

    }


    fun getToken() {
        setLoading()

        songRepository.getToken()
            .registerCallBack(
                onSuccess = {
                    handleLoginSuccess(it)
                },
                onFailure = {
                    handleLoginFailure(it)
                }
            )
    }

    private fun setLoading() {
        screenState.postValue(SongsListScreenState.Loading)
    }

    private fun handleLoginSuccess(tokenResponse: TokenResponse) {
        Global.token = tokenResponse.accessToken
        fetchSongsList("te")
        screenState.postValue(
            SongsListScreenState.SuccessLogin()
        )
    }

    private fun handleLoginFailure(it: Exception) {
        screenState.postValue(
            SongsListScreenState.ErrorLoadingFromApi(error = it, retry = {getToken()})
        )
    }




}