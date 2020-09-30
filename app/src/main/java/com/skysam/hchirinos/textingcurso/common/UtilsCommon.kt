package com.skysam.hchirinos.textingcurso.common

import android.content.Context
import android.os.Build
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

object UtilsCommon {

    const val ONLINE = true
    const val OFFLINE = false
    const val ONLINE_VALUE: Long = -1

    fun getEmailEncoded(email: String) : String {
        val preKey = email.replace("_", "__")
        return preKey.replace(".", "_")
    }

    fun loadImage (context: Context, url: String, target: ImageView) {
        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()

        Glide.with(context).load(url).apply(options).into(target)
    }

    fun hasMaterialDesing() : Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }
}