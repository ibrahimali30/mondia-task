package com.ibrahim.mondia_task.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ibrahim.mondia_task.R
import com.ibrahim.mondia_task.data.model.Song
import com.ibrahim.mondia_task.network.ImageLoader
import com.ibrahim.mondia_task.view.extensions.visible
import kotlinx.android.synthetic.main.item_song.view.*

class SongsAdapter(
    private val onSongItemClicked: (model: Song, imageView: ImageView, tv: TextView) -> Unit
) :
    RecyclerView.Adapter<SongsAdapter.ViewHolder>() {

    private val commitCallback = Runnable {}
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Song>() {

        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.artistName == newItem.artistName
        }
        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val songUiModel = differ.currentList[position]
        holder.bind(songUiModel)

        holder.itemView.setOnClickListener {
            onSongItemClicked.invoke(songUiModel, holder.itemView.ivPoster, holder.itemView.tvArtistName)
        }
    }

    fun setList(list: List<Song>) {
        differ.submitList(list, commitCallback)
        notifyDataSetChanged()
    }

    fun clear() {
        differ.submitList(listOf(), commitCallback)
        notifyDataSetChanged()
    }

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: Song) {
            itemView.apply {
                tvArtistName.text = model.artistName
                tvTitle.text = model.title
                tvAlbumName.text = model.albumName
                tvType.text = model.type
                if (model.genre.isNotEmpty()) {
                    btGenre.visible()
                    btGenre.text = model.genre
                }

                ImageLoader.loadImage(ivPoster, model.getCoverPath())
            }

        }

    }
}
