package com.skysam.hchirinos.textingcurso.profileModule.view

import android.content.Intent

interface ProfileView {
    fun enableUIElements()
    fun disableUIElements()

    fun showProgress()
    fun hideProgress()
    fun showProgressImage()
    fun hideProgressImage()

    fun showUserData(username: String, email: String, photoUrl: String)
    fun launchGallery()
    fun openDialogPreview(data: Intent)

    fun menuEditMode()
    fun menuNormalMode()

    fun saveUsernameSuccess()
    fun updateImageSucces(photoUrl: String)
    fun setResultOK(username: String, photoUrl: String)

    fun onErrorUpload(resMsg: Int)
    fun onError(resMsg: Int)
}