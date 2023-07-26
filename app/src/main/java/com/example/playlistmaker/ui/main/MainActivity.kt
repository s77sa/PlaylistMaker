package com.example.playlistmaker.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.println(Log.INFO, "my_tag", "onCreate_Main")
        viewModel = ViewModelProvider(
            this,
            MainViewModel.getViewModelFactory()
        )[MainViewModel::class.java]
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