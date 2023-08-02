package com.example.playlistmaker.data.sharing

interface ExternalNavigatorRepository {

    fun intentOpenLink(
        link: String
    ): String?

    fun intentEmail(
        address: String,
        subject: String,
        text: String
    ): String?

    fun intentSend(
        text: String
    ): String?
}