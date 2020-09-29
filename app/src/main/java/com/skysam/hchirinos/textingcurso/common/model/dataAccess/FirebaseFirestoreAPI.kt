package com.skysam.hchirinos.textingcurso.common.model.dataAccess

import com.google.firebase.firestore.CollectionReference
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

    fun getContactsReference(uid: String): CollectionReference {
        return getUserReferenceByUid(uid).collection(PATH_CONTACTS)
    }

    fun getRequestReference(email: String) : CollectionReference {
        return getInstance().collection(PATH_REQUESTS)
    }

    fun getUserReference() : CollectionReference {
        return getInstance().collection(PATH_USERS)
    }


}