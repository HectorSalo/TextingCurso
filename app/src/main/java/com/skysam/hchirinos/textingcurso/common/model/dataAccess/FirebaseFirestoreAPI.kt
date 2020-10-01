package com.skysam.hchirinos.textingcurso.common.model.dataAccess

import com.google.firebase.firestore.*
import com.google.firestore.v1.DocumentTransform
import com.skysam.hchirinos.textingcurso.chatModule.model.dataAccess.FirestoreDatabaseConst
import com.skysam.hchirinos.textingcurso.common.UtilsCommon
import com.skysam.hchirinos.textingcurso.common.pojo.UserConst
import java.sql.Timestamp
import java.time.LocalDate.now
import java.util.*


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
        return getInstance().collection(PATH_REQUESTS).document(PATH_REQUESTS).collection(email)
    }

    fun getUsersReference() : CollectionReference {
        return getInstance().collection(PATH_USERS)
    }

    fun getChatsReference(keyChat: String) : DocumentReference {
        return getInstance().collection(FirestoreDatabaseConst.PATH_CHATS).document(keyChat)
    }

    fun updateMyLastConnection(online: Boolean, uid: String) {
        updateMyLastConnection(online, "", uid)
    }

    fun updateMyLastConnection(online: Boolean, uidFriend: String, uid: String) {

        val lastConnectionWith = "${UtilsCommon.ONLINE_VALUE}$SEPARATOR$uidFriend"

        if (online) {
            getUserReferenceByUid(uid).update(UserConst.LAST_CONNECTION_WITH, lastConnectionWith)
        } else {
            getUserReferenceByUid(uid).update(UserConst.LAST_CONNECTION_WITH, FieldValue.serverTimestamp())
        }


    }

}