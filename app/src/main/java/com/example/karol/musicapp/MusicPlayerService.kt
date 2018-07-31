
package com.example.karol.musicapp

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Environment
import android.os.IBinder
import java.io.File
import java.io.IOException

class MusicPlayerService() : Service(), MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private var songs: ArrayList<File>? = null
    private var songNumber: Int = 0
    private var playingSong: Boolean = false
    private var mediaPlayer: MediaPlayer? = null



    override fun onCreate() {
        super.onCreate()
        songs = arrayListOf()
        getSongs()
        mediaPlayer = MediaPlayer().apply {
            setOnCompletionListener(this@MusicPlayerService)
            setOnPreparedListener(this@MusicPlayerService)
        }
    }

    override fun onCompletion(p0: MediaPlayer?) {
        playNextSong()
    }

    override fun onPrepared(p0: MediaPlayer?) {
        mediaPlayer?.start()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return MyBinder()
    }

    fun getSongs() {
        songs!!.clear()
        val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        for (file in filePath.listFiles()!!) {
            songs?.add(file)
        }
    }

    fun playNextSong() {
        mediaPlayer?.reset()
        songNumber ++
        playingSong = false
        playSong()
    }

    fun nextSong() {
        mediaPlayer?.reset()
        playingSong = false
        if (songNumber + 1 == songs?.size) {
            songNumber = 0
        } else {
            songNumber++
        }
    }

    fun prevSong() {
        mediaPlayer?.reset()
        playingSong = false
        if (songNumber == 0) {
            songNumber = songs!!.size - 1
            return
        }
        songNumber--
    }

    fun playSong(): String {
        try {
            if (!playingSong) {
                mediaPlayer?.setDataSource(songs!!.get(songNumber).path)
                mediaPlayer?.prepareAsync()
            } else {
                mediaPlayer?.start()
            }
        } catch (e: IOException) {
            //e.printStackTrace();
        }
        playingSong = true
        return songs!!.get(songNumber).name
    }

    fun pauseSong() {
        mediaPlayer?.pause()
    }

    fun isPlaying(): Boolean {
        return mediaPlayer!!.isPlaying
    }

    fun getFile(): File{
        return songs!!.get(songNumber)
    }

    fun setSong(songPath: String){
        mediaPlayer!!.stop()
        mediaPlayer!!.reset()
        songNumber = songs!!.indexOf(File(songPath))
        playingSong = false
    }

    inner class MyBinder : Binder() {
        fun getService(): MusicPlayerService? {
            getSongs()
            return this@MusicPlayerService
        }
    }


}