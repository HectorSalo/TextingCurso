package com.skysam.hchirinos.textingcurso.profileModule

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.skysam.hchirinos.textingcurso.common.pojo.User
import com.skysam.hchirinos.textingcurso.profileModule.events.ProfileEvent
import com.skysam.hchirinos.textingcurso.profileModule.events.ProfileEventConst
import com.skysam.hchirinos.textingcurso.profileModule.model.ProfileInteractor
import com.skysam.hchirinos.textingcurso.profileModule.model.ProfileInteractorClass
import com.skysam.hchirinos.textingcurso.profileModule.view.ProfileActivity
import com.skysam.hchirinos.textingcurso.profileModule.view.ProfileView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ProfilePresenterClass (private var mView: ProfileView?): ProfilePresenter{

    private val mInteractor: ProfileInteractor = ProfileInteractorClass()
    private var isEdit = false
    private lateinit var mUser: User

    override fun onCreate() {
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        mView = null
    }

    override fun setupUser(username: String, email: String, photoUrl: String) {
        mUser = User()
        mUser.username = username
        mUser.email = email
        mUser.photoUrl = photoUrl

        mView!!.showUserData(username, email, photoUrl)
    }

    override fun checkMode() {
        if (isEdit) mView!!.launchGallery()
    }

    override fun updateUsername(username: String) {
        if (isEdit) {
            if (setProgress()) {
                mView!!.showProgress()
                mInteractor.updateUsername(username)
                mUser.username = username
            }
        } else {
            isEdit = true
            mView!!.menuEditMode()
            mView!!.enableUIElements()
        }
    }

    override fun updateImage(uri: Uri) {
        if (setProgress()) {
            mView!!.showProgressImage()
            mInteractor.updateImage(uri, mUser.photoUrl!!)
        }
    }

    override fun result(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                ProfileActivity.Const.RC_PHOTO_PICKER -> mView!!.openDialogPreview(data!!)
            }
        }
    }

    @Subscribe
    override fun onEventListener(event: ProfileEvent) {
        if (mView != null) {
            mView!!.hideProgress()

            when(event.typeEvent) {
                ProfileEventConst.UPLOAD_IMAGE -> {
                    mView!!.updateImageSucces(event.photoUrl!!)
                    mUser.photoUrl = event.photoUrl
                    mView!!.menuNormalMode()
                    mView!!.setResultOK(mUser.username!!, mUser.photoUrl!!)
                    isEdit = false
                }
                ProfileEventConst.SAVE_USERNAME -> {
                    mView!!.saveUsernameSuccess()
                    mView!!.menuNormalMode()
                    mView!!.setResultOK(mUser.username!!, mUser.photoUrl!!)
                    isEdit = false
                }
                ProfileEventConst.ERROR_USERNAME -> {
                mView!!.enableUIElements()
                mView!!.onError(event.resMsg!!)
            }
                ProfileEventConst.ERROR_IMAGE -> {
                    mView!!.enableUIElements()
                    mView!!.onErrorUpload(event.resMsg!!)
                }
                ProfileEventConst.ERROR_PROFILE -> {
                mView!!.enableUIElements()
                mView!!.onErrorUpload(event.resMsg!!)
            }
                ProfileEventConst.ERROR_SERVER -> {
                    mView!!.enableUIElements()
                    mView!!.onErrorUpload(event.resMsg!!)
                }
            }
        }
    }

    private fun setProgress() : Boolean{
        if (mView != null) {
            mView!!.disableUIElements()
            return true
        }
        return false
    }
}