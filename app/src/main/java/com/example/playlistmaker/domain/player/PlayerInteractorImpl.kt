package com.example.playlistmaker.domain.player

import com.example.playlistmaker.data.player.PlayerRepository
import com.example.playlistmaker.data.player.PlayerState

class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {
    override fun preparePlayer(streamUrl: String) {
        repository.preparePlayer(streamUrl)
    }

    override fun setPlayerState(state: PlayerState) {
        repository.setPlayerState(state)
    }

    override fun getPlayerState(): PlayerState {
        return repository.getPlayerState()
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }
}