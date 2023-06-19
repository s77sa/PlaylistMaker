package com.example.playlistmaker.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.search.SearchActivity
import com.example.playlistmaker.presentation.settings.SettingsActivity
import com.example.playlistmaker.presentation.Intents
import com.example.playlistmaker.presentation.library.LibraryActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.println(Log.INFO, "my_tag", "onCreate_Main")
        onClickListeners() // Запуск всех прослушивателей на активити
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.println(Log.INFO, "my_tag", "onSave_Main")
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle
    ) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.println(Log.INFO, "my_tag", "onRestore_Main")
    }
    private fun onClickListeners(){
        onClickSearch()
        onClickLibrary()
        onClickSettings()
    }

    private fun onClickSearch(){
        val item = findViewById<Button>(R.id.btn_search)
        item.setOnClickListener(clickListener())
    }
    private fun onClickLibrary(){
        val item = findViewById<Button>(R.id.btn_library)
        item.setOnClickListener(clickListener())
    }

    private fun onClickSettings(){
        val item = findViewById<Button>(R.id.btn_settings)
        item.setOnClickListener(clickListener())
    }

    private fun clickListener() = View.OnClickListener { view ->
        //val intents = Intents()
        when (view.id) {
            R.id.btn_settings -> Intents.intentCall(this, SettingsActivity::class.java)
            R.id.btn_library -> Intents.intentCall(this, LibraryActivity::class.java)
            R.id.btn_search -> Intents.intentCall(this, SearchActivity::class.java)
        }
    }
}