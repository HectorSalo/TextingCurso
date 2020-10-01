package com.skysam.hchirinos.textingcurso.chatModule.events

import com.skysam.hchirinos.textingcurso.common.pojo.Message
import com.skysam.hchirinos.textingcurso.common.pojo.User

class ChatEvent {
    var typeEvent: Int? = null
    var resMsg: Int? = null
    var message : Message? = null
    var connected: Boolean? = null
    var lastConnection: Boolean? = null
}