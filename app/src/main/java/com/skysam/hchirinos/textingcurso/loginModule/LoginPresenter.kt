package com.skysam.hchirinos.textingcurso.loginModule

import android.content.Intent
import com.skysam.hchirinos.textingcurso.loginModule.events.LoginEvent

interface LoginPresenter {
    fun onCreate()
    fun onResume()
    fun onPause()
    fun onDestroy()

    fun result(requestCode: Int, resultCode: Int, data: Intent)

    fun getStatusAuth()

    fun onEventListener(event: LoginEvent)
}