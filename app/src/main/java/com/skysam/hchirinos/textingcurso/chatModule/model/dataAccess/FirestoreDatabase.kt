package com.skysam.hchirinos.textingcurso.chatModule.model.dataAccess

import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.chatModule.model.LastConnectionEventListener
import com.skysam.hchirinos.textingcurso.chatModule.model.MessagesEventListener
import com.skysam.hchirinos.textingcurso.common.UtilsCommon
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseFirestoreAPI
import com.skysam.hchirinos.textingcurso.common.pojo.Message
import com.skysam.hchirinos.textingcurso.common.pojo.User
import com.skysam.hchirinos.textingcurso.common.pojo.UserConst
import java.util.*

class FirestoreDatabase {
    private lateinit var mMessagesEventListener: EventListener<QuerySnapshot>
    private lateinit var mFriendProfileListener: EventListener<DocumentSnapshot>

    fun subscribeToMessages(myEmail: String, friendEmail: String, listener: MessagesEventListener) {
        mMessagesEventListener = EventListener { value, error ->
            if (error != null) {
                when(error.code) {
                    FirebaseFirestoreException.Code.PERMISSION_DENIED -> listener.onError(R.string.chat_error_permission_denied)
                    else -> listener.onError(R.string.common_error_server)
                }
                return@EventListener
            }

            for (dc in value!!.documentChanges) {
                when(dc.type) {
                    DocumentChange.Type.ADDED -> listener.onMessageReceived(getMessage(dc))
                    DocumentChange.Type.MODIFIED -> {}
                    DocumentChange.Type.REMOVED -> {}
                }
            }
        }
        getChatMessagesReference(myEmail, friendEmail).addSnapshotListener(mMessagesEventListener)
    }

    private fun getChatMessagesReference(myEmail: String, friendEmail: String) : CollectionReference {
        return getChatsReference(myEmail, friendEmail).collection(FirestoreDatabaseConst.PATH_MESSAGES)
    }

    private fun getChatsReference(myEmail: String, friendEmail: String): DocumentReference {
        val myEmailEncoded = UtilsCommon.getEmailEncoded(myEmail)
        val friendEmailEncoded = UtilsCommon.getEmailEncoded(friendEmail)

        var keyChat = "${myEmailEncoded}${FirebaseFirestoreAPI.SEPARATOR}${friendEmailEncoded}"
        if (myEmailEncoded > friendEmailEncoded){
            keyChat = "${friendEmailEncoded}${FirebaseFirestoreAPI.SEPARATOR}${myEmailEncoded}"
        }
        return FirebaseFirestoreAPI.getChatsReference(keyChat)
    }

    private fun getMessage(value: DocumentChange): Message {
        val message = value.document.toObject(Message::class.java)
        message.uid = value.document.id
        return message
    }

    fun unsubscribeToMessages(myEmail: String, friendEmail: String) {
        getChatMessagesReference(myEmail, friendEmail).addSnapshotListener(mMessagesEventListener).remove()
    }


    fun subscribeToFriend(uid: String, listener: LastConnectionEventListener) {
        mFriendProfileListener = EventListener { value, error ->
            if (error != null) {
                return@EventListener
            }

            if (value != null && value.exists()) {
                var lastConnectionFriend: Long = 0
                var uidConnectedFriend: String = ""

                try {
                    val date: Date? = value.getDate(UserConst.LAST_CONNECTION_WITH)
                    if (date != null) {
                        lastConnectionFriend = date.time
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    val lastConnectionWiht: String? = value.getString(UserConst.LAST_CONNECTION_WITH)
                    if (lastConnectionWiht != null && lastConnectionWiht.isNotEmpty()) {
                        val values: List<String> = lastConnectionWiht.split(FirebaseFirestoreAPI.SEPARATOR)
                        if (values.isNotEmpty()) {
                            lastConnectionFriend = values[0].toLong()
                            if (values.size > 1) {
                                uidConnectedFriend = values[1]
                            }
                        }
                    }
                }
                listener.onSuccess(lastConnectionFriend == UtilsCommon.ONLINE_VALUE, lastConnectionFriend, uidConnectedFriend)
            }
        }

        FirebaseFirestoreAPI.getUserReferenceByUid(uid).addSnapshotListener(mFriendProfileListener)
    }

    fun unsubscribeToFriend(uid: String) {
        FirebaseFirestoreAPI.getUserReferenceByUid(uid).addSnapshotListener(mFriendProfileListener).remove()
    }



    fun setMessageRead(myUid: String, friendUid: String) {
        getOneContactReference(myUid, friendUid).addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (value != null && value.exists()) {
                getOneContactReference(myUid, friendUid).update(UserConst.MESSAGES_UNREAD, 0)
            }
        }
    }


    fun sunUnreadMessages(myUid: String, friendUid: String) {
        FirebaseFirestoreAPI.getInstance().runTransaction { transaction ->
            val snapshot = transaction.get(getOneContactReference(friendUid, myUid))

            val user = snapshot.toObject(User::class.java)
            user!!.messageUnread = user.messageUnread + 1
            //transaction.update(sfDocRef, "population", newPopulation)


            null
        }
            .addOnSuccessListener {  }
            .addOnFailureListener {  }
    }

    private fun getOneContactReference(uidMain: String, uidChild: String) : DocumentReference {
        return FirebaseFirestoreAPI.getUserReferenceByUid(uidMain).collection(FirebaseFirestoreAPI.PATH_CONTACTS)
            .document(uidChild)
    }
}