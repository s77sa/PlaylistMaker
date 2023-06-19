package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerState


class PlayerRepositoryImpl : PlayerRepository {
    private var playerState = PlayerState.STATE_DEFAULT
    private lateinit var mediaPlayer: MediaPlayer

    override fun preparePlayer(streamUrl: String) {
        Log.println(Log.INFO, "my_tag", "mediaPlayer prepare")
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(streamUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
            Log.println(Log.INFO, "my_tag", "mediaPlayer End")
        }
    }

    override fun setPlayerState(state: PlayerState) {
        playerState = state
    }

    override fun getPlayerState(): PlayerState{
        return playerState
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun startPlayer() {
        Log.println(Log.INFO, "my_tag", "mediaPlayer Start in impl")
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun pausePlayer() {
        Log.println(Log.INFO, "my_tag", "mediaPlayer Pause")
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }
}