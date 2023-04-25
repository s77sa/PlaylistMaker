package com.example.playlistmaker

import SearchAdapter
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.example.playlistmaker.retrofit.Track
import com.example.playlistmaker.retrofit.Tracks
import com.example.playlistmaker.retrofit.TracksApi
import com.example.playlistmaker.retrofit.TracksRetrofit
import com.example.playlistmaker.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var tracksApi: TracksApi
    private lateinit var retrofit: TracksRetrofit
    private val interceptor: Boolean = true
    private val utils: Utils = Utils()
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var rvItems: RecyclerView
    private lateinit var refreshButton: Button
    private lateinit var layoutIsEmpty: LinearLayout
    private lateinit var layoutNoInternet: LinearLayout
    private var rvList: MutableList<Track> = mutableListOf()
    private var searchText = ""


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        Log.println(Log.INFO, "my_tag", "onCreate_Search")
        inputEditText = findViewById<EditText>(R.id.et_search)
        clearButton = findViewById<ImageView>(R.id.iv_search_clear)
        rvItems = findViewById<RecyclerView>(R.id.rv_Search)
        refreshButton = findViewById<Button>(R.id.btn_search_refresh)
        layoutIsEmpty = findViewById<LinearLayout>(R.id.layout_is_empty)
        layoutNoInternet = findViewById<LinearLayout>(R.id.layout_no_internet)
        onClickListenersInit()
        textWatcherInit()
        retrofitInit(getString(R.string.searchBaseUrl))
        rvItems.adapter = SearchAdapter(rvList) // Адаптер для RV
        queryInput(inputEditText)
    }

    private fun queryInput(editText: EditText) {
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Toast.makeText(applicationContext, "Search run", Toast.LENGTH_SHORT).show()
                retrofitCall(editText.text.toString())
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
        when (state) {
            State.SUCCESS -> rvItems.visibility = View.VISIBLE
            State.NOT_FOUND -> layoutIsEmpty.visibility = View.VISIBLE
            State.ERROR -> layoutNoInternet.visibility = View.VISIBLE
            else -> {}
        }
    }

    // Заполнение RecyclerView
    private fun addSearchResultToRecycle(list: MutableList<Track>) {
        Log.println(Log.INFO, "my_tag", "searchToRecycle2")
        val start = list.size
        rvList.addAll(list)
        rvItems.adapter?.notifyItemRangeInserted(start, rvList.size)
        Log.println(Log.INFO, "my_tag", "searchToRecycle rvList: ${rvList.size}")
    }

    // Очистка RecycleView
    private fun clearRecycle() {
        Log.println(Log.INFO, "my_tag", "clearRecycle")
        val count = rvList.size
        rvList.clear()
        rvItems.adapter?.notifyItemRangeRemoved(0, count)
        Log.println(Log.INFO, "my_tag", "rvlist: ${rvList.size}")
    }

    private fun textWatcherInit() { // Инициализация TextWatcher

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
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
    }

    private fun onClickRefresh() {
        refreshButton.setOnClickListener(clickListener())
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
        clearButton.setOnClickListener(clickListener())
    }

    private fun clearInputText() { // Очистка поля ввода
        Log.println(Log.INFO, "my_tag", "clearInputText")
        // Очистка строки ввода
        inputEditText.setText("")
        showInvisibleLayout(State.HIDE_ALL)
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

    enum class State{
        SUCCESS, // Show RV
        ERROR, // Show Layout NoInternet
        NOT_FOUND, // Show Layout Empty
        HIDE_ALL // Hide all Layout and RV
    }

    companion object {
        const val TEXT_SEARCH = "first value"
    }
}

