package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.BuildConfig
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converters.FavoritesTrackDbConvertor
import com.example.playlistmaker.data.db.converters.PlaylistDbConvertor
import com.example.playlistmaker.data.db.converters.TracksInPlaylistDbConvertor
import com.example.playlistmaker.data.player.PlayerRepository
import com.example.playlistmaker.data.player.impl.PlayerRepositoryImpl
import com.example.playlistmaker.data.privatestorage.PrivateStorage
import com.example.playlistmaker.data.search.network.retrofit.RetrofitNetworkClient
import com.example.playlistmaker.data.search.network.retrofit.TrackRepository
import com.example.playlistmaker.data.search.network.retrofit.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.search.network.retrofit.models.ItunesApiService
import com.example.playlistmaker.data.search.network.retrofit.models.NetworkClient
import com.example.playlistmaker.domain.search.sharedprefs.HistoryRepository
import com.example.playlistmaker.data.search.sharedprefs.impl.HistoryRepositoryImpl
import com.example.playlistmaker.domain.settings.sharedprefs.ThemeRepository
import com.example.playlistmaker.data.settings.sharedprefs.impl.ThemeRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigatorRepository
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorRepositoryImpl
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val PLAY_LIST_SHARED_PREFERENCES = "play_list_preferences"
const val PLAY_LIST_DATABASE_NAME = "playListDb.db"

val dataModule = module {

    single<ThemeRepository> {
        ThemeRepositoryImpl(sharedPreferences = get())
    }

    single<ExternalNavigatorRepository> {
        ExternalNavigatorRepositoryImpl(context = get())
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(
            sharedPreferences = get(),
            gson = get()
        )
    }

    single<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }

    single<ItunesApiService> {
        get<Retrofit>().create(ItunesApiService::class.java)
    }

    single<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    single<SharedPreferences> {
        androidApplication().getSharedPreferences(
            PLAY_LIST_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    factory<Gson> {
        Gson()
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    single<Retrofit> {
        val baseUrl = BuildConfig.SEARCH_BASE_URL
        Retrofit.Builder().apply {
            client(get())
            baseUrl(baseUrl)
            addConverterFactory(GsonConverterFactory.create())
        }.build()
    }

    factory<MediaPlayer> {
        MediaPlayer()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, PLAY_LIST_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    factory { FavoritesTrackDbConvertor() }

    factory { PlaylistDbConvertor() }

    factory { TracksInPlaylistDbConvertor() }

    single<PrivateStorage> {
        PrivateStorage(get())
    }
}