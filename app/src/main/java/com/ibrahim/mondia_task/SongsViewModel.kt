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
        val v = Calendar.getInstance().timeInMillis
        if (query.length < 2 )return
        songRepository.getSongsList(query)
            .register({
                Log.d("TAG", "fetchSongsList: ${Calendar.getInstance().timeInMillis - v}")
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