package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

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
        val item = findViewById<ImageView>(R.id.iv_settings_back)
        item.setOnClickListener{
            this.finish()
        }
    }
// Не стал убирать эту функцию, считаю что она еще пригодиться в дальнейшем
    private fun intentCall(view: View, activityClass: Class<out AppCompatActivity>){ // Общий вызов Intent
        view.setOnClickListener{
            val intent = Intent(this, activityClass)
            startActivity(intent)
        }
    }
}