package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.playlistmaker.models.Utils
import com.example.playlistmaker.retrofit.Track
import com.google.gson.GsonBuilder

class PlayerActivity : AppCompatActivity() {
    private lateinit var buttonBack: ImageView
    private lateinit var track: Track
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackTimeValue: TextView
    private lateinit var collectionNameValue: TextView
    private lateinit var releaseDateValue: TextView
    private lateinit var primaryGenreValue: TextView
    private lateinit var countryValue: TextView
    private lateinit var artWorkBig: ImageView
    private val replaceLinkPattern: String = "512x512bb.jpg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        viewInit()
        clickListenersInit()
        getMessageFromIntent()
        writeTrackDataToView()
    }

    private fun writeTrackDataToView() {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTimeValue.text = Utils.millisToString(track.trackTimeMillis)
        collectionNameValue.text = track.collectionName
        releaseDateValue.text = shrinkReleaseDate(track.releaseDate)
        primaryGenreValue.text = track.primaryGenreName
        countryValue.text = track.country
        writeArtWorkToImageView(track.artworkUrl100)
    }

    private fun shrinkReleaseDate(value: String): String{
        val length = value.length - 4
        return value.dropLast(length)
    }
    private fun writeArtWorkToImageView(link: String){
        val newLink = link.replaceAfterLast('/', replaceLinkPattern)
        Utils.glideBind(newLink, this.artWorkBig)
    }

    private fun getMessageFromIntent(){
        val message = intent.getStringExtra(KEY_INTENT_PLAYER_ACTIVITY)
        Log.println(Log.INFO, "my_tag", "intent player = $message")
        if(message != null) track = writeMessageToTrackData(message)
        Log.println(Log.INFO, "my_tag", "intent player = $track")
    }

    private fun writeMessageToTrackData(value: String): Track {
        val gson = GsonBuilder().create()
        return gson.fromJson(value, Track::class.java)
    }

    private fun viewInit(){
        buttonBack = findViewById(R.id.iv_player_back)
        trackName = findViewById(R.id.tv_trackName)
        artistName = findViewById(R.id.tv_artistName)
        trackTimeValue = findViewById(R.id.tv_trackTimeValue)
        collectionNameValue = findViewById(R.id.tv_collectionNameValue)
        releaseDateValue = findViewById(R.id.tv_releaseDateValue)
        primaryGenreValue = findViewById(R.id.tv_primaryGenreValue)
        countryValue = findViewById(R.id.tv_countryValue)
        artWorkBig = findViewById(R.id.iv_artWorkBig)
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