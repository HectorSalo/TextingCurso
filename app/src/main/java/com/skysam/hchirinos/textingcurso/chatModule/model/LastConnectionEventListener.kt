package com.skysam.hchirinos.textingcurso.chatModule.model

interface LastConnectionEventListener {
    fun onSuccess(online: Boolean, lastConnection: Long, uidConnectedFriend: String)
}