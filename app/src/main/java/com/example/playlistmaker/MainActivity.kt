package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.playlistmaker.utils.Intents

@Suppress("DUPLICATE_LABEL_IN_WHEN")
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
        val intents = Intents()
        when (view.id) {
            R.id.btn_settings -> intents.intentCall(this, SettingsActivity::class.java)
            R.id.btn_library -> intents.intentCall(this, LibraryActivity::class.java)
            R.id.btn_search -> intents.intentCall(this, SearchActivity::class.java)
        }
    }
}