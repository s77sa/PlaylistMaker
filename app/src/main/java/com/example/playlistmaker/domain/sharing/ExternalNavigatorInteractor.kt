package com.example.playlistmaker.domain.sharing

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

interface ExternalNavigatorInteractor {
    fun intentCall(activityClass: Class<out AppCompatActivity>)

    fun intentCallWithKey(
        activityClass: Class<out AppCompatActivity>,
        keyName: String,
        keyValue: String
    )

    fun intentCallWithKeySerializable(
        activityClass: Class<Any>,
        keyName: String,
        keyValue: Any
    )

    fun intentOpenLink(link: String)

    fun intentEmail(address: String, subject: String, text: String)

    fun intentSend(text: String)
}