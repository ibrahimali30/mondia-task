package com.ibrahim.mondia_task.view.extensions

import android.view.View
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.*

fun SearchView.onTextChanged(function: (query: String) -> Unit){
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean { return false }

        override fun onQueryTextChange(newText: String?): Boolean {
            function.invoke(newText.toString())
            return false
        }
    })
}


 fun View.visible() {
    visibility = View.VISIBLE
}