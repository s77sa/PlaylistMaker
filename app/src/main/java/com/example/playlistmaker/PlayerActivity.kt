package com.example.playlistmaker

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.playlistmaker.utils.Helpers
import com.example.playlistmaker.retrofit.Track
import java.text.SimpleDateFormat

private const val REPLACE_LINK_PATTERN: String = "512x512bb.jpg"

@Suppress("DEPRECATION")
class PlayerActivity : AppCompatActivity() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initView()
        clickListenersInit()
        getMessageFromIntent()
        writeTrackDataToView()
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

    private fun writeCollectionName(value: String){
        if (value.isNotEmpty()){
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

    private fun writeArtWorkToImageView(link: String){
        val newLink = link.replaceAfterLast('/', REPLACE_LINK_PATTERN)
        Helpers.glideBind(newLink, this.artWorkBig)
    }

private fun getMessageFromIntent(){
    track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getSerializableExtra(KEY_INTENT_PLAYER_ACTIVITY, Track::class.java)!!
    }else {
        intent.getSerializableExtra(KEY_INTENT_PLAYER_ACTIVITY)!! as Track
    }
}

    private fun initView(){
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
    }

    private fun clickListenersInit(){
        buttonBack.setOnClickListener(clickListener())
    }

    private fun clickListener() = View.OnClickListener { view ->
        when (view.id) {
            R.id.iv_player_back -> this.finish()
        }
    }
}