package com.example.karol.musicapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import com.example.karol.musicapp.R
import com.example.karol.musicapp.adapter.SongsAdapter
import kotlinx.android.synthetic.main.activity_songs_list.*
import java.io.File

class SongsListActivity : AppCompatActivity() {

    private val songsList: ArrayList<File> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs_list)
        addSongs()
        songs_list.layoutManager = LinearLayoutManager(this)
        songs_list.adapter = SongsAdapter(songsList, this)
    }

    fun addSongs(){
        val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        songsList += filePath.listFiles()
    }
}
