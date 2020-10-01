package com.skysam.hchirinos.textingcurso.profileModule.model

import android.app.Activity
import android.net.Uri

interface ProfileInteractor {
    fun updateUsername(username: String)
    fun updateImage(uri: Uri, oldPhotoUrl: String)
}