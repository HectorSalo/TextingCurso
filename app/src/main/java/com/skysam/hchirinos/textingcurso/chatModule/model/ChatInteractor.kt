package com.skysam.hchirinos.textingcurso.chatModule.model

import android.app.Activity
import android.net.Uri

interface ChatInteractor {
    fun subscribeToFriend(friendUid: String, friendEmail: String)
    fun unsubscribeToUFriend(friendUid: String)

    fun subscribeToMessages()
    fun unsubscribeToMessages()

    fun sendMessage(msg: String)
    fun sendImage(activity: Activity, imageUri: Uri)
}