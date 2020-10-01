package com.skysam.hchirinos.textingcurso.chatModule.view

import android.content.Intent
import com.skysam.hchirinos.textingcurso.common.pojo.Message

interface ChatView {
    fun showProgress()
    fun hideProgress()

    fun onStatusUser(connected: Boolean, lastConnection: Long)

    fun onError(resMsg: Int)

    fun onMessageReceived(msg: Message)
    fun openDialogPreview(data: Intent)
}