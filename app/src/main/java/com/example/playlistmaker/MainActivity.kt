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
        intentCall(item, SearchActivity::class.java)
    }
    private fun onClickLibrary(){
        val item = findViewById<Button>(R.id.btn_library)
        intentCall(item, LibraryActivity::class.java)
    }

    private fun onClickSettings(){
        val item = findViewById<Button>(R.id.btn_settings)
        intentCall(item, SettingsActivity::class.java)
    }

    private fun intentCall(view: View, activityClass: Class<out AppCompatActivity>){ // Общий вызов Intent
        view.setOnClickListener{
            val intent = Intent(this, activityClass)
            startActivity(intent)
        }
    }
}