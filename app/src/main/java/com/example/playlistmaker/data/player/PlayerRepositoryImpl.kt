package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.BuildConfig

class PlayerRepositoryImpl(
    private var mediaPlayer: MediaPlayer
) : PlayerRepository {
    private var playerState = PlayerState.STATE_DEFAULT


    override fun preparePlayer(streamUrl: String?) {
        Log.d(BuildConfig.LOG_TAG, "mediaPlayer prepare")
        if (!streamUrl.isNullOrEmpty()) {
            Log.d(BuildConfig.LOG_TAG, "mediaPlayer url = $streamUrl")
            mediaPlayer.reset()
            mediaPlayer.setDataSource(streamUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState = PlayerState.STATE_PREPARED
            }
            mediaPlayer.setOnCompletionListener {
                playerState = PlayerState.STATE_PREPARED
                Log.d(BuildConfig.LOG_TAG, "mediaPlayer End")
            }
        } else (
                Log.d(BuildConfig.LOG_TAG, "Input url empty or null")
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
        Log.d(BuildConfig.LOG_TAG, "mediaPlayer Start in impl")
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun pausePlayer() {
        Log.d(BuildConfig.LOG_TAG, "mediaPlayer Pause")
        if (playerState == PlayerState.STATE_PLAYING) {
            mediaPlayer.pause()
            playerState = PlayerState.STATE_PAUSED
        }
    }
}