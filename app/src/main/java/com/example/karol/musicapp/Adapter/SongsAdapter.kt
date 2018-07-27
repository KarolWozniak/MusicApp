package com.example.karol.musicapp.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.karol.musicapp.PlayerActivity
import com.example.karol.musicapp.R
import kotlinx.android.synthetic.main.data_item.view.*
import java.io.File

class SongsAdapter (val items : ArrayList<File>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.data_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.list_item?.text = items.get(position).name
        holder.path = items.get(position).path
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    val list_item = view.list_item
    var path = ""

    override fun onClick(p0: View?) {
        val context = itemView.context
        val songIntent = Intent(context, PlayerActivity::class.java)
        songIntent.putExtra("SONG_PATH", path)
        context.startActivity(songIntent)
    }

    init {
        view.setOnClickListener(this)
    }
}