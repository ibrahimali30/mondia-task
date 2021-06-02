package com.ibrahim.mondia_task.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object ImageLoader {

    private val downloadList = mutableListOf<ImageStatus>()

    fun loadImage(imageView: ImageView, url: String){
        val imageStatus = getImageStatus(url)
        when (imageStatus.status){
            //if NONE add to downloadList start Downloading Image
            ImageStatus.DownloadStatus.NONE ->{
                imageStatus.setLoading()
                startDownloadingImage(imageView, url)
            }
            ImageStatus.DownloadStatus.DOWNLOADED ->{
                imageView.post {
                    imageView.setImageBitmap(imageStatus.bitmap)
                }
            }
            //todo show loading progress
            ImageStatus.DownloadStatus.DOWNLOADED ->{}
        }
    }

    private fun startDownloadingImage(imageView: ImageView, imageUrl: String) {
        thread {
            try {
                val url = URL(imageUrl)
                val connection = url
                    .openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(input)

                //set status to DownloadStatus.DOWNLOADED and set image bitmap
                getImageStatus(imageUrl).setDownloaded(bitmap)
                loadImage(imageView, imageUrl)

            } catch (e: Exception) {
                e.printStackTrace()
                getImageStatus(imageUrl).status = ImageStatus.DownloadStatus.NONE
            } finally {
//            urlConnection?.disconnect()
            }
        }
    }

    private fun getImageStatus(imageUrl: String): ImageStatus {
        return downloadList.find { it.url == imageUrl }
            //if not included in downloadList add new one with LOADING status
            ?: ImageStatus(imageUrl, null, ImageStatus.DownloadStatus.NONE)
    }


    data class ImageStatus(
        val url: String,
        var bitmap: Bitmap? = null,
        var status: DownloadStatus = DownloadStatus.NONE
    ) {
        fun setDownloaded(v: Bitmap) {
            status = DownloadStatus.DOWNLOADED
            bitmap = v
        }

        fun setLoading() {
            status = DownloadStatus.LOADING
            downloadList.add(this)
        }

        enum class DownloadStatus {
            NONE,
            LOADING,
            DOWNLOADED
        }
    }
}