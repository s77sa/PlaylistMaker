package com.example.playlistmaker.models

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
//import android.content.ContextWrapper
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.R


object Intents2 {
    fun intentCall(activity: Activity, activityClass: Class<out AppCompatActivity>){
        val intent = Intent(activity, activityClass)
        activity.startActivity(intent)
    }

    fun intentOpenLink(activity: Activity, link: String) {
        val shareIntent = Intent(Intent.ACTION_VIEW)
        shareIntent.data = Uri.parse(link)
        try {
            activity.startActivity(shareIntent)
        } catch (e: Exception) {
            Toast.makeText(activity,  R.string.error_no_browser, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("QueryPermissionsNeeded", "SuspiciousIndentation")
    fun intentEmail(activity: Activity, address: String, subject: String, text: String)
    {
        val shareIntent = Intent(Intent.ACTION_SENDTO)
        shareIntent.data = Uri.parse("mailto:")
        shareIntent.putExtra(Intent.EXTRA_EMAIL, address)
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,subject)
        val putExtra = shareIntent.putExtra(Intent.EXTRA_TEXT, text)
            if (putExtra.resolveActivity(activity.packageManager) != null) {
                activity.startActivity(shareIntent)
            }
            else {
                Toast.makeText(activity, R.string.error_no_email, Toast.LENGTH_SHORT).show()
            }
    }

    fun intentSend(activity: Activity, text: String){
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        activity.startActivity(shareIntent)
    }
}