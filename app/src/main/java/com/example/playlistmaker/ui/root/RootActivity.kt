package com.example.playlistmaker.ui.root

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.data.models.Track
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.example.playlistmaker.domain.library.TrackStorage
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootActivity : AppCompatActivity(), TrackStorage {

    private lateinit var binding: ActivityRootBinding
    private val viewModel: RootViewModel by viewModel<RootViewModel>()
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    private var trackStorage: Track? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.containerView.id) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigationView = findViewById<BottomNavigationView>(binding.bottomNavigationView.id)
        bottomNavigationView.setupWithNavController(navController)

        viewModel.switchTheme()

        navControllerListener()
    }


    private fun navControllerListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.createPlaylistFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }

                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }

            }
        }
    }

    override fun setTrack(track: Track) {
        trackStorage = track
    }

    override fun getTrack(): Track? {
        return trackStorage
    }
}