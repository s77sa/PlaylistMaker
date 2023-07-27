package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.data.sharing.ExternalNavigatorRepository
import com.example.playlistmaker.domain.sharing.ExternalNavigatorInteractor

class ExternalNavigatorInteractorImpl(private val repository: ExternalNavigatorRepository) :
    ExternalNavigatorInteractor {

    override fun intentOpenLink(link: String): String? {
        return repository.intentOpenLink(
            link = link
        )
    }

    override fun intentEmail(address: String, subject: String, text: String): String? {
        return repository.intentEmail(
            address = address,
            subject = subject,
            text = text
        )
    }

    override fun intentSend(text: String): String? {
        return repository.intentSend(
            text = text
        )
    }
}