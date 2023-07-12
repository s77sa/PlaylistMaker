package com.example.playlistmaker.player.data

import com.example.playlistmaker.player.domain.PlayerState

interface PlayerRepository {

    fun preparePlayer(streamUrl: String)

    fun setPlayerState(state: PlayerState)

    fun getPlayerState(): PlayerState

    fun getCurrentPosition(): Int

    fun pausePlayer()

    fun startPlayer()

    fun releasePlayer()
}