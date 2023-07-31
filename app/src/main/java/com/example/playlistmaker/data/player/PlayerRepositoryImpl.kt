package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import android.util.Log

class PlayerRepositoryImpl(
    private var mediaPlayer: MediaPlayer
) : PlayerRepository {
    private var playerState = PlayerState.STATE_DEFAULT


    override fun preparePlayer(streamUrl: String?) {
        Log.println(Log.INFO, "my_tag", "mediaPlayer prepare")
        if (!streamUrl.isNullOrEmpty()) {
            mediaPlayer.setDataSource(streamUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState = PlayerState.STATE_PREPARED
            }
            mediaPlayer.setOnCompletionListener {
                playerState = PlayerState.STATE_PREPARED
                Log.d("my_tag", "mediaPlayer End")
            }
        } else (
                Log.d("my_tag", "Input url empty or null")
                )
    }

    override fun setPlayerState(state: PlayerState) {
        playerState = state
    }

    override fun getPlayerState(): PlayerState {
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
        if (playerState == PlayerState.STATE_PLAYING) {
            mediaPlayer.pause()
            playerState = PlayerState.STATE_PAUSED
        }
    }

    override fun releasePlayer() {
        if (playerState != PlayerState.STATE_DEFAULT) {
            mediaPlayer.release()
        }
    }
}