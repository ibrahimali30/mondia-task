package com.ibrahim.mondia_task.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibrahim.mondia_task.R
import com.ibrahim.mondia_task.data.model.Song
import com.ibrahim.mondia_task.view.extensions.onTextChanged
import com.ibrahim.mondia_task.viewmodel.SongsListScreenState
import com.ibrahim.mondia_task.viewmodel.SongsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_error_view.*


class MainActivity : AppCompatActivity() {


    private val songsViewModel by lazy {
        ViewModelProvider(this).get(SongsViewModel::class.java)
    }

    private val songsAdapter: SongsAdapter by lazy {
        SongsAdapter(::onAForecastItemClicked)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observeScreenState()
        initRecyclerView()
        songsViewModel.getToken()

    }

    private fun observeScreenState() {
        songsViewModel.screenState.observe(this, Observer {
            onScreenStateChanged(it)
        })
    }

    private fun onScreenStateChanged(state: SongsListScreenState?) {
        Log.d("TAG", "onScreenStateChanged: ${state.toString()}")

        when (state) {
            is SongsListScreenState.SuccessAPIResponse -> handleSuccess(state.data)
            is SongsListScreenState.SuccessLogin -> initSearchView()
            is SongsListScreenState.ErrorLoadingFromApi -> {
                handleErrorLoadingFromApi(state.error, state.retry)
            }
            else -> {}
        }

        handleLoadingVisibility(state == SongsListScreenState.Loading)
        handleErrorViewsVisibility(state)
    }

    private fun handleErrorViewsVisibility(state: SongsListScreenState?) {
        if (state is SongsListScreenState.ErrorLoadingFromApi)
            errorViewLayout.visibility = View.VISIBLE
        else
            errorViewLayout.visibility = View.GONE

    }

    private fun handleLoadingVisibility(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun handleSuccess(data: List<Song>) {
        if (data.isEmpty()) {
            tvNoSoredData.visibility = View.VISIBLE
        } else {
            tvNoSoredData.visibility = View.INVISIBLE
        }
        songsAdapter.setList(data)
    }

    private fun handleErrorLoadingFromApi(error: Throwable, retry: () -> Unit) {
        btRetry.setOnClickListener {
            retry()
        }
    }

    private fun initRecyclerView() {
        rvForecast.layoutManager = LinearLayoutManager(this)
        rvForecast.adapter = songsAdapter
    }

    private fun onAForecastItemClicked(song: Song, iv: ImageView, tv: TextView) {
        SongDetailsActivity.startCallingIntent(song, this, iv, tv)
    }


    private fun initSearchView() {
        searchView.isActivated = true
        searchView.isIconified = false
        searchView.setIconifiedByDefault(false)
        searchView.onTextChanged {
            songsViewModel.fetchSongsList(searchView.query.toString())
        }
    }
}

