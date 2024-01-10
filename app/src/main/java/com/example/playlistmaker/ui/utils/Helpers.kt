package com.example.playlistmaker.ui.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale


object Helpers {
    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view: View? = activity.currentFocus
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun millisToString(millis: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
    }

    fun glideBind(link: String, target: ImageView) {
        Glide.with(target)
            .load(link)
            .placeholder(R.drawable.ic_playlist_stub)
            .centerInside()
            .into(target)
    }

    fun tracksDeclension(context: Context, count: Int): String {
        var text: String =
            ContextCompat.getString(context, R.string.tracks_text)
        val lastInt = (count.toString()[count.toString().length - 1]).digitToInt()
        if (Locale.getDefault().language == Locale("ru").language) {
            text = when (lastInt) {
                1 -> {
                    text.removeRange(4, text.length)
                }

                in 2..4 -> {
                    text.removeRange(5, text.length)
                }

                else -> {
                    text.removeRange(4, text.length - 2)
                }
            }
        } else {
            if (lastInt == 1) {
                text = text.removeRange(5, text.length)
            }
        }
        return text
    }

    fun minutesDeclension(context: Context, minutes: Int): String {
        var text: String = ContextCompat.getString(context, R.string.minutes_text)
        if (Locale.getDefault().language == Locale("ru").language) {
            text = when {
                minutes % 10 == 1 && minutes % 100 != 11 -> {
                    text.removeRange(5, text.length)
                }// минута
                minutes % 10 in 2..4 && minutes % 100 !in 12..14 -> {
                    text.removeRange(5, 6)
                }// минуты
                else -> {
                    text.removeRange(5, text.length)
                }// минут
            }
        } else {
            text = when (minutes) {
                1 -> {
                    text.removeRange(5, text.length)
                }// minute
                else -> {
                    text
                }// minutes
            }
        }

        return text
    }
}