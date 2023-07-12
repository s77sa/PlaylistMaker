package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.models.track.Track
import com.example.playlistmaker.player.domain.PlayerViewModel
import com.example.playlistmaker.search.ui.KEY_INTENT_PLAYER_ACTIVITY
import com.example.playlistmaker.sharing.helpers.Helpers
import java.text.SimpleDateFormat

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel
    private lateinit var track: Track

    companion object {
        private const val REPLACE_LINK_PATTERN: String = "512x512bb.jpg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getMessageFromIntent()
        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(track)
        )[PlayerViewModel::class.java]
        initObserver()
        clickListenersInit()
        viewModel.saveValues()
        viewModel.preparePlayer()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun initObserver() {
        viewModel.getLoadingValues().observe(this) { track ->
            Log.d("my_tag", "init - Observer - ${track.trackName}")
            writeTrackDataToView()
        }
        viewModel.getPlayingPositionLiveData().observe(this) { value ->
            binding.tvTrackTimeCurrent.text = value
        }
        viewModel.getIsPlayingLiveData().observe(this) {
            setButtonPlayState(it)
        }
    }

    private fun setButtonPlayState(status: Boolean) {
        if (status) {
            binding.ivPlay.setImageResource(R.drawable.ic_button_pause)
        } else {
            binding.ivPlay.setImageResource(R.drawable.ic_button_play)
        }
    }

    private fun clickListenersInit() {
        binding.ivPlayerBack.setOnClickListener { finishActivity() }
        binding.ivPlay.setOnClickListener { startPlaying() }
    }

    private fun startPlaying() {
        viewModel.playbackControl()
    }

    private fun finishActivity() {
        this.finish()
    }

    private fun writeTrackDataToView() {
        binding.tvTrackName.text = viewModel.getLoadingValues().value?.trackName
        binding.tvArtistName.text = viewModel.getLoadingValues().value?.artistName
        binding.tvTrackTimeValue.text =
            viewModel.getLoadingValues().value?.trackTimeMillis?.let { Helpers.millisToString(it) }
        writeCollectionName(viewModel.getLoadingValues().value?.collectionName)
        binding.tvReleaseDateValue.text =
            getReleaseDateValue(viewModel.getLoadingValues().value?.releaseDate)
        binding.tvPrimaryGenreValue.text = viewModel.getLoadingValues().value?.primaryGenreName
        binding.tvCountryValue.text = viewModel.getLoadingValues().value?.country
        viewModel.getLoadingValues().value?.artworkUrl100?.let { writeArtWorkToImageView(it) }
    }

    private fun writeCollectionName(value: String?) {
        if (!value.isNullOrEmpty()) {
            binding.tvCollectionNameValue.text = value
            binding.tvCollectionNameValue.visibility = View.VISIBLE
            binding.tvCollectionNameHeader.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getReleaseDateValue(value: String?): String? {
        val inputDate = value?.let { SimpleDateFormat("yyyy-M-d").parse(it) }
        return inputDate?.let { SimpleDateFormat("yyyy").format(it) }
    }

    private fun writeArtWorkToImageView(link: String) {
        val newLink = link.replaceAfterLast('/', REPLACE_LINK_PATTERN)
        Helpers.glideBind(newLink, binding.ivArtWorkBig)
    }

    private fun getMessageFromIntent() {
        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(KEY_INTENT_PLAYER_ACTIVITY, Track::class.java)!!
        } else {
            intent.getSerializableExtra(KEY_INTENT_PLAYER_ACTIVITY)!! as Track
        }
    }
}