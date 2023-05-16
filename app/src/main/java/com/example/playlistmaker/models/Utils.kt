package com.example.playlistmaker.models

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

object Utils {
    fun hideKeyboard(activity: Activity){
        val imm: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view: View? = activity.currentFocus
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken,0)
        }
    }
}