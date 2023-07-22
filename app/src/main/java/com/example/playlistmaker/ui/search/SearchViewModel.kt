package com.example.playlistmaker.ui.search


import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.data.search.models.Tracks
import com.example.playlistmaker.data.search.network.retrofit.ConnectionStatus
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.settings.sharedprefs.HistoryInteractor
import com.example.playlistmaker.domain.sharing.ExternalNavigatorInteractor
import com.example.playlistmaker.ui.player.KEY_INTENT_PLAYER_ACTIVITY
import com.example.playlistmaker.ui.player.PlayerActivity

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val externalNavigator: ExternalNavigatorInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchRequest() }

    private val searchTextMutable = MutableLiveData<String>().apply { }
    val searchText: LiveData<String> = searchTextMutable

    private val searchActivityStateMutable =
        MutableLiveData<StateActivity>().apply { StateActivity.HIDE_ALL }
    val searchActivityState: LiveData<StateActivity> = searchActivityStateMutable

    private val searchTrackListMutable = MutableLiveData<MutableList<Track>>()
    val searchTrackList: LiveData<MutableList<Track>> = searchTrackListMutable

    private val historyTrackListMutable = MutableLiveData<MutableList<Track>>()
    val historyTrackList: LiveData<MutableList<Track>> = historyTrackListMutable

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun searchRequest() {
        if (searchTextMutable.value?.isNotEmpty() == true) {
            val queryText = searchTextMutable.value
            searchActivityStateMutable.value = StateActivity.PROGRESS_BAR
            if (queryText != null) {
                Log.d("my_tag", "Start search query = $queryText")
                initSearch(queryText)
            }
        }
    }

    private fun saveHistoryToSharedPrefs() {
        val tracks: Tracks? =
            historyTrackListMutable.value?.let { Tracks(historyTrackListMutable.value!!.size, it) }
        Log.d("my_tag", "saveHistoryToSharedPrefs size: ${historyTrackList.value?.size}")
        //historyInteractor.saveUserHistory(historyTrackList.value as List<Track>)
        if (tracks != null) {
            historyInteractor.saveUserHistory2(tracks)
        }
    }

    private fun loadHistoryFromSharedPrefs() {
        val historyList: Tracks? = historyInteractor.restoreUserHistory2()
        if (historyList != null) {
            if (historyList.results.isNotEmpty()) {
                historyTrackListMutable.value = historyList.results
                Log.d(
                    "my_tag",
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
        Log.d("my_tag", "checkState")
        if (searchTextMutable.value?.isEmpty() == true || searchTextMutable.value == null) {
            Log.d("my_tag", "checkState2")
            showHistory()
        }
        else {
            showHistory()
        }
    }

    fun clearHistory(){
        Log.d("my_tag", "VM ClearHistory")
        historyTrackListMutable.value?.clear()
        saveHistoryToSharedPrefs()
    }

    private fun showHistory(){
        if (!historyTrackListMutable.value.isNullOrEmpty()) {
            searchActivityStateMutable.value = StateActivity.SHOW_HISTORY
            Log.d("my_tag", "checkHistory State = ${searchActivityStateMutable.value}")
            Log.d("my_tag", "checkHistory Size = ${historyTrackListMutable.value?.size}")
        }
    }

    fun setSearchText(value: String) {
        if (value.isNotEmpty()) {
            searchTextMutable.value = value
            searchDebounce()
        }
    }

    fun addTrackToHistory(item: Track) {
        Log.d("my_tag", "addTrackToHistory: ${item.trackName}")
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
            //historyTrackListMutable.value = MutableList<Track>(1) { item }
            Log.d("my_tag", "addTrackToHistory is null")
        }
        historyTrackListMutable.value = fullList
        Log.d("my_tag", "trackHistorySize: ${historyTrackListMutable.value?.size}")
        for (value in historyTrackListMutable.value!!) {
            Log.d("my_tag", "trackHistoryValues : ${value.trackId}")
        }
        saveHistoryToSharedPrefs()
    }

    @Suppress("UNCHECKED_CAST")
    fun callPlayerActivity(item: Track) {
        externalNavigator.intentCallWithKeySerializable(
            PlayerActivity::class.java as Class<Any>,
            KEY_INTENT_PLAYER_ACTIVITY,
            item
        )
    }

    private fun initSearch(queryText: String) {
        if (queryText.isNotEmpty()) {
            Log.d("my_tag", "Search text = $queryText")
            trackInteractor.searchTracks(queryText, object : TrackInteractor.TracksConsumer {
                override fun consume(foundMovies: List<Track>?, errorMessage: ConnectionStatus) {
                    handler.post {
                        if (foundMovies != null) {
                            Log.d("my_tag", "Found tracks = ${foundMovies.size}")
                            searchTrackListMutable.value?.clear()
                            //searchTrackListMutable.value?.addAll(foundMovies)
                            searchTrackListMutable.value = foundMovies as MutableList<Track>?
                            if (foundMovies.isNotEmpty()) {
                                searchActivityStateMutable.value = StateActivity.SUCCESS
                            } else {
                                searchActivityStateMutable.value = StateActivity.NOT_FOUND
                            }
                        }
                        if (errorMessage != ConnectionStatus.SUCCESS) {
                            searchActivityStateMutable.value = StateActivity.ERROR
                        }
                        Log.d("my_tag", "VM State = ${searchActivityStateMutable.value}")
                        Log.d("my_tag", "VM Value size = ${searchTrackListMutable.value?.size}")
                    }
                }
            })
        }
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    trackInteractor = Creator.provideTrackInteractor(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application),
                    externalNavigator = Creator.provideExternalNavigator(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application),
                    historyInteractor = Creator.provideHistoryInteractor(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
                )
            }
        }

        private const val HISTORY_COUNT = 10
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}