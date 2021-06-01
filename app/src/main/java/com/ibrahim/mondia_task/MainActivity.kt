package com.ibrahim.mondia_task

import android.R.attr.src
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ibrahim.mondia_task.data.model.Song
import com.ibrahim.mondia_task.data.network.ImageLoader
import com.ibrahim.mondia_task.view.SongsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_error_view.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    val songsViewModel by lazy {
        ViewModelProvider(this).get(SongsViewModel::class.java)
    }
    lateinit var adapter: SongsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observeScreenState()
        initSearchView()
        initRecyclerView()
        songsViewModel.getToken()
        test()
    }

    private fun observeScreenState() {
        songsViewModel.screenState.observe(this , Observer {
            onScreenStateChanged(it)
        })
    }

    private fun onScreenStateChanged(state: SongsViewModel.ForecastScreenState?) {
        Log.d("TAG", "onScreenStateChanged: ${state.toString()}")

        when (state) {
            is SongsViewModel.ForecastScreenState.SuccessAPIResponse -> handleSuccess(state.data)
            is SongsViewModel.ForecastScreenState.ErrorLoadingFromApi -> handleErrorLoadingFromApi(state.error, state.retry)
            else -> {}
        }

        handleLoadingVisibility(state == SongsViewModel.ForecastScreenState.Loading)
        handleErrorViewsVisibility(state)
    }

    private fun handleErrorViewsVisibility(state: SongsViewModel.ForecastScreenState?) {
        if (state is SongsViewModel.ForecastScreenState.ErrorLoadingFromApi)
            errorViewLayout.visibility = View.VISIBLE
        else
            errorViewLayout.visibility = View.GONE

    }

    private fun handleLoadingVisibility(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun handleSuccess(data: List<Song>) {
        if (data.isEmpty()){
            tvNoSoredData.visibility = View.VISIBLE
        }else{
            tvNoSoredData.visibility = View.INVISIBLE
        }
        adapter.setList(data)
    }

    private fun handleErrorLoadingFromApi(error: Throwable, retry: () -> Unit) {
        btRetry.setOnClickListener {
            retry()
        }
    }

    private fun initRecyclerView() {
        adapter = SongsAdapter(::onAForecastItemClicked)

        rvForecast.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        rvForecast.adapter = adapter
    }

    private fun onAForecastItemClicked(song: Song) {

    }


    private fun initSearchView() {

        searchView.isActivated = true

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.clear()
                songsViewModel.fetchSongsList(searchView.query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                songsViewModel.fetchSongsList(searchView.query.toString())
                return false
            }
        })

    }

}


fun MainActivity.test(){
    

}

