package com.skysam.hchirinos.textingcurso.chatModule.model.dataAccess

import android.app.Activity
import android.net.Uri
import com.google.firebase.storage.StorageReference
import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.common.model.StorageUploadImageCallback
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseStorageAPI
import com.skysam.hchirinos.textingcurso.chatModule.model.dataAccess.StorageConst

class Storage {
    fun uploadImageChat(activity: Activity, imageUri: Uri, myEmail: String, callback: StorageUploadImageCallback) {
        if (imageUri.lastPathSegment != null) {
            val photoRef: StorageReference = FirebaseStorageAPI.getPhotosReferenceByEmail(myEmail)
                .child(StorageConst.PATH_CHATS).child(imageUri.lastPathSegment!!)

            photoRef.putFile(imageUri).addOnSuccessListener { task ->
                task.storage.downloadUrl.addOnSuccessListener { uri ->
                    if (uri != null) {
                        callback.onSuccess(uri)
                    } else {
                        callback.onError(R.string.chat_error_imageUpload)
                    }
                }
            }
        } else {
            callback.onError(R.string.chat_error_imageUpload)
        }
    }
}