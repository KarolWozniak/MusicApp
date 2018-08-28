package com.example.karol.musicapp.activities

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.karol.musicapp.adapter.DataAdapter
import com.example.karol.musicapp.Data.Song
import com.example.karol.musicapp.MusicApp
import com.example.karol.musicapp.Parser
import com.example.karol.musicapp.R
import com.example.karol.musicapp.adapter.ImageAdapter
import com.roger.catloadinglibrary.CatLoadingView
import kotlinx.android.synthetic.main.activity_main.*
import com.example.karol.musicapp.network.DataApiHelper
import com.example.karol.musicapp.network.DownloadApiHelper
import org.jetbrains.anko.doAsync

class MainActivity: FragmentActivity() {

    private lateinit var data: DataApiHelper
    private lateinit var parser: Parser

    private var isRunning: Boolean = false
    private var progressVisible: Boolean = false
    private var progressStop: Boolean = false

    private lateinit var listAdapter: RecyclerView.Adapter<*>
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private var catProgress =  CatLoadingView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getIntentData()
        checkNotification()
        audio_list.setHasFixedSize(true)
        this.mLayoutManager = LinearLayoutManager(this)
        audio_list.layoutManager = mLayoutManager
        this.isRunning = true
    }

    fun getIntentData() {
        val intent = intent
        val action = intent.action
        val type = intent.type
        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                val urlNew = intent.getStringExtra(Intent.EXTRA_TEXT)
                editText.setText(urlNew)
            }
        }
    }

    fun check(view: View) {
        parser = Parser(editText.text.toString())
        text.text = parser.right_link
        data = DataApiHelper(parser.right_link, this)
    }

    fun show() {
        listAdapter = DataAdapter(data.video, this)
        audio_list.adapter = listAdapter
        text.text = data.video.title
        pagerAdapter = ImageAdapter(supportFragmentManager,parser)
        imagePager.adapter = pagerAdapter
    }

    private fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                return false
            }
        } else {
            return true
        }
    }

    fun downloadUrl(url: String) {
        progressVisible = true
        catProgress.show(supportFragmentManager, "")
        val a = isStoragePermissionGranted()
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        catProgress.setText("Downloading")
        catProgress.isCancelable = false
        val downloadApi = DownloadApiHelper(data.video.title, url, this)
    }

    fun stopProgress() {
        if (this.isRunning && this.progressVisible) {
            Toast.makeText(this@MainActivity, "Download ends successfully", Toast.LENGTH_SHORT).show()
            catProgress.dismiss()
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            progressVisible = false
            var imageNumber = imagePager.currentItem
            val song = Song(data.video.title, parser.getImage(imageNumber))
            doAsync {
                val db = MusicApp.database
                db.songDao().insertSong(song)
                Log.d("DAO","Insert " + song )
            }
        } else {
            progressStop = true
        }
    }

    fun checkNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val channel = NotificationChannel("MusicApp", name, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = getString(R.string.channel_description)
            val notificationManager = getSystemService(
                    Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun goPlay(view: View) {
        val intent = Intent(this, PlayerActivity::class.java)
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        isRunning = false
    }

    override fun onRestart() {
        super.onRestart()
        isRunning = true
        if (progressStop) {
            stopProgress()
        }
    }

}