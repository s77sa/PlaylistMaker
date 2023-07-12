package com.example.playlistmaker.creator

//import com.example.playlistmaker.player.data.GlideRepository
//import com.example.playlistmaker.player.data.GlideRepositoryImpl
import com.example.playlistmaker.player.data.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerInteractorImpl
import com.example.playlistmaker.player.data.PlayerRepository
//import com.example.playlistmaker.player.domain.GlideInteractor
//import com.example.playlistmaker.player.domain.GlideInteractorImpl

object Creator {
    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository()
        )
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

//    fun provideGlideInteractor(): GlideInteractor{
//        return GlideInteractorImpl(getGlideRepository())
//    }
//
//    private fun getGlideRepository(): GlideRepository{
//        return GlideRepositoryImpl()
//    }
}