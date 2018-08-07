package com.example.karol.musicapp.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.karol.musicapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_image.*

private const val IMAGE = "imageUrl"

class ImageFragment : Fragment() {

    private var imageUrl = ""
    companion object {

        fun newInstance(imageUrl: String): ImageFragment {
            val args = Bundle()
            args.putString(IMAGE, imageUrl)
            val fragment = ImageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageUrl =  arguments.getString(IMAGE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Picasso.get().load(imageUrl).into(songImage)
    }

}
