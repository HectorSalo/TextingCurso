package com.skysam.hchirinos.textingcurso.chatModule.model

import android.app.Activity
import android.app.TimePickerDialog
import android.net.Uri
import com.skysam.hchirinos.textingcurso.chatModule.events.ChatEvent
import com.skysam.hchirinos.textingcurso.chatModule.events.ChatEventConst

import com.skysam.hchirinos.textingcurso.chatModule.model.dataAccess.FirestoreDatabase
import com.skysam.hchirinos.textingcurso.chatModule.model.dataAccess.NotificationRS
import com.skysam.hchirinos.textingcurso.chatModule.model.dataAccess.Storage
import com.skysam.hchirinos.textingcurso.common.UtilsCommon
import com.skysam.hchirinos.textingcurso.common.model.EventErrorTypeListener
import com.skysam.hchirinos.textingcurso.common.model.StorageUploadImageCallback
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseAuthenticationAPI
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseFirestoreAPI
import com.skysam.hchirinos.textingcurso.common.pojo.Message
import com.skysam.hchirinos.textingcurso.common.pojo.User
import com.skysam.hchirinos.textingcurso.profileModule.events.ProfileEvent
import org.greenrobot.eventbus.EventBus

class ChatInteractorClass: ChatInteractor {
    private var mDatabase: FirestoreDatabase = FirestoreDatabase()
    private var mStorage: Storage = Storage()

    private var myUser = User()
    private lateinit var mFriendUid: String
    private lateinit var mFriendEmail: String
    private var mUidConnectedFriend: String = ""
    private var mLastConnectionFriend: Long = 0


    private fun getCurrentUser() : User {
        return FirebaseAuthenticationAPI.getAuthUser()
    }

    override fun subscribeToFriend(friendUid: String, friendEmail: String) {
        this.mFriendEmail = friendEmail
        this.mFriendUid =friendUid

        mDatabase.subscribeToFriend(friendUid, object : LastConnectionEventListener {
            override fun onSuccess(
                online: Boolean,
                lastConnection: Long,
                uidConnectedFriend: String
            ) {
                postStatusFriend(online, lastConnection)
                mUidConnectedFriend = uidConnectedFriend
                mLastConnectionFriend = lastConnection
            }
        })

        mDatabase.setMessageRead(getCurrentUser().uid!!, friendUid)
    }

    override fun unsubscribeToUFriend(friendUid: String) {
       mDatabase.unsubscribeToFriend(friendUid)
    }

    override fun subscribeToMessages() {
        mDatabase.subscribeToMessages(getCurrentUser().email!!, mFriendEmail, object : MessagesEventListener{
            override fun onMessageReceived(message: Message) {
                val msgSender = message.sender
                message.sentByMe = msgSender!! == getCurrentUser().email
                postMessage(message)
            }

            override fun onError(resMsg: Int) {
                post(ChatEventConst.ERROR_SERVER, resMsg)
            }
        })
        FirebaseFirestoreAPI.updateMyLastConnection(UtilsCommon.ONLINE, mFriendUid, getCurrentUser().uid!!)
    }

    override fun unsubscribeToMessages() {
        mDatabase.unsubscribeToMessages(getCurrentUser().email!!, mFriendEmail)
        FirebaseFirestoreAPI.updateMyLastConnection(UtilsCommon.OFFLINE, getCurrentUser().uid!!)
    }

    override fun sendMessage(msg: String) {
        sendMessage(msg, null)
    }

    override fun sendImage(activity: Activity, imageUri: Uri) {
        mStorage.uploadImageChat(activity, imageUri, getCurrentUser().email!!,
        object : StorageUploadImageCallback{
            override fun onSuccess(newUri: Uri) {
                sendMessage(null, newUri.toString())
                postUploadSucces()
            }

            override fun onError(resMsg: Int) {
                post(ChatEventConst.IMAGE_UPLOAD_FAIL, resMsg)
            }
        })
    }


    private fun sendMessage (msg: String?, photoUrl: String?) {
        mDatabase.sendMessage(msg, photoUrl, mFriendEmail, getCurrentUser(),
            object : SendMessageListener {
                override fun onSuccess() {
                    if (mUidConnectedFriend != getCurrentUser().uid) {
                        mDatabase.sunUnreadMessages(getCurrentUser().uid!!, mFriendUid)

                        if (mLastConnectionFriend != UtilsCommon.ONLINE_VALUE) {
                            NotificationRS.sendNotification(getCurrentUser().username!!, msg!!, mFriendEmail,
                            getCurrentUser().uid!!, getCurrentUser().email!!, getCurrentUser().uri!!, object : EventErrorTypeListener {
                                    override fun onError(typeEvent: Int, reaMsg: Int) {
                                        post(typeEvent, reaMsg)
                                    }
                                })
                        }
                    }
                }
            })
    }


    private fun postUploadSucces() {
        post(ChatEventConst.IMAGE_UPLOAD_SUCCESS, 0, null, false, 0)
    }


    private fun postMessage(message: Message?) {
        post(ChatEventConst.MESSAGE_ADDED, 0, message, false, 0)
    }

    private fun post(typeEvent: Int, resMsg: Int){
        post(typeEvent, resMsg, null, false, 0)
    }

    private fun postStatusFriend(online: Boolean, lastConnection: Long) {
        post(ChatEventConst.GET_STATUS_FRIEND, 0,null, online, lastConnection)
    }

    private fun post(typeEvent: Int, resMsg: Int, message: Message?, online: Boolean, lastConnection: Long) {
        val event = ChatEvent()
        event.typeEvent = typeEvent
        event.resMsg = resMsg
        event.message = message
        event.connected = online
        event.lastConnection = lastConnection
        EventBus.getDefault().post(event)
    }
}