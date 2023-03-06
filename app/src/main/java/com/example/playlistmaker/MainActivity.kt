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
        val item = findViewById<Button>(R.id.btn_01)
        intentCall(item, SearchActivity())
    }
    private fun onClickLibrary(){
        val item = findViewById<Button>(R.id.btn_02)
        intentCall(item, LibraryActivity())
    }

    private fun onClickSettings(){
        val item = findViewById<Button>(R.id.btn_03)
        intentCall(item, SettingsActivity())
    }

    private fun intentCall(view: View, activity: AppCompatActivity){ // Общий вызов Intent
        view.setOnClickListener{
            val intent = Intent(this, activity::class.java)
            startActivity(intent)
        }
    }
}