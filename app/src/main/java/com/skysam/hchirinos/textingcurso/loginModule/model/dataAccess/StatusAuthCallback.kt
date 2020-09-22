package com.skysam.hchirinos.textingcurso.loginModule.model.dataAccess

import com.google.firebase.auth.FirebaseUser

interface StatusAuthCallback {
    fun onGetUser(user: FirebaseUser)
    fun onLaunchUILogin()
}