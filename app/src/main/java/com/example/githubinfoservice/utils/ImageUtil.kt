package com.example.githubinfoservice.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.githubinfoservice.R

fun ImageView.loadImage(url : String) {

    val options = RequestOptions()

    Glide.with(this.context)
        .setDefaultRequestOptions(options)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .into(this)
}