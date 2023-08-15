package com.example.playlistmaker.ui.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class LibraryActivity : AppCompatActivity() {

    private val buttonBack: ImageView? = null

    private lateinit var binding: ActivityLibraryBinding
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.libraryViewPager.adapter = LibraryViewPagerAdapter(supportFragmentManager, lifecycle)


        initTabMediator()
        initOnClickListeners()
    }

    private fun initTabMediator(){
        tabLayoutMediator = TabLayoutMediator(binding.libraryLayout, binding.libraryViewPager){ tab, position ->
            when (position){
                0 -> tab.text = getString(R.string.library_tabLayout_favoritesTracks)
                1 -> tab.text = getString(R.string.library_tabLayout_playLists)
            }
        }
        tabLayoutMediator.attach()
    }



    private fun initOnClickListeners(){
        binding.ivLibraryBack.setOnClickListener {
            onClickReturn()
        }
    }
    private fun onClickReturn(){
            this.finish()
    }
}