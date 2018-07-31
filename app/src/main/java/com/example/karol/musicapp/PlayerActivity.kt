package com.example.karol.musicapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.AsyncTask
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
import com.roger.catloadinglibrary.CatLoadingView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_player.*
import java.io.File

class PlayerActivity: AppCompatActivity() {

    private var songName: String? = null
    private var catProgress: CatLoadingView? = null
    private var musicService: MusicPlayerService? = null
    private var playIntent: Intent? = null
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
        if (playIntent == null) {
            playIntent = Intent(this, MusicPlayerService::class.java)
            startService(playIntent)
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onDestroy() {
        stopService(playIntent)
        musicService = null
        super.onDestroy()
    }

    fun getExtras() {
        val b = intent.extras
        if (b != null) {
            val path = b.getString("SONG_PATH")
            musicService?.setSong(path!!)
            changePlayButton()
        }
    }

    fun checkMp3() {
        if (songName!!.contains(".mp3"))
            fab.setVisibility(View.INVISIBLE)
        else
            fab.setVisibility(View.VISIBLE)
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
        catProgress = CatLoadingView()
        catProgress!!.show(supportFragmentManager, "")
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        catProgress!!.setText("Converting ...")
        catProgress!!.setCancelable(false)
    }

    fun stopAnimation() {
        catProgress?.dismiss()
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun refreshList() {
        musicService?.getSongs()
        stopAnimation()
    }

    fun changePlayButton() {
        if (musicService?.isPlaying()!!) {
            play_button.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play))
            musicService?.pauseSong()
        } else {
            play_button.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause))
            songName = musicService?.playSong()
            song_name.setText(songName)
            checkMp3()
            setImage()
        }
    }

    fun setImage() {
        LoadImageTask().execute()
    }

    fun nextSong(view: View) {
        musicService?.nextSong()
        changePlayButton()
    }

    fun prevSong(view: View) {
        musicService?.prevSong()
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
        convertFileToMp3(musicService!!.getFile())
    }

    private inner class LoadImageTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg strings: String): String? {
            var all = MusicApp.database?.songDao()?.getAllSongs()
            var songTitle = songName?.replace(".webm","")?.replace(".mp3","")
            var res = MusicApp.database?.songDao()?.findSongByName(songTitle)
            return res?.image
        }

        override fun onPostExecute(result: String?) {
            if (result != null)
                Picasso.get().load(result).into(imageview)
            else
                Picasso.get().load("https://img.youtube.com/vi/1G4isv_Fylg/hqdefault.jpg").into(imageview)
        }

    }

}

