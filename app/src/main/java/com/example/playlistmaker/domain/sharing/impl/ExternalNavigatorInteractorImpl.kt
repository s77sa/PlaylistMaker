package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.data.sharing.ExternalNavigatorRepository
import com.example.playlistmaker.domain.sharing.ExternalNavigatorInteractor

class ExternalNavigatorInteractorImpl(private val repository: ExternalNavigatorRepository) :
    ExternalNavigatorInteractor {

    override fun intentOpenLink(link: String){
        repository.intentOpenLink(
            link = link
        )
    }

    override fun intentEmail(address: String, subject: String, text: String){
        repository.intentEmail(
            address = address,
            subject = subject,
            text = text
        )
    }

    override fun intentSend(text: String){
        repository.intentSend(
            text = text
        )
    }
}