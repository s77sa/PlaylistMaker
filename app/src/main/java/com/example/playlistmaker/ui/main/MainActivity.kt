package com.example.playlistmaker.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.println(Log.INFO, "my_tag", "onCreate_Main")
        viewModel.switchTheme()
        onClickListeners()
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
        when (view.id) {
            R.id.btn_settings -> viewModel.callSettingsActivity()
            R.id.btn_library -> viewModel.callLibraryActivity()
            R.id.btn_search -> viewModel.callSearchActivity()
        }
    }
}