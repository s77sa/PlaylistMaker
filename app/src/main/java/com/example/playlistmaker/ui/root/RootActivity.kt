package com.example.playlistmaker.ui.root

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.example.playlistmaker.domain.library.PlaylistStorage
import com.example.playlistmaker.domain.library.TrackStorage
import com.example.playlistmaker.domain.model.Playlist
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootActivity : AppCompatActivity(), TrackStorage, PlaylistStorage {

    private lateinit var binding: ActivityRootBinding
    private val viewModel: RootViewModel by viewModel<RootViewModel>()
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView
    private var trackStorage: Track? = null
    private var playlistStorage: Playlist? = null

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
                    visibleBottomNav(View.GONE)
                }

                R.id.playerFragment -> {
                    visibleBottomNav(View.GONE)
                }

                R.id.editPlaylistFragment ->{
                    visibleBottomNav(View.GONE)
                }

                R.id.infoPlaylistFragment ->{
                    visibleBottomNav(View.GONE)
                }

                else -> {
                    visibleBottomNav(View.VISIBLE)
                }

            }
        }
    }

    private fun visibleBottomNav(visibility: Int){
        bottomNavigationView.visibility = visibility
        binding.bottomNavigationSeparator.visibility = visibility
    }

    override fun setTrack(track: Track) {
        trackStorage = track
    }

    override fun getTrack(): Track? {
        return trackStorage
    }

    override fun setPlaylist(playlist: Playlist) {
        playlistStorage = playlist
    }

    override fun getPlaylist(): Playlist? {
        return playlistStorage
    }
}