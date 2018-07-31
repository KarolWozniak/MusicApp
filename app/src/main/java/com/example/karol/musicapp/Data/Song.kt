package com.example.karol.musicapp.Data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "song")
data class Song(@ColumnInfo(name="name") var name: String,
                @PrimaryKey(autoGenerate = false)@ColumnInfo(name="image")var image: String) {
}