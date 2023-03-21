package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onClickListeners() // Запуск всех прослушивателей на активити
    }

    private fun onClickListeners(){
        onClickSearch()
        onClickLibrary()
        onClickSettings()
    }

    private fun onClickSearch(){
        val item = findViewById<Button>(R.id.btn_search)
        item.setOnClickListener(clickListener)
    }
    private fun onClickLibrary(){
        val item = findViewById<Button>(R.id.btn_library)
        item.setOnClickListener(clickListener)
    }

    private fun onClickSettings(){
        val item = findViewById<Button>(R.id.btn_settings)
        item.setOnClickListener(clickListener)
    }

    // Подсмотрел подобную реализацию на stackoverflow
    private val clickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btn_settings -> intentCall(SettingsActivity::class.java)
            R.id.btn_library -> intentCall(LibraryActivity::class.java)
            R.id.btn_search -> intentCall(SearchActivity::class.java)
        }
    }
    private fun intentCall(activityClass: Class<out AppCompatActivity>){
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }
}