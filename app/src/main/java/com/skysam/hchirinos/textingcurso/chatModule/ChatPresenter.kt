package com.skysam.hchirinos.textingcurso.chatModule

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.skysam.hchirinos.textingcurso.chatModule.events.ChatEvent

interface ChatPresenter {
    fun onCreate()
    fun onDestroy()
    fun onPause()
    fun onResume()

    fun setupFriend(uid: String, email: String)

    fun sendMessage(msg: String)

    fun sendImage(activity: Activity, imageUri: Uri)

    fun result(requestCode: Int, resultCode: Int, data: Intent?)

    fun onEventListener(event: ChatEvent)
}