package com.skysam.hchirinos.textingcurso

import android.app.Application
import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley


class TextingApplication: Application() {

    companion object {
        private lateinit var mInstance: TextingApplication
        private lateinit var mRequestQueue: RequestQueue
        lateinit  var appContext: Context
    }


    override fun onCreate() {
        super.onCreate()
        mInstance = this
        appContext = applicationContext
    }

    object TextingApplicationObject {
        fun getInstance(): TextingApplication {
            return mInstance
        }

        fun getmRequestQueue(): RequestQueue {
            mRequestQueue = Volley.newRequestQueue(appContext)
            return mRequestQueue
        }

        fun <T> addToReqQueue(request: Request<T>) {
            request.retryPolicy =
                DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            getmRequestQueue().add(request)
        }

    }


}

