package com.example.playlistmaker.data.sharing

import androidx.appcompat.app.AppCompatActivity

interface ExternalNavigatorRepository {

    fun intentCall(
//        context: Context,
        activityClass: Class<out AppCompatActivity>)

    fun intentCallWithKey(
//        context: Context,
        activityClass: Class<out AppCompatActivity>,
        keyName: String,
        keyValue: String
    )

    fun intentCallWithKeySerializable(
//        context: Context,
        activityClass: Class<Any>,
        keyName: String,
        keyValue: Any
    )

    fun intentOpenLink(
//        context: Context,
        link: String)

    fun intentEmail(
//        context: Context,
        address: String,
        subject: String,
        text: String)

    fun intentSend(
//        context: Context,
        text: String)
}