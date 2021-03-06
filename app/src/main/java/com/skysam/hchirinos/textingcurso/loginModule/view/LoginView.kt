package com.skysam.hchirinos.textingcurso.loginModule.view

import android.content.Intent

interface LoginView {
    fun showProgress()
    fun hideProgress()

    fun openMainActivity()
    fun openUILogin()

    fun showLoginSuccessfully(data: Intent)
    fun showMessageStarting()
    fun showError(reaMsg: Int)
}