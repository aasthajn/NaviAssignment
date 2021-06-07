package com.example.githubinfoservice.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*

class AppUtils {

    companion object {

        fun dateFormatted(date: String, format: String): String? {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
            simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val parsedDate = simpleDateFormat.parse(date)

            val format2 = SimpleDateFormat(format, Locale.ENGLISH)
            return format2.format(parsedDate)
        }
    }
}