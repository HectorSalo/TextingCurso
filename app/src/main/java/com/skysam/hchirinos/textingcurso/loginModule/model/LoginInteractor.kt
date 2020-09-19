package com.skysam.hchirinos.textingcurso.loginModule.model

interface LoginInteractor {
    fun onResume()
    fun onPause()

    fun getStatusAuth()
}