package com.example.karol.musicapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_songs_list.*
import java.io.File

class SongsListActivity : AppCompatActivity() {

    val songsList: ArrayList<File> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs_list)
        addSongs()
        songs_list.layoutManager = LinearLayoutManager(this)
        songs_list.adapter = SongsAdapter(songsList, this)
    }

    fun addSongs(){
        val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        for (file in filePath.listFiles()!!) {
            songsList.add(file)
        }
    }
}
