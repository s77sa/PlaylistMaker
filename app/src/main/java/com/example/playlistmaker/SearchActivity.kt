package com.example.playlistmaker

import SearchAdapter
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var rvItems: RecyclerView
    //private lateinit var rvAdapter: SearchAdapter
    private var rvList: MutableList<TrackData> = mutableListOf()
    private var searchText = ""
    companion object{
        const val TEXT_SEARCH = "first value"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        Log.println(Log.INFO, "my_tag", "onCreate_Search")
        inputEditText = findViewById<EditText>(R.id.et_search)
        clearButton = findViewById<ImageView>(R.id.iv_search_clear)
        rvItems = findViewById<RecyclerView>(R.id.rv_Search)
        onClickListeners() // Запуск всех прослушивателей на активити
        textWatcherInit()

        // =============
        //rvList = mockTrackData() // Заполнили сразу
        //rvAdapter = SearchAdapter(rvList)
        rvItems.adapter = SearchAdapter(rvList)
//        rvItems.adapter = rvAdapter
        //rvItems.adapter = SearchAdapter(rvList)

        //        val adapter = SearchAdapter(mockTrackData())
        //        rvSearch.adapter = adapter

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

    private fun mockTrackData(): MutableList<TrackData> {
        return mutableListOf(
            TrackData(
                trackName = "Smells Like Teen Spirit",
                artistName = "Nirvana",
                trackTime = "5:01",
                artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
            ),
            TrackData(
                trackName = "Billie Jean",
                artistName = "Michael Jackson",
                trackTime = "4:35",
                artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
            ),
            TrackData(
                trackName = "Stayin' Alive",
                artistName = "Bee Gees",
                trackTime = "4:10",
                artworkUrl100 = "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
            ),
            TrackData(
                trackName = "Whole Lotta Love",
                artistName = "Led Zeppelin",
                trackTime = "5:33",
                artworkUrl100 = "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
            ),
            TrackData(
                trackName = "Sweet Child O'Mine",
                artistName = "Guns N' Roses",
                trackTime = "5:03",
                artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
            )
        )
    }

    // Заполнение RecyclerView
    private fun searchToRecycle(){
        Log.println(Log.INFO, "my_tag", "searchToRecycle")
        val start = rvList.size
        rvList.addAll(mockTrackData())
        rvItems.adapter?.notifyItemRangeInserted(start,rvList.size)
        Log.println(Log.INFO, "my_tag", "searchToRecycle rvList: ${rvList.size}")
    }

    // Очистка RecycleView
    private fun clearRecycle(){
        Log.println(Log.INFO, "my_tag", "clearRecycle")
        val count = rvList.size
        rvList.clear()
        rvItems.adapter?.notifyItemRangeRemoved(0,count)
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
                    searchToRecycle() // Заполнение RecyclerView
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
    private fun onClickListeners(){
        onClickReturn()
        onClickSearchClear()
        onClickClear()
        onClickSearch()
    }

    private fun onClickSearch(){ // Клик на строку поиска
        inputEditText.setOnClickListener(clickListener())
    }

    private fun onClickClear(){ // Очистка строки поиска
        val item = findViewById<ImageView>(R.id.iv_search_clear)
        item.setOnClickListener(clickListener())
    }

    private fun onClickReturn(){ // Возврат на предыдущий экран
        val item = findViewById<ImageView>(R.id.iv_search_back)
        item.setOnClickListener(clickListener())
    }
    private fun onClickSearchClear(){ // Очистка ввода
        clearButton.setOnClickListener(clickListener())
    }
    private fun clearInputText(){ // Очистка поля ввода
        Log.println(Log.INFO, "my_tag", "clearInputText")
        // Очистка строки ввода
        inputEditText.setText("")
        // Скрытие клавиатуры
        val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = this.currentFocus
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken,0)
        }
        clearRecycle() // Очистка RV
    }

    private fun clickListener() = View.OnClickListener { view ->
        when (view.id) {
            R.id.iv_search_back -> this.finish()
            R.id.iv_search_clear -> clearInputText()
        }
    }
}