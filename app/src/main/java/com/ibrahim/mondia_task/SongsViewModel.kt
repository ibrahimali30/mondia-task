package com.ibrahim.mondia_task

import androidx.lifecycle.ViewModel
import com.ibrahim.mondia_task.data.repository.SongRepository

class SongsViewModel: ViewModel(){


    val songRepository = SongRepository()

    fun fetchSongsList(query: String){
        val v = songRepository.getSongsList()

        v.register( {
            it
        },{
            it
        })

    }


}