package com.example.karol.musicapp

import android.app.Application
import android.arch.persistence.room.Room
import cafe.adriel.androidaudioconverter.AndroidAudioConverter
import cafe.adriel.androidaudioconverter.callback.ILoadCallback
import com.example.karol.musicapp.Data.AppDatabase

class MusicApp : Application() {

    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database =  Room.databaseBuilder(this, AppDatabase::class.java, "song.db").build()
        AndroidAudioConverter.load(this, object : ILoadCallback {
            override fun onSuccess() {}
            override fun onFailure(error: Exception) {}
        })
    }
}