package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.player.PlayerState

interface PlayerInteractor {
    fun preparePlayer(streamUrl: String)

    fun setPlayerState(state: PlayerState)

    fun getPlayerState(): PlayerState

    fun getCurrentPosition(): Int

    fun pausePlayer()

    fun startPlayer()

    fun releasePlayer()
}