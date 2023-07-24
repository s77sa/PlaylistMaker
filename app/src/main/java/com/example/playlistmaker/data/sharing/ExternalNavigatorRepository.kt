package com.example.playlistmaker.data.sharing

interface ExternalNavigatorRepository {

    fun intentOpenLink(
        link: String)

    fun intentEmail(
        address: String,
        subject: String,
        text: String)

    fun intentSend(
        text: String)
}