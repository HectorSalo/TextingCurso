package com.skysam.hchirinos.textingcurso.profileModule.model.dataAccess

import android.net.Uri
import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.common.model.StorageUploadImageCallback
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseFirestoreAPI
import com.skysam.hchirinos.textingcurso.common.pojo.User
import com.skysam.hchirinos.textingcurso.common.pojo.UserConst

class FirestoreDatabase {
    
    fun changeUsername(myUser: User, listener: UpdateUserListener) {
        FirebaseFirestoreAPI.getUserReferenceByUid(myUser.uid!!)
            .update(UserConst.USERNAME, myUser.username)
            .addOnSuccessListener { 
                listener.onSuccess()
                notifyContactsUsername(myUser, listener)
            }
    }

    private fun notifyContactsUsername(myUser: User, listener: UpdateUserListener) {
        FirebaseFirestoreAPI.getContactsReference(myUser.uid!!).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val friendUid = document.id
                    FirebaseFirestoreAPI.getContactsReference(friendUid).document(myUser.uid!!)
                        .update(UserConst.USERNAME, myUser.username)
                    listener.onNotifyContacts()
                }
            }
            .addOnFailureListener { listener.onError(R.string.profile_error_userUpdated) }
    }

    fun updatePhotoUrl(downloadUri: Uri, myUid: String, callback: StorageUploadImageCallback) {
        FirebaseFirestoreAPI.getUserReferenceByUid(myUid)
            .update(UserConst.PHOTO_URL, downloadUri.toString())
            .addOnSuccessListener {
                callback.onSuccess(downloadUri)
                notifyContactsPhoto(downloadUri.toString(), myUid, callback)
            }
    }

    private fun notifyContactsPhoto(
        photoUrl: String,
        myUid: String,
        callback: StorageUploadImageCallback
    ) {
        FirebaseFirestoreAPI.getContactsReference(myUid).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val friendUid = document.id
                    FirebaseFirestoreAPI.getContactsReference(friendUid).document(myUid)
                        .update(UserConst.PHOTO_URL, photoUrl)
                }
            }
            .addOnFailureListener { callback.onError(R.string.profile_error_imageUpdated) }
    }

}