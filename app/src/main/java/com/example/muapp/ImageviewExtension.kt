package com.example.muapp

import android.widget.ImageView
import com.bumptech.glide.Glide


fun ImageView.load(url :String)
{
    if(url.isNotEmpty()){
        Glide.with(this)
            .load(url)
            .placeholder(R.color.gray)
            .error(R.drawable.ic_home)
            .into(this)
    }
    else{
        setImageResource(R.drawable.ic_image_not_supported)
    }
}