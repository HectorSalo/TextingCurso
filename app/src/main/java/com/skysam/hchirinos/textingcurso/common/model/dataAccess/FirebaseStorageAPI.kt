package com.skysam.hchirinos.textingcurso.common.model.dataAccess

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.skysam.hchirinos.textingcurso.common.UtilsCommon

object FirebaseStorageAPI {
    private var mFirebaseStorage: FirebaseStorage? = null

    fun getInstance() : FirebaseStorage {
        if (mFirebaseStorage == null) {
            mFirebaseStorage = FirebaseStorage.getInstance()
        }
        return mFirebaseStorage!!
    }

    fun getPhotosReferenceByEmail(email: String) : StorageReference {
        return getInstance().reference.child(UtilsCommon.getEmailEncoded(email))
    }
}