package com.skysam.hchirinos.textingcurso.profileModule.model.dataAccess

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.common.model.EventErrorTypeListener
import com.skysam.hchirinos.textingcurso.common.model.StorageUploadImageCallback
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseAuthenticationAPI
import com.skysam.hchirinos.textingcurso.common.pojo.User
import com.skysam.hchirinos.textingcurso.profileModule.events.ProfileEvent
import com.skysam.hchirinos.textingcurso.profileModule.events.ProfileEventConst

class Authentication {
    private var mAuthenticationAPI: FirebaseAuth = FirebaseAuthenticationAPI.getInstance()

    fun updateUsernameFirebaseProfile(myUser: User, listener: EventErrorTypeListener) {
        val user = FirebaseAuthenticationAPI.getCurrentUser()
        if (user != null) {
            val profileUpdates: UserProfileChangeRequest = UserProfileChangeRequest.Builder()
                .setDisplayName(myUser.username)
                .build()

            user.updateProfile(profileUpdates).addOnCompleteListener {task ->
                if (!task.isSuccessful) listener.onError(ProfileEventConst.ERROR_PROFILE, R.string.profile_error_userUpdated)
            }
        }
    }

    fun updateImageFirebaseProfile(downloadUri: Uri, callback: StorageUploadImageCallback) {
        val user = FirebaseAuthenticationAPI.getCurrentUser()
        if (user != null) {
            val profileUpdates: UserProfileChangeRequest = UserProfileChangeRequest.Builder()
                .setPhotoUri(downloadUri)
                .build()

            user.updateProfile(profileUpdates).addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    callback.onSuccess(downloadUri)
                } else {
                    callback.onError(R.string.profile_error_imageUpdated)
                }
            }
        }
    }
}