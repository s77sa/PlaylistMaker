@file:Suppress("unused", "unused")

package com.example.playlistmaker.ui.sharing

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable

class ActivityNavigator(
    private val context: Context,
) {
    fun intentCallWithKeySerializable(
        activityClass: Class<Any>,
        keyName: String,
        keyValue: Any
    ) {
        val intent = Intent(context, activityClass).apply {
            putExtra(keyName, keyValue as Serializable)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun intentCall(
        activityClass: Class<out AppCompatActivity>) {
        val intent = Intent(context, activityClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}