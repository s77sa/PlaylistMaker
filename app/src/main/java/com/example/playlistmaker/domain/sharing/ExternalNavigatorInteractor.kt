package com.example.playlistmaker.domain.sharing

interface ExternalNavigatorInteractor {
    fun intentOpenLink(link: String)

    fun intentEmail(address: String, subject: String, text: String)

    fun intentSend(text: String)
}