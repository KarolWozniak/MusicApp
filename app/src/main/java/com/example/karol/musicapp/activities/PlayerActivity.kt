package com.example.karol.musicapp.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import cafe.adriel.androidaudioconverter.AndroidAudioConverter
import cafe.adriel.androidaudioconverter.callback.IConvertCallback
import cafe.adriel.androidaudioconverter.model.AudioFormat
import com.example.karol.musicapp.MusicApp
import com.example.karol.musicapp.R
import com.example.karol.musicapp.services.MusicPlayerService
import com.roger.catloadinglibrary.CatLoadingView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_player.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File

class PlayerActivity: AppCompatActivity() {

    private lateinit var songName: String
    private lateinit var musicService: MusicPlayerService
    private lateinit var playIntent: Intent

    private var catProgress: CatLoadingView = CatLoadingView()
    private val musicConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            val binder = iBinder as MusicPlayerService.MyBinder
            musicService = binder.getService()
            getExtras()
            Log.d("PlayerActivity", "MusicService is connected!")
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            Log.d("PlayerActivity", "MusicService is disconnected!")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        setSupportActionBar(my_toolbar)
    }

    override fun onStart() {
        super.onStart()
        playIntent = Intent(this, MusicPlayerService::class.java)
        startService(playIntent)
        bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onRestart() {
        songName = musicService.getFile().name
        song_name.text = musicService.getFile().name
        setImage()
        super.onRestart()
    }

    override fun onDestroy() {
        stopService(playIntent)
        super.onDestroy()
    }

    fun getExtras() {
        val b = intent.extras
        if (b != null) {
            val path = b.getString("SONG_PATH")
            musicService.setSong(path)
            changePlayButton()
        }
    }

    fun checkMp3() {
        if (songName.contains(".mp3"))
            fab.visibility = View.INVISIBLE
        else
            fab.visibility = View.VISIBLE
    }

    private fun convertFileToMp3(file: File) {
        startAnimation()
        val callback = object : IConvertCallback {
            override fun onSuccess(convertedFile: File) {
                Log.d("MainActivity", "Saving ends successfully")
                file.delete()
                this@PlayerActivity.runOnUiThread { refreshList() }
            }

            override fun onFailure(error: Exception) {
                Log.d("MainActivity", "Something went wrong with converting!")
            }
        }
        AndroidAudioConverter.with(this@PlayerActivity)
                .setFile(file)
                .setFormat(AudioFormat.MP3)
                .setCallback(callback)
                .convert()
    }

    fun startAnimation() {
        catProgress.show(supportFragmentManager, "")
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        catProgress.setText("Converting ...")
        catProgress.isCancelable = false
    }

    fun stopAnimation() {
        catProgress.dismiss()
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun refreshList() {
        musicService.getSongs()
        stopAnimation()
    }

    fun changePlayButton() {
        if (musicService.isPlaying()) {
            play_button.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play))
            musicService.pauseSong()
        } else {
            play_button.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause))
            songName = musicService.playSong()
            song_name.text = songName
            checkMp3()
            setImage()
        }
    }

    fun setImage() {
        doAsync {
            var songTitle = songName.replace(".webm","").replace(".mp3","")
            var res = MusicApp.database.songDao().findSongByName(songTitle).image
            uiThread {
                Picasso.get().load(res).into(imageview)
            }
        }
    }

    fun nextSong(view: View) {
        musicService.nextSong()
        changePlayButton()
    }

    fun prevSong(view: View) {
        musicService.prevSong()
        changePlayButton()
    }

    fun gotoList(view: View) {
        val intent = Intent(this, SongsListActivity::class.java)
        startActivity(intent)
    }

    fun playButton(view: View) {
        changePlayButton()
    }

    fun onFabClick() {
        convertFileToMp3(musicService.getFile())
    }

}

