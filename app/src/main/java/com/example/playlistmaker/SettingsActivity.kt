package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        onClickListeners() // Запуск всех прослушивателей на активити
    }

    private fun onClickListeners(){
        onClickReturn()
    }

    private fun onClickReturn(){
        val item = findViewById<Button>(R.id.btn_04)
        intentCall(item, MainActivity())
    }

    private fun intentCall(view: View, activity: AppCompatActivity){ // Общий вызов Intent
        view.setOnClickListener{
            val intent = Intent(this, activity::class.java)
            startActivity(intent)
        }
    }
}