package com.skysam.hchirinos.textingcurso.profileModule.model.dataAccess

import android.app.Activity
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.common.model.StorageUploadImageCallback
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseStorageAPI

class Storage {
    fun uploadImageProfile(imageUri: Uri, email: String, callback: StorageUploadImageCallback) {
        if (imageUri.lastPathSegment != null) {
            val photoRef: StorageReference = FirebaseStorageAPI.getPhotosReferenceByEmail(email)
                .child(StorageConst.PATH_PROFILE).child(imageUri.lastPathSegment!!)

            photoRef.putFile(imageUri).addOnSuccessListener { task ->
                task.storage.downloadUrl.addOnSuccessListener { uri ->
                    if (uri != null) {
                        callback.onSuccess(uri)
                    } else {
                        callback.onError(R.string.profile_error_imageUpdated)
                    }
                }
            }
        } else {
            callback.onError(R.string.profile_error_invalid_image)
        }
    }

    fun deleteOldImage(oldPhotoUrl: String, downloadUrl: String) {
        if (oldPhotoUrl.isNotEmpty()) {
            val storageReference = FirebaseStorageAPI.getInstance().getReferenceFromUrl(downloadUrl)
            var oldStorageReference: StorageReference? = null
            try {
                oldStorageReference = FirebaseStorageAPI.getInstance().getReferenceFromUrl(oldPhotoUrl)
            } catch (e: Exception) {
            }

            if (oldStorageReference!= null && !oldStorageReference.path.equals(storageReference.root)) {
                oldStorageReference.delete()
            }
        }
    }
}