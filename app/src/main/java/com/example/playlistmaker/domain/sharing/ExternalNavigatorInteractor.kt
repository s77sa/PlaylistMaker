package com.example.playlistmaker.domain.sharing

interface ExternalNavigatorInteractor {
    fun intentOpenLink(link: String): String?

    fun intentEmail(address: String, subject: String, text: String): String?

    fun intentSend(text: String): String?
}