package com.example.playlistmaker.ui.player

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.data.player.PlayerState
import com.example.playlistmaker.data.search.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: Track,
    private var mediaPlayerInteractor: PlayerInteractor
) : ViewModel() {
    init {
        Log.d("my_tag", "init - PlayerViewModel - ${track.trackName}")
    }

    private val valuesLiveData = MutableLiveData<Track>()
    fun getLoadingValues(): LiveData<Track> = valuesLiveData

    private val isPlayingLiveData = MutableLiveData(false)
    fun getIsPlayingLiveData(): LiveData<Boolean> = isPlayingLiveData

    private val playingPositionLiveData = MutableLiveData<String>()
    fun getPlayingPositionLiveData(): LiveData<String> = playingPositionLiveData

    private var mainThreadHandler: Handler? = null

    fun saveValues() {
        valuesLiveData.value = track
    }

    private fun startThread() {
        Log.println(Log.INFO, "my_tag", "startThread")
        mainThreadHandler?.post(
            object : Runnable {
                override fun run() {
                    if (mediaPlayerInteractor.getPlayerState() == PlayerState.STATE_PLAYING) {
                        setTimeToTrackTime(mediaPlayerInteractor.getCurrentPosition())
                        mainThreadHandler?.postDelayed(this, REFRESH_TIME_HEADER_DELAY_MILLIS)
                    } else {
                        setPlayerState()
                    }
                }
            }
        )
    }

    private fun setTimeToTrackTime(position: Int = 0) {
        saveCurrentPositionToLiveData(
            SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(position)
        )
    }

    private fun saveCurrentPositionToLiveData(value: String) {
        playingPositionLiveData.value = value
    }

    fun playbackControl() {
        when (mediaPlayerInteractor.getPlayerState()) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
                startThread()
            }

            else -> {}
        }
        setPlayerState()
    }

    fun preparePlayer() {
        mainThreadHandler = Handler(Looper.getMainLooper())
        track.previewUrl?.let { mediaPlayerInteractor.preparePlayer(it) }
    }

    private fun startPlayer() {
        mediaPlayerInteractor.startPlayer()
        startThread()
    }

    private fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
    }

    fun setPlayerState() {
        when (mediaPlayerInteractor.getPlayerState()) {
            PlayerState.STATE_PLAYING -> {
                isPlayingLiveData.value = true
            }

            PlayerState.STATE_PAUSED -> {
                isPlayingLiveData.value = false
            }

            else -> { // PREPARED STATE
                isPlayingLiveData.value = false
                setTimeToTrackTime()
            }
        }
    }

    fun onPause() {
        mediaPlayerInteractor.pausePlayer()
    }

    fun onDestroy() {
        mainThreadHandler?.removeCallbacksAndMessages(null)
    }

    companion object {
        private const val REFRESH_TIME_HEADER_DELAY_MILLIS: Long = 330L
    }
}