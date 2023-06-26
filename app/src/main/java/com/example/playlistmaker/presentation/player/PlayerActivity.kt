package com.example.playlistmaker.presentation.player

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.presentation.search.KEY_INTENT_PLAYER_ACTIVITY
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.models.track.Track
import com.example.playlistmaker.presentation.helpers.Helpers
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.player.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

private const val REPLACE_LINK_PATTERN: String = "512x512bb.jpg"

@Suppress("DEPRECATION")
class PlayerActivity : AppCompatActivity() {
    companion object {
        private const val REFRESH_TIME_HEADER_DELAY_MILLIS: Long = 330L
    }

    private lateinit var mediaPlayerInteractor: PlayerInteractor

    private var mainThreadHandler: Handler? = null

    private lateinit var buttonBack: ImageView
    private lateinit var track: Track
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackTimeValue: TextView
    private lateinit var collectionNameValue: TextView
    private lateinit var collectionNameDescription: TextView
    private lateinit var releaseDateValue: TextView
    private lateinit var primaryGenreValue: TextView
    private lateinit var countryValue: TextView
    private lateinit var artWorkBig: ImageView
    private lateinit var buttonPlay: ImageView
    private lateinit var trackTimeCurrent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initView()
        clickListenersInit()
        getMessageFromIntent()
        writeTrackDataToView()
        preparePlayer()
        mainThreadHandler = Handler(Looper.getMainLooper())
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerInteractor.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerInteractor.releasePlayer()
        mainThreadHandler?.removeCallbacksAndMessages(null)
    }

    private fun startThread() {
        Log.println(Log.INFO, "my_tag", "startThread")
        mainThreadHandler?.post(
            object : Runnable {
                override fun run() {
                    if (mediaPlayerInteractor.getPlayerState() == PlayerState.STATE_PLAYING) {
                        setTimeToTrackTime(mediaPlayerInteractor.getCurrentPosition())
                        mainThreadHandler?.postDelayed(this, REFRESH_TIME_HEADER_DELAY_MILLIS)
                    } else {
                        setButtonPlayState()
                    }
                }
            }
        )
    }

    private fun setTimeToTrackTime(position: Int = 0) {
        Log.println(Log.INFO, "my_tag", "position=$position")
        trackTimeCurrent.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(position)
    }

    private fun playbackControl() {
        when (mediaPlayerInteractor.getPlayerState()) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
                startThread()
            }

            else -> {}
        }
        setButtonPlayState()
    }

    private fun preparePlayer() {
        mediaPlayerInteractor = Creator.providePlayerInteractor()
        mediaPlayerInteractor.preparePlayer(track.previewUrl)
    }

    private fun startPlayer() {
        mediaPlayerInteractor.startPlayer()
    }

    private fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
    }

    private fun setButtonPlayState() {
        when (mediaPlayerInteractor.getPlayerState()) {
            PlayerState.STATE_PLAYING -> {
                buttonPlay.setImageResource(R.drawable.ic_button_pause)
            }

            PlayerState.STATE_PAUSED -> {
                buttonPlay.setImageResource(R.drawable.ic_button_play)
            }

            else -> { // PREPARED STATE
                buttonPlay.setImageResource(R.drawable.ic_button_play)
                setTimeToTrackTime()

            }
        }
    }

    private fun writeTrackDataToView() {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTimeValue.text = Helpers.millisToString(track.trackTimeMillis)
        writeCollectionName(track.collectionName)
        releaseDateValue.text = shrinkReleaseDate(track.releaseDate)
        primaryGenreValue.text = track.primaryGenreName
        countryValue.text = track.country
        writeArtWorkToImageView(track.artworkUrl100)
    }

    private fun writeCollectionName(value: String) {
        if (value.isNotEmpty()) {
            collectionNameValue.text = track.collectionName
            collectionNameDescription.visibility = View.VISIBLE
            collectionNameValue.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun shrinkReleaseDate(value: String): String? {
        val inputDate = SimpleDateFormat("yyyy-M-d").parse(value)
        return inputDate?.let { SimpleDateFormat("yyyy").format(it) }
    }

    private fun writeArtWorkToImageView(link: String) {
        val newLink = link.replaceAfterLast('/', REPLACE_LINK_PATTERN)
        Helpers.glideBind(newLink, this.artWorkBig)
    }

    private fun getMessageFromIntent() {
        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(KEY_INTENT_PLAYER_ACTIVITY, Track::class.java)!!
        } else {
            intent.getSerializableExtra(KEY_INTENT_PLAYER_ACTIVITY)!! as Track
        }
    }

    private fun initView() {
        buttonBack = findViewById(R.id.iv_player_back)
        trackName = findViewById(R.id.tv_trackName)
        artistName = findViewById(R.id.tv_artistName)
        trackTimeValue = findViewById(R.id.tv_trackTimeValue)
        collectionNameValue = findViewById(R.id.tv_collectionNameValue)
        releaseDateValue = findViewById(R.id.tv_releaseDateValue)
        primaryGenreValue = findViewById(R.id.tv_primaryGenreValue)
        countryValue = findViewById(R.id.tv_countryValue)
        artWorkBig = findViewById(R.id.iv_artWorkBig)
        collectionNameDescription = findViewById(R.id.tv_collectionNameHeader)
        buttonPlay = findViewById(R.id.iv_play)
        trackTimeCurrent = findViewById(R.id.tv_trackTimeCurrent)
    }

    private fun clickListenersInit() {
        buttonBack.setOnClickListener(clickListener())
        buttonPlay.setOnClickListener(clickListener())
    }

    private fun clickListener() = View.OnClickListener { view ->
        when (view.id) {
            R.id.iv_player_back -> this.finish()
            R.id.iv_play -> playbackControl()
        }
    }
}