package com.ibrahim.mondia_task

import androidx.lifecycle.ViewModel
import com.ibrahim.mondia_task.data.repository.SongRepository

class SongsViewModel: ViewModel(){


    val songRepository = SongRepository()

    fun fetchSongsList(query: String){
        getToken()
        songRepository.getSongsList()
        .register( {
            it
        },{
            it
        })

    }


    fun getToken(){
        songRepository.getToken()
            .register( {
                it
            },{
                it
            })
    }


}