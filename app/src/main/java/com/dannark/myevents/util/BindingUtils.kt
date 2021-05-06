package com.dannark.turistando.home

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dannark.myevents.R
import com.dannark.myevents.util.getTimeArrayDiff

@BindingAdapter("timePastFormatted")
fun TextView.timePastFormatted(time: Long){
    time.let{
        val diff = getTimeArrayDiff(it)

        val postTime = when{
            diff.sec < 60 -> String.format(resources.getString(R.string.posted_time_s), diff.sec)
            diff.min < 60 -> String.format(resources.getString(R.string.posted_time_m), diff.min)
            diff.hours < 24 -> String.format(resources.getString(R.string.posted_time_h), diff.hours)
            diff.days < 31 -> String.format(resources.getString(R.string.posted_time_d), diff.days)
            diff.month <= 12 -> String.format(resources.getString(R.string.posted_time_M), diff.month)
            else -> String.format(resources.getString(R.string.posted_time_y), diff.year)

        }

        text = postTime
    }
}

@BindingAdapter("imageUrl", "imageBitmap", "glideCenterCrop","glideCircularCrop", "glideRoundingRadius", requireAll = false)
fun ImageView.bindImage(imgUrl: String?, bitmap: Bitmap?, centerCrop: Boolean = false, circularCrop: Boolean = false, roundingRadius : Int = 0){
    val imgUri = imgUrl?.toUri()?.buildUpon()?.scheme("https")?.build()
    val imgToLoad = bitmap?:imgUri

    // Loads either Bitmap or an Url
    imgToLoad?.let {
        val req = Glide.with(context)
                .load(imgToLoad)
                .apply(
                        RequestOptions()
                                .placeholder(R.drawable.loading_animation)
                                .error(R.drawable.ic_broken_image)
                )
        if (centerCrop) req.centerCrop()
        if (circularCrop) req.circleCrop()
        if (roundingRadius != 0) req.apply(RequestOptions().transform(CenterCrop(), RoundedCorners(roundingRadius)))
        req.into(this)
    }
}

@BindingAdapter("isVisibleOnLoading")
fun LinearLayout.isVisibleOnLoading(isLoading: Boolean){
    if(isLoading) visibility = View.VISIBLE else visibility = View.GONE
}