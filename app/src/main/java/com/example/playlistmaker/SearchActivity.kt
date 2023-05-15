package com.example.playlistmaker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.BoringLayout
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.models.HISTORY_COUNT
import com.example.playlistmaker.models.PLAY_LIST_PREFERENCES
import com.example.playlistmaker.models.Preferences
import com.example.playlistmaker.retrofit.Track
import com.example.playlistmaker.retrofit.Tracks
import com.example.playlistmaker.retrofit.TracksApi
import com.example.playlistmaker.retrofit.TracksRetrofit
import com.example.playlistmaker.models.Utils
import com.example.playlistmaker.recyclerview.SearchAdapter
//import com.example.playlistmaker.recyclerview.SearchAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var tracksApi: TracksApi
    private lateinit var retrofit: TracksRetrofit
    private val interceptor: Boolean = true
    private val utils: Utils = Utils()
    private lateinit var inputEditText: EditText
    private lateinit var rvItems: RecyclerView
    private lateinit var rvHistory: RecyclerView
    private lateinit var rvSearchAdapter: SearchAdapter
    private lateinit var rvHistoryAdapter: SearchAdapter
    private lateinit var buttonRefresh: Button
    private lateinit var buttonClear: ImageView
    private lateinit var buttonClearHistory: Button
    private lateinit var layoutIsEmpty: LinearLayout
    private lateinit var layoutNoInternet: LinearLayout
    private lateinit var layoutHistory: LinearLayout
    private var searchTrackList: MutableList<Track> = mutableListOf()
    private var historyTrackList: MutableList<Track> = mutableListOf()
    private var searchText = ""
    private lateinit var preferences: Preferences


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        Log.println(Log.INFO, "my_tag", "onCreate_Search")
        inputEditText = findViewById<EditText>(R.id.et_search)
        buttonClear = findViewById<ImageView>(R.id.iv_search_clear)
        rvItems = findViewById<RecyclerView>(R.id.rv_Search)
        buttonRefresh = findViewById<Button>(R.id.btn_search_refresh)
        layoutIsEmpty = findViewById<LinearLayout>(R.id.layout_is_empty)
        layoutNoInternet = findViewById<LinearLayout>(R.id.layout_no_internet)
        layoutHistory = findViewById<LinearLayout>(R.id.layout_history)
        buttonClearHistory = findViewById<Button>(R.id.btn_clear_history)
        rvHistory = findViewById<RecyclerView>(R.id.rv_history)
        textWatcherInit()
        retrofitInit(getString(R.string.searchBaseUrl))
        rvSearchAdapter = SearchAdapter(searchTrackList) // Адаптер для RV
        rvHistoryAdapter = SearchAdapter(historyTrackList)
        rvItems.adapter = rvSearchAdapter
        rvHistory.adapter = rvHistoryAdapter
        onClickListenersInit()
        queryInput(inputEditText)
        preferences = Preferences(getSharedPreferences(PLAY_LIST_PREFERENCES, MODE_PRIVATE))
    }

    override fun onResume() {
        super.onResume()
        loadHistoryFromSharedPrefs()
        showHistory()
    }

    override fun onPause() {
        super.onPause()
        saveHistoryToSharedPrefs()
    }

    private fun saveHistoryToSharedPrefs() {
        preferences.saveUserHistory(Tracks(historyTrackList.size, historyTrackList))
    }

    private fun loadHistoryFromSharedPrefs() {
        val trackList: MutableList<Track>? = preferences.restoreUserHistory()?.results
        if (trackList != null) historyTrackList.addAll(trackList)

    }

    private fun clearHistory() {
        if (historyTrackList.size > 0) historyTrackList.clear()
        showInvisibleLayout(State.HIDE_ALL)
    }

    private fun showHistory() {
        Log.println(Log.INFO, "my_tag", "showHistory: ${historyTrackList.size}")
        if (historyTrackList.size > 0) {
            updateHistoryRecycleView()
            showInvisibleLayout(State.SHOW_HISTORY)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateHistoryRecycleView() {
        Log.println(Log.INFO, "my_tag", "updateHistoryRecycleView")
        rvHistory.adapter?.notifyDataSetChanged()
    }

    private fun onFocusListenerSearchInit() {
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            Log.println(Log.INFO, "my_tag", "onFocusListenerInit")
            if (hasFocus) {
                showHistory()
            }
        }
    }

    private fun addTrackToHistory(item: Track) {
        var exist: Track? = null
        for (history in historyTrackList) {
            if (history.trackId == item.trackId) {
                exist = history
            }
        }
        if(exist != null) {
            historyTrackList.remove(exist)
            historyTrackList.add(0, exist)
        }
        else{
            if (historyTrackList.size >= HISTORY_COUNT) historyTrackList.removeAt(HISTORY_COUNT - 1)
            historyTrackList.add(0, item)
        }
        Log.println(Log.INFO, "my_tag", "trackHistorySize: ${historyTrackList.size}")
    }

    private fun queryInput(editText: EditText) {
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Toast.makeText(applicationContext, "Search run", Toast.LENGTH_SHORT).show()
                retrofitCall(editText.text.toString())
                showInvisibleLayout(State.HIDE_ALL)
            }
            false
        }
    }

    private fun retrofitCall(text: String) {
        val call = tracksApi.searchTracks(text)
        call.enqueue(object : Callback<Tracks> {
            override fun onResponse(call: Call<Tracks>, response: Response<Tracks>) {
                Log.println(Log.INFO, "my_tag", "onResponse Code: ${response.code()}")
                if (response.code() == 200) {
                    Log.println(Log.INFO, "my_tag", "TrackCount: ${response.body()?.resultCount}")
                    if (response.body()?.results?.isNotEmpty() == true) {
                        addSearchResultToRecycle(response.body()?.results!!)
                        showInvisibleLayout(State.SUCCESS)
                    } else {
                        showInvisibleLayout(State.NOT_FOUND)
                    }
                }

            }

            override fun onFailure(call: Call<Tracks>, t: Throwable) {
                Log.println(Log.INFO, "my_tag", "onFailure")
                showInvisibleLayout(State.ERROR)
            }
        })
    }

    private fun retrofitInit(baseUrl: String) {
        retrofit = TracksRetrofit(baseUrl, interceptor)
        tracksApi = retrofit.retrofitInit()
    }

    private fun searchRefresh() {
        showInvisibleLayout()
        retrofitCall(searchText)
    }

    private fun showInvisibleLayout(state: State = State.HIDE_ALL) {
        rvItems.visibility = View.GONE
        layoutNoInternet.visibility = View.GONE
        layoutIsEmpty.visibility = View.GONE
        layoutHistory.visibility = View.GONE
        when (state) {
            State.SUCCESS -> rvItems.visibility = View.VISIBLE
            State.NOT_FOUND -> layoutIsEmpty.visibility = View.VISIBLE
            State.ERROR -> layoutNoInternet.visibility = View.VISIBLE
            State.SHOW_HISTORY -> layoutHistory.visibility = View.VISIBLE
            else -> {}
        }
    }

    // Заполнение RecyclerView
    private fun addSearchResultToRecycle(list: MutableList<Track>) {
        Log.println(Log.INFO, "my_tag", "searchToRecycle2")
        val start = list.size
        searchTrackList.addAll(list)
        rvItems.adapter?.notifyItemRangeInserted(start, searchTrackList.size)
        Log.println(Log.INFO, "my_tag", "searchToRecycle rvList: ${searchTrackList.size}")
    }

    // Очистка RecycleView
    private fun clearRecycle() {
        Log.println(Log.INFO, "my_tag", "clearRecycle")
        val count = searchTrackList.size
        searchTrackList.clear()
        rvItems.adapter?.notifyItemRangeRemoved(0, count)
        Log.println(Log.INFO, "my_tag", "rvlist: ${searchTrackList.size}")
    }

    private fun textWatcherInit() { // Инициализация TextWatcher

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonClear.visibility = clearButtonVisibility(s)
                showHistory()
                Log.println(Log.INFO, "my_tag", "onTextChanged")
            }

            override fun afterTextChanged(s: Editable?) {
                // Запись вводимого текста в глобальную переменную
                searchText = inputEditText.text.toString()
                Log.println(Log.INFO, "my_tag", "afterTextChanged")

                if (searchText.isNotEmpty()) {
                    //addSearchResultToRecycle() // Заполнение RecyclerView
                }
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher) // init text watcher
    }

    private fun clearButtonVisibility(s: CharSequence?): Int { // Изменение прозрачности кнопки (крестик)
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun onClickListenersInit() {
        onClickReturn()
        onClickSearchClear()
        onClickClear()
        onClickSearch()
        onClickRefresh()
        onClickRecyclerViewSearchItem()
        onFocusListenerSearchInit()
        onClickClearHistory()
    }

    private fun onClickRecyclerViewSearchItem() {
        rvSearchAdapter.setOnClickListener(object : SearchAdapter.OnClickListener {
            override fun onClick(position: Int, track: Track) {
                Toast.makeText(
                    applicationContext,
                    "Add to history: ${track.trackName}",
                    Toast.LENGTH_SHORT
                ).show()
                addTrackToHistory(track)
            }
        })
    }

    private fun onClickClearHistory() {
        buttonClearHistory.setOnClickListener(clickListener())
    }

    private fun onClickRefresh() {
        buttonRefresh.setOnClickListener(clickListener())
    }

    private fun onClickSearch() { // Клик на строку поиска
        inputEditText.setOnClickListener(clickListener())
    }

    private fun onClickClear() { // Очистка строки поиска
        val item = findViewById<ImageView>(R.id.iv_search_clear)
        item.setOnClickListener(clickListener())
    }

    private fun onClickReturn() { // Возврат на предыдущий экран
        val item = findViewById<ImageView>(R.id.iv_search_back)
        item.setOnClickListener(clickListener())
    }

    private fun onClickSearchClear() { // Очистка ввода
        buttonClear.setOnClickListener(clickListener())
    }

    private fun clearInputText() { // Очистка поля ввода
        Log.println(Log.INFO, "my_tag", "clearInputText")
        // Очистка строки ввода
        inputEditText.setText("")
        showHistory()
    }

    private fun clearButtonListener() {
        utils.hideKeyboard(this) // Скрытие клавиатуры
        clearInputText() // Очистка текста в поле поиска
        clearRecycle() // Очистка RV
    }

    private fun clickListener() = View.OnClickListener { view ->
        when (view.id) {
            R.id.iv_search_back -> this.finish()
            R.id.iv_search_clear -> clearButtonListener()
            R.id.btn_search_refresh -> searchRefresh()
            R.id.btn_clear_history -> clearHistory()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.println(Log.INFO, "my_tag", "onSave_Search")
        outState.putString(TEXT_SEARCH, searchText)
        Log.println(Log.INFO, "my_tag", "onSave_Search: $searchText")
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle
    ) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.println(Log.INFO, "my_tag", "onRestore_Search")
        val txt = savedInstanceState.getString(TEXT_SEARCH)
        Log.println(Log.INFO, "my_tag", "onRestore_Search: $txt")
        inputEditText.setText(txt)
        if (txt != null) {
            inputEditText.setSelection(txt.length)
            searchText = txt
        }
    }

    enum class State {
        SUCCESS, // Show RV
        ERROR, // Show Layout NoInternet
        NOT_FOUND, // Show Layout Empty
        SHOW_HISTORY, // Show Layout History
        HIDE_ALL // Hide all Layout and RV
    }

    companion object {
        const val TEXT_SEARCH = "first value"
    }
}

