package com.example.playlistmaker.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.BuildConfig
import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.data.search.models.Tracks
import com.example.playlistmaker.data.search.network.retrofit.models.ConnectionStatus
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.settings.sharedprefs.HistoryInteractor
import com.example.playlistmaker.ui.player.KEY_INTENT_PLAYER_ACTIVITY
import com.example.playlistmaker.ui.player.PlayerActivity
import com.example.playlistmaker.ui.sharing.ActivityNavigator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragmentViewModel(
    private val trackInteractor: TrackInteractor,
    private val activityNavigator: ActivityNavigator,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {
    init {
        Log.d(BuildConfig.LOG_TAG, "init - Search ViewModel}")
    }

    companion object {
        private const val HISTORY_COUNT = 10
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var searchJob: Job? = null

    private val searchTextMutable = MutableLiveData<String>().apply { }
    val searchText: LiveData<String> = searchTextMutable

    private val searchActivityStateMutable =
        MutableLiveData<ActivityState>().apply { ActivityState.HIDE_ALL }
    val searchActivityState: LiveData<ActivityState> = searchActivityStateMutable

    private val searchTrackListMutable = MutableLiveData<MutableList<Track>>()
    val searchTrackList: LiveData<MutableList<Track>> = searchTrackListMutable

    private val historyTrackListMutable = MutableLiveData<MutableList<Track>>()
    val historyTrackList: LiveData<MutableList<Track>> = historyTrackListMutable

    private fun searchDebounce() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest()
        }
    }

    fun setSearchText(value: String) {
        searchTextMutable.value = value
        if (value.isNotEmpty()) {
            searchDebounce()
        }
    }

    fun searchRequest() {
        if (searchTextMutable.value?.isNotEmpty() == true) {
            val queryText = searchTextMutable.value
            searchActivityStateMutable.value = ActivityState.PROGRESS_BAR
            if (queryText != null) {
                Log.d(BuildConfig.LOG_TAG, "Start search query = $queryText")
                initSearch(queryText)
            }
        }
    }

    private fun saveHistoryToSharedPrefs() {
        val tracks: Tracks? =
            historyTrackListMutable.value?.let { Tracks(historyTrackListMutable.value!!.size, it) }
        Log.d(BuildConfig.LOG_TAG, "saveHistoryToSharedPrefs size: ${historyTrackList.value?.size}")
        if (tracks != null) {
            historyInteractor.saveUserHistoryTracks(tracks)
        }
    }

    private fun loadHistoryFromSharedPrefs() {
        val historyList: Tracks? = historyInteractor.restoreUserHistoryTracks()
        if (historyList != null) {
            if (historyList.results.isNotEmpty()) {
                historyTrackListMutable.value = historyList.results
                Log.d(
                    BuildConfig.LOG_TAG,
                    "loadHistoryFromSharedPrefs size: ${historyTrackListMutable.value!!.size}"
                )
            }
        }
    }

    fun clearSearchTrackList() {
        searchTrackListMutable.value?.clear()
    }

    fun checkState() {
        loadHistoryFromSharedPrefs()
        Log.d(BuildConfig.LOG_TAG, "checkState")
        if (searchTextMutable.value?.isEmpty() == true || searchTextMutable.value == null) {
            Log.d(BuildConfig.LOG_TAG, "checkState2")
            showHistory()
        } else {
            showHistory()
        }
    }

    fun clearHistory() {
        Log.d(BuildConfig.LOG_TAG, "VM ClearHistory")
        historyTrackListMutable.value?.clear()
        saveHistoryToSharedPrefs()
    }

    private fun showHistory() {
        if (!historyTrackListMutable.value.isNullOrEmpty()) {
            searchActivityStateMutable.value = ActivityState.HISTORY_RESULT
            Log.d(BuildConfig.LOG_TAG, "checkHistory State = ${searchActivityStateMutable.value}")
            Log.d(BuildConfig.LOG_TAG, "checkHistory Size = ${historyTrackListMutable.value?.size}")
        }
    }

    fun addTrackToHistory(item: Track) {
        Log.d(BuildConfig.LOG_TAG, "addTrackToHistory: ${item.trackName}")
        val fullList: MutableList<Track> = mutableListOf()
        if (!historyTrackListMutable.value.isNullOrEmpty()) {
            fullList.addAll(historyTrackListMutable.value!!)
            var exist: Track? = null
            for (history in fullList) {
                if (history.trackId == item.trackId) {
                    exist = history
                }
            }
            if (exist != null) {
                fullList.remove(exist)
                fullList.add(0, exist)
            } else {
                if (fullList.size >= HISTORY_COUNT) fullList.removeAt(HISTORY_COUNT - 1)
                fullList.add(0, item)
            }
            historyTrackListMutable.value = fullList
        } else {
            fullList.addAll(listOf(item))
            Log.d(BuildConfig.LOG_TAG, "addTrackToHistory is null")
        }
        historyTrackListMutable.value = fullList
        Log.d(BuildConfig.LOG_TAG, "trackHistorySize: ${historyTrackListMutable.value?.size}")
        for (value in historyTrackListMutable.value!!) {
            Log.d(BuildConfig.LOG_TAG, "trackHistoryValues : ${value.trackId}")
        }
        saveHistoryToSharedPrefs()
    }

    @Suppress("UNCHECKED_CAST")
    fun callPlayerActivity(item: Track) {
        activityNavigator.intentCallWithKeySerializable(
            PlayerActivity::class.java as Class<Any>,
            KEY_INTENT_PLAYER_ACTIVITY,
            item
        )
    }

    private fun initSearch(queryText: String) {
        if (queryText.isNotEmpty()) {
            viewModelScope.launch {
                trackInteractor
                    .searchTracks(queryText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundMovies: List<Track>?, errorMessage: ConnectionStatus) {
        when (errorMessage) {
            ConnectionStatus.SUCCESS -> {
                if (foundMovies != null) {
                    searchTrackListMutable.value?.clear()
                    searchTrackListMutable.value = foundMovies as MutableList<Track>?
                    if (foundMovies.isNotEmpty()) {
                        searchActivityStateMutable.value = ActivityState.SEARCH_RESULT
                    } else {
                        searchActivityStateMutable.value = ActivityState.NOT_FOUND
                    }
                }
            }

            ConnectionStatus.CONNECTION_ERROR -> {
                searchActivityStateMutable.value = ActivityState.NO_INTERNET
            }

            else -> {
                searchActivityStateMutable.value = ActivityState.NOT_FOUND
            }
        }
    }
}