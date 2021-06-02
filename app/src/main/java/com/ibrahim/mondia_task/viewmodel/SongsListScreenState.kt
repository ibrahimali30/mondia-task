package com.ibrahim.mondia_task.viewmodel

import com.ibrahim.mondia_task.data.model.Song

sealed class SongsListScreenState {
    object Loading : SongsListScreenState()
    class ErrorLoadingFromApi(val error: Throwable, val retry: () -> Unit) :
        SongsListScreenState()

    class SuccessAPIResponse(val data: List<Song>) : SongsListScreenState()
    object SuccessLogin : SongsListScreenState()
}