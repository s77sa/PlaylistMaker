package com.example.playlistmaker.ui.player

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converters.FavoritesTrackDbConvertor
import com.example.playlistmaker.data.db.entity.FavoritesTrackEntity
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.data.player.PlayerState
import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.ui.search.SearchFragmentViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: Track,
    private var mediaPlayerInteractor: PlayerInteractor,
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: FavoritesTrackDbConvertor
) : ViewModel() {
    init {
        Log.d(TAG, "init - PlayerViewModel - ${track.trackName}")
        saveFavoriteTrackJob(track)
    }

    private val valuesLiveData = MutableLiveData<Track>()
    fun getLoadingValues(): LiveData<Track> = valuesLiveData

    private val isPlayingLiveData = MutableLiveData(false)
    fun getIsPlayingLiveData(): LiveData<Boolean> = isPlayingLiveData

    private val playingPositionLiveData = MutableLiveData<String>()
    fun getPlayingPositionLiveData(): LiveData<String> = playingPositionLiveData

    private var coroutineJob: Job? = null
    private var timerJob: Job? = null

    private fun saveFavoriteTrackJob(track: Track) {
        coroutineJob?.cancel()
        coroutineJob = viewModelScope.launch {
            saveFavoriteTrack(track)
            Log.d(TAG, "Saved to Favorites: ${track.trackName}")
        }
    }
    private suspend fun saveFavoriteTrack(track: Track){
        val trackEntity: FavoritesTrackEntity = trackDbConvertor.map(track)
        appDatabase.favoritesTrackDao().insertFavoritesTrack(trackEntity)
    }

    fun saveValues() {
        valuesLiveData.value = track
    }

    private fun startTimer() {
        Log.d(TAG, "startJob")
        timerJob = viewModelScope.launch {
            while (mediaPlayerInteractor.getPlayerState() == PlayerState.STATE_PLAYING) {
                delay(REFRESH_TIME_HEADER_DELAY_MILLIS)
                setPlayerState()
            }
        }
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
                startTimer()
            }

            else -> {}
        }
        setPlayerState()
    }

    fun preparePlayer() {
        track.previewUrl?.let { mediaPlayerInteractor.preparePlayer(it) }
    }

    private fun startPlayer() {
        mediaPlayerInteractor.startPlayer()
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
    }

    private fun setPlayerState() {
        when (mediaPlayerInteractor.getPlayerState()) {
            PlayerState.STATE_PLAYING -> {
                isPlayingLiveData.value = true
                setTimeToTrackTime(mediaPlayerInteractor.getCurrentPosition())
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

    }

    companion object {
        private const val REFRESH_TIME_HEADER_DELAY_MILLIS: Long = 300L
        private val TAG = PlayerActivity::class.simpleName
    }
}