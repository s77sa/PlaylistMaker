package com.example.playlistmaker.domain.sharing.impl

import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.data.sharing.ExternalNavigatorRepository
import com.example.playlistmaker.domain.sharing.ExternalNavigatorInteractor

class ExternalNavigatorInteractorImpl(private val repository: ExternalNavigatorRepository) :
    ExternalNavigatorInteractor {
    override fun intentCall(activityClass: Class<out AppCompatActivity>) {
        repository.intentCall(
            activityClass = activityClass
        )
    }

    override fun intentCallWithKey(
        activityClass: Class<out AppCompatActivity>,
        keyName: String,
        keyValue: String
    ) {
        repository.intentCallWithKey(
            activityClass = activityClass,
            keyName = keyName,
            keyValue = keyValue
        )
    }

    override fun intentCallWithKeySerializable(
        activityClass: Class<Any>,
        keyName: String,
        keyValue: Any
    ) {
        repository.intentCallWithKeySerializable(
            activityClass = activityClass,
            keyName = keyName,
            keyValue = keyValue
        )
    }

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