package com.skysam.hchirinos.textingcurso.mainModule.model.dataAccess

import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.common.UtilsCommon
import com.skysam.hchirinos.textingcurso.common.model.BasicEventsCallback
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseFirestoreAPI
import com.skysam.hchirinos.textingcurso.common.pojo.User
import com.skysam.hchirinos.textingcurso.common.pojo.UserConst
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

    fun removeUser(friendUid: String, myUid: String, callback: BasicEventsCallback) {
        FirebaseFirestoreAPI.getUsersReference().document(myUid).collection(FirebaseFirestoreAPI.PATH_CONTACTS).document(friendUid).delete()
            .addOnSuccessListener {
                FirebaseFirestoreAPI.getUsersReference().document(friendUid).collection(FirebaseFirestoreAPI.PATH_CONTACTS).document(myUid)
                    .delete()
                    .addOnSuccessListener { callback.onSuccess() }
                    .addOnFailureListener { callback.onError() }
            }
            .addOnFailureListener { callback.onError() }
    }

    fun acceptRequest(user: User, myUser: User, callback: BasicEventsCallback) {
        val userRequestMap = HashMap<String, Any?>()
        userRequestMap[UserConst.USERNAME] = user.username
        userRequestMap[UserConst.EMAIL] = user.email
        userRequestMap[UserConst.PHOTO_URL] = user.photoUrl

        val myUserMap = HashMap<String, Any?>()
        myUserMap[UserConst.USERNAME] = myUser.username
        myUserMap[UserConst.EMAIL] = myUser.email
        myUserMap[UserConst.PHOTO_URL] = myUser.photoUrl

        val emailEncoded = UtilsCommon.getEmailEncoded(myUser.email!!)

        FirebaseFirestoreAPI.getUsersReference().document(user.uid!!).collection(FirebaseFirestoreAPI.PATH_CONTACTS).document(myUser.uid!!)
            .set(myUserMap)
            .addOnSuccessListener {
                FirebaseFirestoreAPI.getUsersReference().document(myUser.uid!!).collection(FirebaseFirestoreAPI.PATH_CONTACTS).document(user.uid!!)
                    .set(userRequestMap)
                    .addOnSuccessListener {
                        FirebaseFirestoreAPI.getRequestReference(emailEncoded).document(FirebaseFirestoreAPI.PATH_REQUESTS).collection(emailEncoded).document(user.uid!!)
                            .delete()
                            .addOnSuccessListener { callback.onSuccess() }
                            .addOnFailureListener { callback.onError() }
                    }
                    .addOnFailureListener { callback.onError() }
            }
            .addOnFailureListener { callback.onError() }
    }

    fun denyRequest(user: User, myEmail: String, callback: BasicEventsCallback) {
        val emailEncoded = UtilsCommon.getEmailEncoded(myEmail)

        FirebaseFirestoreAPI.getRequestReference(emailEncoded).document(FirebaseFirestoreAPI.PATH_REQUESTS).collection(emailEncoded)
            .document(user.uid!!).delete()
            .addOnSuccessListener { callback.onSuccess() }
            .addOnFailureListener { callback.onError() }
    }
}