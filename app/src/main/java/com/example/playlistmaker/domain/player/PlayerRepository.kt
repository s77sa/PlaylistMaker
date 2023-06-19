package com.example.playlistmaker.domain.player

interface PlayerRepository {

    fun preparePlayer(streamUrl: String)

    fun setPlayerState(state: PlayerState)

    fun getPlayerState(): PlayerState

    fun getCurrentPosition(): Int

    fun pausePlayer()

    fun startPlayer()

    fun releasePlayer()
}