package com.example.karol.musicapp.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.karol.musicapp.Parser
import com.example.karol.musicapp.fragments.ImageFragment

const val itemsSize = 4

class ImageAdapter(fragmentManager: FragmentManager, private var parser: Parser?):FragmentStatePagerAdapter(fragmentManager)  {


    override fun getCount(): Int {
        return itemsSize
    }

    override fun getItem(position: Int): Fragment {
        return ImageFragment.newInstance(parser!!.getImage(position))
    }
}