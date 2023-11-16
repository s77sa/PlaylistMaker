package com.example.playlistmaker.ui.player

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.ui.utils.Helpers
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat


const val KEY_INTENT_PLAYER_ACTIVITY = "player_intent"
class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track
    private val viewModel by viewModel<PlayerViewModel> { parametersOf(track) }

    companion object {
        private const val REPLACE_LINK_PATTERN: String = "512x512bb.jpg"
        private val TAG = PlayerActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getMessageFromIntent()
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
            Log.d(TAG, "init - Player Observer - ${track.trackName}")
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
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(KEY_INTENT_PLAYER_ACTIVITY)!! as Track
        }
    }
}