package com.example.playlistmaker.data.sharing.impl

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.data.sharing.ExternalNavigatorRepository
import java.io.Serializable


class ExternalNavigatorRepositoryImpl(private val context: Context) : ExternalNavigatorRepository{

    override fun intentCall(activityClass: Class<out AppCompatActivity>) {
        val intent = Intent(context, activityClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun intentCallWithKey(
        activityClass: Class<out AppCompatActivity>,
        keyName: String,
        keyValue: String
    ) {
        val intent = Intent(context, activityClass).apply {
            putExtra(keyName, keyValue)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun intentCallWithKeySerializable(
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

    override fun intentOpenLink(link: String) {
        val shareIntent = Intent(Intent.ACTION_VIEW)
        shareIntent.data = Uri.parse(link)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    @SuppressLint("QueryPermissionsNeeded", "SuspiciousIndentation")
    override fun intentEmail(address: String, subject: String, text: String) {
        val shareIntent = Intent(Intent.ACTION_SENDTO)
        shareIntent.data = Uri.parse("mailto:")
        shareIntent.putExtra(Intent.EXTRA_EMAIL, address)
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun intentSend(text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }
}