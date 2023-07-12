//package com.example.playlistmaker.player.data
//
//import android.widget.ImageView
//import androidx.core.content.contentValuesOf
//import com.bumptech.glide.Glide
//
//class GlideRepositoryImpl : GlideRepository{
//
//    override fun glideBind(link: String, target: ImageView, placeholderId: Int) {
//        Glide.with(target)
//            .load(link)
//            //.placeholder(placeholderId)
//            .centerInside()
//            .into(target)
//    }
//}