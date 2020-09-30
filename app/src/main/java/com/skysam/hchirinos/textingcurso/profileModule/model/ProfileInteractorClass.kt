package com.skysam.hchirinos.textingcurso.profileModule.model

import android.app.Activity
import android.net.Uri
import com.skysam.hchirinos.textingcurso.common.model.EventErrorTypeListener
import com.skysam.hchirinos.textingcurso.common.model.StorageUploadImageCallback
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseAuthenticationAPI
import com.skysam.hchirinos.textingcurso.common.pojo.User
import com.skysam.hchirinos.textingcurso.mainModule.events.MainEvent
import com.skysam.hchirinos.textingcurso.profileModule.events.ProfileEvent
import com.skysam.hchirinos.textingcurso.profileModule.events.ProfileEventConst
import com.skysam.hchirinos.textingcurso.profileModule.model.dataAccess.Authentication
import com.skysam.hchirinos.textingcurso.profileModule.model.dataAccess.FirestoreDatabase
import com.skysam.hchirinos.textingcurso.profileModule.model.dataAccess.Storage
import com.skysam.hchirinos.textingcurso.profileModule.model.dataAccess.UpdateUserListener
import org.greenrobot.eventbus.EventBus

class ProfileInteractorClass : ProfileInteractor {

    private var mAuthentication: Authentication = Authentication()
    private var mDatabase: FirestoreDatabase = FirestoreDatabase()
    private var mStorage: Storage = Storage()

    private fun getCurrentUser() : User {
        return FirebaseAuthenticationAPI.getAuthUser()
    }

    override fun updateUsername(username: String) {
        val myUser = getCurrentUser()
        myUser.username = username
        mDatabase.changeUsername(myUser, object : UpdateUserListener{
            override fun onSuccess() {
                mAuthentication.updateUsernameFirebaseProfile(myUser, object : EventErrorTypeListener{
                    override fun onError(typeEvent: Int, reaMsg: Int) {
                        post(typeEvent, null, reaMsg)
                    }
                })
            }

            override fun onNotifyContacts() {
                postUsernameSuccess()
            }

            override fun onError(resMsg: Int) {
                post(ProfileEventConst.ERROR_USERNAME, null, resMsg)
            }
        })
    }

    override fun updateImage(activity: Activity, uri: Uri, oldPhotoUrl: String) {
        mStorage.uploadImageProfile(activity, uri, getCurrentUser().email!!, object : StorageUploadImageCallback{
            override fun onSuccess(newUri: Uri) {
                mDatabase.updatePhotoUrl(newUri, getCurrentUser().uid!!, object : StorageUploadImageCallback{
                    override fun onSuccess(newUri: Uri) {
                        post(ProfileEventConst.UPLOAD_IMAGE, newUri.toString(), 0)
                    }

                    override fun onError(resMsg: Int) {
                        post(ProfileEventConst.ERROR_SERVER, resMsg)
                    }
                })

                mAuthentication.updateImageFirebaseProfile(uri, object : StorageUploadImageCallback{
                    override fun onSuccess(newUri: Uri) {
                        mStorage.deleteOldImage(oldPhotoUrl, newUri.toString())
                    }

                    override fun onError(resMsg: Int) {
                        post(ProfileEventConst.ERROR_PROFILE, resMsg)
                    }
                })
            }

            override fun onError(resMsg: Int) {
                post(ProfileEventConst.ERROR_IMAGE, resMsg)
            }
        })
    }


    private fun postUsernameSuccess() {
        post(ProfileEventConst.SAVE_USERNAME, null, 0)
    }


    private fun post(typeEvent: Int, resMsg: Int) {
        post(typeEvent, null, resMsg)
    }

    private fun post(typeEvent: Int, photoUrl: String?, resMsg: Int) {
        val event = ProfileEvent()
        event.typeEvent = typeEvent
        event.photoUrl = photoUrl
        event.resMsg = resMsg
        EventBus.getDefault().post(event)
    }
}