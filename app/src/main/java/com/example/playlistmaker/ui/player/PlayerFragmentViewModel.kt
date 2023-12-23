package com.example.playlistmaker.ui.player

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converters.FavoritesTrackDbConvertor
import com.example.playlistmaker.data.db.converters.TracksInPlaylistDbConvertor
import com.example.playlistmaker.data.db.entity.FavoritesTrackEntity
import com.example.playlistmaker.data.db.entity.TracksInPlaylistsEntity
import com.example.playlistmaker.data.models.Track
import com.example.playlistmaker.data.player.PlayerState
import com.example.playlistmaker.domain.db.DbInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.player.PlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragmentViewModel(
    private val track: Track,
    private var mediaPlayerInteractor: PlayerInteractor,
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: FavoritesTrackDbConvertor,
    private val tracksInPlaylistDbConvertor: TracksInPlaylistDbConvertor,
    private val dbInteractor: DbInteractor
) : ViewModel() {
    init {
        Log.d(TAG, "init - PlayerViewModel - ${track.trackName}")
    }

    private var addedTrackInPlaylistMutable = MutableLiveData<MessageStatus>(MessageStatus.NOTHIN)
    fun addedTrackInPlaylist(): LiveData<MessageStatus> = addedTrackInPlaylistMutable

    private var playlistListMutable = MutableLiveData<List<Playlist>>()
    fun getPlaylistList(): LiveData<List<Playlist>> = playlistListMutable

    private val isFavoritesMutableLiveData = MutableLiveData<Boolean>()
    fun getIsFavorites(): LiveData<Boolean> = isFavoritesMutableLiveData

    private val valuesLiveData = MutableLiveData<Track>()
    fun getLoadingValues(): LiveData<Track> = valuesLiveData

    private val isPlayingLiveData = MutableLiveData(false)
    fun getIsPlayingLiveData(): LiveData<Boolean> = isPlayingLiveData

    private val playingPositionLiveData = MutableLiveData<String>()
    fun getPlayingPositionLiveData(): LiveData<String> = playingPositionLiveData

    private var coroutineJob: Job? = null
    private var timerJob: Job? = null

    fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        val playlistId = playlist.id
        coroutineJob?.cancel()
        coroutineJob = viewModelScope.launch {
            if ((dbInteractor.checkTrackInPlaylist(playlist.id, track.trackId!!) > 0)) {
                addedTrackInPlaylistMutable.value = MessageStatus.TRACK_EXIST
            } else {
                addTrackToPlaylist(track, playlistId)
                addedTrackInPlaylistMutable.value = MessageStatus.TRACK_ADDED
            }
        }
    }

    private suspend fun addTrackToPlaylist(track: Track, playlistId: Int) {
        val trackInPlaylistEntity: TracksInPlaylistsEntity =
            tracksInPlaylistDbConvertor.map(track, playlistId)
        appDatabase.tracksInPlaylistDao().insertTrackInPlaylist(trackInPlaylistEntity)
    }

    fun getPlaylists() {
        viewModelScope.launch {
            dbInteractor
                .playlists()
                .collect { playlist -> playlistResult(playlist) }
        }
    }

    private suspend fun playlistResult(playlists: List<Playlist>) {
        for (playlist in playlists) {
            playlist.tracksCount = dbInteractor.countTracksInPlaylists(playlist.id)
        }
        playlistListMutable.value = playlists
    }

    fun changeTrackStatus() {
        if (getIsFavorites().value == true) {
            deleteFavoriteTrackJob()
        } else {
            saveFavoriteTrackJob()
        }
    }

    private fun deleteFavoriteTrackJob() {
        coroutineJob?.cancel()
        coroutineJob = viewModelScope.launch {
            deleteFavoriteTrack()
            Log.d(TAG, "Drop from Favorites: ${track.trackName}")
            isFavoritesMutableLiveData.value = checkFavoriteTrack()
        }
    }

    private fun saveFavoriteTrackJob() {
        coroutineJob?.cancel()
        coroutineJob = viewModelScope.launch {
            saveFavoriteTrack()
            Log.d(TAG, "Saved to Favorites: ${track.trackName}")
            isFavoritesMutableLiveData.value = checkFavoriteTrack()
        }
    }

    fun checkFavoriteTrackJob() {
        coroutineJob?.cancel()
        coroutineJob = viewModelScope.launch {
            val result = checkFavoriteTrack()
            Log.d(TAG, "Check Favorites: $result")
            isFavoritesMutableLiveData.value = result
        }
    }

    private suspend fun checkFavoriteTrack(): Boolean {
        val trackEntity: FavoritesTrackEntity = trackDbConvertor.map(track)
        return appDatabase.favoritesTrackDao().checkFavoritesTrack(trackEntity.trackId) > 0
    }

    private suspend fun deleteFavoriteTrack() {
        val trackEntity: FavoritesTrackEntity = trackDbConvertor.map(track)
        appDatabase.favoritesTrackDao().deleteFavoritesTrack(trackEntity.trackId)
    }

    private suspend fun saveFavoriteTrack() {
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
        private val TAG = PlayerFragmentViewModel::class.simpleName
    }
}