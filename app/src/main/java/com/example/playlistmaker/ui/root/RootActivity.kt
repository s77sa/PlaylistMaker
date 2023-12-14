package com.example.playlistmaker.ui.root

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding
    private val viewModel: RootViewModel by viewModel<RootViewModel>()
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.containerView.id) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigationView = findViewById<BottomNavigationView>(binding.bottomNavigationView.id)
        bottomNavigationView.setupWithNavController(navController)

        viewModel.switchTheme()

        navControllerListener()
    }

    private fun navControllerListener(){
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.createPlaylistFragment ->{
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
            }

        }
        }
    }
}