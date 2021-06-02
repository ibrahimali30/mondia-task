package com.ibrahim.mondia_task.view.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ibrahim.mondia_task.R
import com.ibrahim.mondia_task.data.model.Song
import com.ibrahim.mondia_task.network.ImageLoader
import com.ibrahim.mondia_task.view.extensions.visible
import kotlinx.android.synthetic.main.activity_song_details.*

class SongDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_details)

        val song = intent.getSerializableExtra(EXTRA_SERIZLIZABLE_OBJECT) as Song

        bindSongData(song)

    }

    private fun bindSongData(song: Song) {
        tvArtistName.text = song.artistName
        tvTitle.text = song.title
        tvAlbumName.text = song.albumName
        tvType.text = song.type
        if (song.genre.isNotEmpty()) {
            btGenre.visible()
            btGenre.text = song.genre
        }

        ImageLoader.loadImage(ivTopImagePoster, song.getCoverPath())
    }


    companion object{

        val EXTRA_SERIZLIZABLE_OBJECT = "character"

        fun startCallingIntent(
            recipeItem: Song,
            context: Activity,
            viewToTransition: View,
            viewToTransition2: View
        ) {
            val options = ActivityOptions.makeSceneTransitionAnimation(
                context,
                Pair(viewToTransition, "tr"),
                Pair(viewToTransition2, "tr2")
            )

            val intent2 = Intent(context, SongDetailsActivity::class.java)
            intent2.putExtra(EXTRA_SERIZLIZABLE_OBJECT, recipeItem)

            context.startActivity(intent2, options.toBundle())

        }
    }

}