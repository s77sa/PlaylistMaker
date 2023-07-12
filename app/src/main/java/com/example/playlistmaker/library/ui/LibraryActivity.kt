package com.example.playlistmaker.library.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.example.playlistmaker.R

class LibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
        onClickListeners() // Запуск всех прослушивателей на активити
    }

    private fun onClickListeners(){
        onClickReturn()
    }
    private fun onClickReturn(){ // Возврат на предыдущий экран
        val item = findViewById<ImageView>(R.id.iv_library_back)
        item.setOnClickListener(clickListener())
    }

    private fun clickListener() = View.OnClickListener { view ->
        //val intents = Intents()
        when (view.id) {
            R.id.iv_library_back -> this.finish()
        }
    }
}