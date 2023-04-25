package com.example.playlistmaker.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.TrackData

class Utils {
    // Скрытие клавиатуры. Убрал сюда, так как может пригодиться в других активити
    fun hideKeyboard(activity: Activity){
        val imm: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = activity.currentFocus
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken,0)
        }
    }
}