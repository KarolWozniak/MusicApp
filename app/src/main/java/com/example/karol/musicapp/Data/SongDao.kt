package com.example.karol.musicapp.Data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query

@Dao interface SongDao {

    @Query("select * from song")
    fun getAllSongs(): List<Song>

    @Query("select * from song where name = :name")
    fun findSongByName(name: String?): Song

    @Insert(onConflict = REPLACE)
    fun insertSong(song: Song)

    @Delete
    fun deleteSong(song: Song)

}