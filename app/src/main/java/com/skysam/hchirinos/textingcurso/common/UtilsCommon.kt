package com.skysam.hchirinos.textingcurso.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.skysam.hchirinos.textingcurso.R
import java.io.File
import java.io.FileNotFoundException
import kotlin.math.ceil
import kotlin.math.max


object UtilsCommon {

    const val ONLINE = true
    const val OFFLINE = false
    const val ONLINE_VALUE: Long = -1
    const val RC_PHOTO_PICKER: Int = 22

    const val RP_STORAGE = 23

    const val METHOD = "method"
    const val TITLE = "title"
    const val MESSAGE = "message"
    const val TOPIC = "topic"
    const val SUCCESS = "success"

    const val PARAM_CONTEXT = "param_context"
    const val ADD_FRIEND = "add_friend"


    fun getEmailEncoded(email: String) : String {
        val preKey = email.replace("_", "__")
        return preKey.replace(".", "_")
    }

    fun getEmailToTopic(email: String): String {
        var topic = getEmailEncoded(email)
        topic = topic.replace("@", "_64")
        return topic
    }

    fun loadImage(context: Context, url: String, target: ImageView) {
        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()

        Glide.with(context).load(url).apply(options).into(target)
    }

    fun reduceBitmap(
        context: Context,
        container: View?,
        uri: String?,
        maxAncho: Int,
        maxAlto: Int
    ): Bitmap? {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(Uri.parse(uri)),
                null, options
            )
            options.inSampleSize = max(
                ceil(options.outWidth / maxAncho.toDouble()),
                ceil(options.outHeight / maxAlto.toDouble())
            ).toInt()
            options.inJustDecodeBounds = false
            BitmapFactory.decodeStream(
                context.contentResolver
                    .openInputStream(Uri.parse(uri)), null, options
            )
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Snackbar.make(container!!, R.string.profile_error_notfound, Snackbar.LENGTH_LONG).show()
            null
        }
    }

    fun deleteTempFiles(file: File) {
        if (file.isDirectory) {
            val files: Array<File>? = file.listFiles()
            if (files != null) {
                for (f in files) {
                    if (f.isDirectory) {
                        deleteTempFiles(f)
                    } else {
                        f.delete()
                    }
                }
            }
        }
    }

    fun validateMessage(etMessage: AppCompatEditText): Boolean {
        return etMessage.text != null && etMessage.text!!.isNotEmpty()
    }

    fun isOnline (context: Context) : Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }
}