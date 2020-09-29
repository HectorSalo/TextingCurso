package com.skysam.hchirinos.textingcurso.common.model.dataAccess

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firestore.v1.DocumentTransform
import com.skysam.hchirinos.textingcurso.common.UtilsCommon
import com.skysam.hchirinos.textingcurso.common.pojo.UserConst

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

    fun getUsersReference() : CollectionReference {
        return getInstance().collection(PATH_USERS)
    }

    fun updateMyLastConnection(online: Boolean, uid: String) {
        updateMyLastConnection(online, "", uid)
    }

    fun updateMyLastConnection(online: Boolean, uidFriend: String, uid: String) {
        val lastConnectionWith = "${UtilsCommon.ONLINE_VALUE}$SEPARATOR$uidFriend"
        val timesTamp: String = FieldValue.serverTimestamp().toString()

        val value = if (online) lastConnectionWith else timesTamp

        getUserReferenceByUid(uid).update(UserConst.LAST_CONNECTION_WITH, value)
    }

}