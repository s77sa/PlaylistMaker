package com.example.playlistmaker.player.domain

interface PlayerInteractor {
    fun preparePlayer(streamUrl: String)

    fun setPlayerState(state: PlayerState)

    fun getPlayerState(): PlayerState

    fun getCurrentPosition(): Int

    fun pausePlayer()

    fun startPlayer()

    fun releasePlayer()
}