package com.example.playlistmaker.domain.player

import com.example.playlistmaker.data.player.PlayerState

interface PlayerInteractor {
    fun preparePlayer(streamUrl: String)

    fun setPlayerState(state: PlayerState)

    fun getPlayerState(): PlayerState

    fun getCurrentPosition(): Int

    fun pausePlayer()

    fun startPlayer()
}