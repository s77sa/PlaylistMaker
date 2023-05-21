package com.example.playlistmaker.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale


object Helpers {
    fun hideKeyboard(activity: Activity){
        val imm: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view: View? = activity.currentFocus
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken,0)
        }
    }
    fun millisToString(millis: Int): String{
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
    }

    fun glideBind(link: String, target: ImageView) {
        Glide.with(target)
            .load(link)
            .placeholder(R.drawable.ic_playlist_stub)
            .centerInside()
            .into(target)
    }
}