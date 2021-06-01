package com.ibrahim.mondia_task.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object ImageLoader {

    val downloadList = mutableListOf<ImageStatus>()

    data class ImageStatus(
        val url: String,
        var bitmap: Bitmap? = null,
        var status: DownloadStatus = DownloadStatus.LOADING
    ){
        fun setDownloaded(v: Bitmap) {
            status = DownloadStatus.DOWNLOADED
            bitmap = v
        }


        enum class DownloadStatus{
            LOADING,
            DOWNLOADED
        }
    }

    fun loadImage(testimage: ImageView, url: String){
        val imageStatus = getImageStatus(url)
        when (imageStatus?.status){
            ImageStatus.DownloadStatus.LOADING ->{
                downloadList.add(ImageStatus(url))
                startDownloadingImage(testimage, url)
            }
            ImageStatus.DownloadStatus.DOWNLOADED ->{
                testimage.post {
                    testimage.setImageBitmap(imageStatus.bitmap)
                }
            }

        }

    }

    private fun startDownloadingImage(testimage: ImageView, imageUrl: String) {

        thread {
            try {
                val url = URL(imageUrl)
                val connection = url
                    .openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                val v = BitmapFactory.decodeStream(input)
                getImageStatus(imageUrl)?.setDownloaded(v)
                loadImage(testimage, imageUrl)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
//            urlConnection?.disconnect()
            }
        }
    }

    private fun getImageStatus(imageUrl: String): ImageStatus {
        return downloadList.find { it.url == imageUrl }?: ImageStatus(imageUrl)
    }

}