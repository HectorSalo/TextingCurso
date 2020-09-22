package com.skysam.hchirinos.textingcurso.common.model.dataAccess

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseFirestoreAPI {
    const val SEPARATOR = "___&___"
    const val PATH_USERS = "users"
    const val PATH_CONTACTS = "contacts"
    const val PATH_REQUESTS = "requests"

    private var mDatabaseReference: FirebaseFirestore? = null

    fun getInstance() : FirebaseFirestore {
        if (mDatabaseReference == null) {
            mDatabaseReference = FirebaseFirestore.getInstance()
        }
        return mDatabaseReference!!
    }

    fun getUserReferenceByUid(uid: String): DocumentReference {
        return getInstance().collection(PATH_USERS).document(uid)
    }


}