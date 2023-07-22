package com.example.playlistmaker.ui.search

//import com.example.playlistmaker.HISTORY_COUNT
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.ui.Helpers
import com.example.playlistmaker.ui.search.recyclerview.SearchAdapter

class SearchActivity : AppCompatActivity() {
    private lateinit var viewModel: SearchViewModel

    private lateinit var inputEditText: EditText
    private lateinit var rvSearch: RecyclerView
    private lateinit var rvHistory: RecyclerView
    private lateinit var rvSearchAdapter: SearchAdapter
    private lateinit var rvHistoryAdapter: SearchAdapter
    private lateinit var buttonRefresh: Button
    private lateinit var buttonClear: ImageView
    private lateinit var buttonClearHistory: Button
    private lateinit var layoutIsEmpty: LinearLayout
    private lateinit var layoutNoInternet: LinearLayout
    private lateinit var layoutHistory: LinearLayout
    private lateinit var progressBar: ProgressBar
    private var searchTrackList: MutableList<Track> = mutableListOf()
    private var historyTrackList: MutableList<Track> = mutableListOf()
    private var searchText = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        Log.println(Log.INFO, "my_tag", "onCreate Search")
        initViews()
        initTextWatcher()
        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]
        initAdapters()
        initOnClickListeners()
        initObservers()
        viewModel.checkState()
    }

    private fun initAdapters() {
        rvSearchAdapter = SearchAdapter(searchTrackList)
        rvHistoryAdapter = SearchAdapter(historyTrackList)
        rvSearch.adapter = rvSearchAdapter
        rvHistory.adapter = rvHistoryAdapter
    }

    private fun initObservers() {
        viewModel.searchText.observe(this) {
            searchText = it
            Log.d("my_tag", "observe searchText = $it")
        }
        viewModel.searchTrackList.observe(this) {
            addSearchResultToRecycle(it)
            Log.d("my_tag", "observe searchTrackList = ${it.size}")
        }
        viewModel.historyTrackList.observe(this) {
            addHistoryResultToRecycle(it)
            Log.d("my_tag", "observe historyTrackList = ${it.size}")
        }
        viewModel.searchActivityState.observe(this) {
            showInvisibleLayout(it)
            Log.d("my_tag", "observe ActivityState = $it")
        }
    }

    private fun clearHistory() {
        if (historyTrackList.size > 0) historyTrackList.clear()
        showInvisibleLayout(StateActivity.HIDE_ALL)
        viewModel.clearHistory()
        Log.println(Log.INFO, "my_tag", "clearHistory")
    }

    private fun onFocusListenerSearchInit() {
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            Log.println(Log.INFO, "my_tag", "onFocusListenerInit")
            if (hasFocus) {
                viewModel.checkState()
            }
        }
    }

    private fun searchRefresh() {
        //showInvisibleLayout()
        //retrofitCall(searchText)
        viewModel.searchRequest()
    }

    private fun addSearchResultToRecycle(list: List<Track>) {
        searchTrackList.clear()
        searchTrackList.addAll(list)
        rvSearch.adapter?.notifyItemRangeInserted(searchTrackList.size, list.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addHistoryResultToRecycle(list: List<Track>) {
        for (item in list){
            Log.d("my_tag", "add To History Adapter: ${item.trackId}")
        }
        historyTrackList.clear()
        historyTrackList.addAll(list)
        //rvHistory.adapter?.notifyItemRangeInserted(historyTrackList.size, list.size)
        rvHistory.adapter?.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearSearchResult() {
        Log.println(Log.INFO, "my_tag", "clearRecycle")
        searchTrackList.clear()
        rvSearch.adapter?.notifyDataSetChanged()
        viewModel.clearSearchTrackList()
        viewModel.checkState()
    }

    private fun initTextWatcher() { // Инициализация TextWatcher
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonClear.visibility = clearButtonVisibility(s)
                val text = inputEditText.text.toString()
                viewModel.setSearchText(text)
                Log.d("my_tag", "onTextChanged=$text")
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d("my_tag", "afterTextChanged")
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    private fun clearInputText() {
        Log.println(Log.INFO, "my_tag", "clearInputText")
        inputEditText.setText("")
        viewModel.checkState()
    }

    private fun clearButtonVisibility(s: CharSequence?): Int { // Изменение прозрачности кнопки (крестик)
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun onClickRecyclerViewHistoryItem() {
        rvHistoryAdapter.setOnClickListener(object : SearchAdapter.OnClickListener {
            override fun onClick(position: Int, track: Track) {
                viewModel.callPlayerActivity(track)
            }
        })
    }

    private fun onClickRecyclerViewSearchItem() {
        rvSearchAdapter.setOnClickListener(object : SearchAdapter.OnClickListener {
            override fun onClick(position: Int, track: Track) {
                Toast.makeText(
                    applicationContext,
                    "Add to history: ${track.trackName}",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.addTrackToHistory(track)
                viewModel.callPlayerActivity(track)
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

    private fun clearButtonListener() {
        Helpers.hideKeyboard(this) // Скрытие клавиатуры
        clearInputText() // Очистка текста в поле поиска
        clearSearchResult() // Очистка RV
    }

    private fun initOnClickListeners() {
        onClickReturn()
        buttonClear.setOnClickListener(clickListener())
        onClickClear()
        onClickSearch()
        onClickRefresh()
        onClickRecyclerViewSearchItem()
        onClickRecyclerViewHistoryItem()
        onFocusListenerSearchInit()
        onClickClearHistory()
    }

    private fun clickListener() = View.OnClickListener { view ->
        when (view.id) {
            R.id.iv_search_back -> this.finish()
            R.id.iv_search_clear -> clearButtonListener()
            R.id.btn_search_refresh -> searchRefresh()
            R.id.btn_clear_history -> clearHistory()
        }
    }

    private fun initViews() {
        inputEditText = findViewById(R.id.et_search)
        buttonClear = findViewById(R.id.iv_search_clear)
        rvSearch = findViewById(R.id.rv_Search)
        buttonRefresh = findViewById(R.id.btn_search_refresh)
        layoutIsEmpty = findViewById(R.id.layout_is_empty)
        layoutNoInternet = findViewById(R.id.layout_no_internet)
        layoutHistory = findViewById(R.id.layout_history)
        buttonClearHistory = findViewById(R.id.btn_clear_history)
        rvHistory = findViewById(R.id.rv_history)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun showInvisibleLayout(state: StateActivity = StateActivity.HIDE_ALL) {
        rvSearch.visibility = View.GONE
        layoutNoInternet.visibility = View.GONE
        layoutIsEmpty.visibility = View.GONE
        layoutHistory.visibility = View.GONE
        progressBar.visibility = View.GONE
        when (state) {
            StateActivity.SUCCESS -> rvSearch.visibility = View.VISIBLE
            StateActivity.NOT_FOUND -> layoutIsEmpty.visibility = View.VISIBLE
            StateActivity.ERROR -> layoutNoInternet.visibility = View.VISIBLE
            StateActivity.SHOW_HISTORY -> layoutHistory.visibility = View.VISIBLE
            StateActivity.PROGRESS_BAR -> progressBar.visibility = View.VISIBLE
            else -> {}
        }
    }
}