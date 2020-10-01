package com.skysam.hchirinos.textingcurso.profileModule

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.skysam.hchirinos.textingcurso.profileModule.events.ProfileEvent

interface ProfilePresenter {
    fun onCreate()
    fun onDestroy()

    fun setupUser(username: String, email: String, photoUrl: String)
    fun checkMode()

    fun updateUsername(username: String)
    fun updateImage(uri: Uri)

    fun result(requestCode: Int, resultCode: Int, data: Intent?)

    fun onEventListener(event: ProfileEvent)
}