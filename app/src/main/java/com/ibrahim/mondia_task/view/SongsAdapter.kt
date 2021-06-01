package com.ibrahim.mondia_task.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ibrahim.mondia_task.R
import com.ibrahim.mondia_task.data.model.Song
import com.ibrahim.mondia_task.data.network.ImageLoader
import kotlinx.android.synthetic.main.song_forecast.view.*

class SongsAdapter(
        val onAForecastItemClicked: (model: Song) -> Unit
) :
    RecyclerView.Adapter<SongsAdapter.ViewHolder>() {

    val commitCallback = Runnable {}
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Song>() {

        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.name == newItem.name
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
            LayoutInflater.from(parent.context).inflate(R.layout.song_forecast, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecastUiModel = differ.currentList[position]
        holder.bind(forecastUiModel)

        holder.itemView.setOnClickListener {
            onAForecastItemClicked.invoke(forecastUiModel)
        }
    }

    fun setList(list: List<Song>) {
        differ.submitList(list, commitCallback)
    }

    fun clear() {
        differ.submitList(listOf(), commitCallback)
    }

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: Song) {
            itemView.apply {
                tvSongname.text = model.titel
                ImageLoader.loadImage(iv, model.getCoverPath())
            }

        }

    }
}