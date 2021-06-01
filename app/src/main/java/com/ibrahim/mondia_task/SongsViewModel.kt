package com.ibrahim.mondia_task

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibrahim.mondia_task.base.Universals
import com.ibrahim.mondia_task.data.model.Song
import com.ibrahim.mondia_task.data.repository.SongRepository
import java.util.*

class SongsViewModel : ViewModel() {

    val screenState by lazy { MutableLiveData<ForecastScreenState>() }


    val songRepository = SongRepository()

    fun fetchSongsList(query: String) {
        if (query.length < 2 )return

        screenState.postValue(ForecastScreenState.Loading)

        val callTime = Calendar.getInstance().timeInMillis

        songRepository.getSongsList(query)
            .register({
                Log.d("TAG", "response time = ${Calendar.getInstance().timeInMillis - callTime}")
                screenState.postValue(
                    ForecastScreenState.SuccessAPIResponse(it)
                )
            }, {
                screenState.postValue(
                    ForecastScreenState.ErrorLoadingFromApi(it, {fetchSongsList(query)})
                )
            })

    }


    fun getToken() {
        songRepository.getToken()
            .register({
                Universals.token = it.accessToken
                fetchSongsList("te")
            }, {
                screenState.postValue(
                    ForecastScreenState.ErrorLoadingFromApi(it, {getToken()})
                )
            })
    }


    sealed class ForecastScreenState {
        object Loading : ForecastScreenState()
        class ErrorLoadingFromApi(val error: Throwable, val retry: () -> Unit) : ForecastScreenState()
        class SuccessAPIResponse(val data: List<Song>) : ForecastScreenState()
    }

}