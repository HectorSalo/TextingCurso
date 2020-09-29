package com.skysam.hchirinos.textingcurso.mainModule.model.dataAccess

import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.common.UtilsCommon
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseFirestoreAPI
import com.skysam.hchirinos.textingcurso.common.pojo.User
import java.util.*

class FirestoreDatabase {
    private lateinit var mUserEventListener: EventListener<QuerySnapshot>
    private lateinit var mRequestEventListener: EventListener<QuerySnapshot>


    fun subscribeToUserList(myUid: String, listener: UserEventListener) {
            mUserEventListener = EventListener { value, error ->
                if (error != null) {
                    when(error.code) {
                        FirebaseFirestoreException.Code.PERMISSION_DENIED -> listener.onError(R.string.main_error_permission_denied)
                        else -> listener.onError(R.string.common_error_server)
                    }
                    return@EventListener
                }

                for (dc in value!!.documentChanges) {
                    when(dc.type) {
                        DocumentChange.Type.ADDED -> listener.onUserAdded(getUser(dc))
                        DocumentChange.Type.MODIFIED -> listener.onUserUpdated(getUser(dc))
                        DocumentChange.Type.REMOVED -> listener.onUserRemoved(getUser(dc))
                    }
                }
            }
        FirebaseFirestoreAPI.getContactsReference(myUid).addSnapshotListener(mUserEventListener)
    }

    private fun getUser(value: DocumentChange): User {
        val user = User()
        user.uid = value.document.id
        return user
    }


    fun subscribeToRequests(email: String, listener: UserEventListener) {
        mRequestEventListener = EventListener { value, error ->
            if (error != null) {
                when(error.code) {
                    FirebaseFirestoreException.Code.PERMISSION_DENIED -> listener.onError(R.string.main_error_permission_denied)
                    else -> listener.onError(R.string.common_error_server)
                }
                return@EventListener
            }

            for (dc in value!!.documentChanges) {
                when(dc.type) {
                    DocumentChange.Type.ADDED -> listener.onUserAdded(getUser(dc))
                    DocumentChange.Type.MODIFIED -> listener.onUserUpdated(getUser(dc))
                    DocumentChange.Type.REMOVED -> listener.onUserRemoved(getUser(dc))
                }
            }
        }
        val emailEncoded = UtilsCommon.getEmailEncoded(email)
        FirebaseFirestoreAPI.getRequestReference(emailEncoded).addSnapshotListener(mRequestEventListener)
    }


    fun unsubscribeToUsers(uid: String) {
        FirebaseFirestoreAPI.getContactsReference(uid).addSnapshotListener(mUserEventListener).remove()
    }

    fun unsubscribeToRequests(email: String) {
        val emailEncoded = UtilsCommon.getEmailEncoded(email)
        FirebaseFirestoreAPI.getRequestReference(emailEncoded).addSnapshotListener(mRequestEventListener).remove()
    }

}