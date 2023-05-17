package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.playlistmaker.retrofit.Track
import com.google.gson.GsonBuilder

class PlayerActivity : AppCompatActivity() {
    private lateinit var buttonBack: ImageView
    private lateinit var track: Track
    private lateinit var trackName: TextView
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
        trackName = findViewById(R.id.tv_TrackName)
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