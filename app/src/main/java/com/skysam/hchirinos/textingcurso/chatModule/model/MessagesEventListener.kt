package com.skysam.hchirinos.textingcurso.chatModule.model

import com.skysam.hchirinos.textingcurso.common.pojo.Message

interface MessagesEventListener {
    fun onMessageReceived(message: Message)
    fun onError(resMsg: Int)
}