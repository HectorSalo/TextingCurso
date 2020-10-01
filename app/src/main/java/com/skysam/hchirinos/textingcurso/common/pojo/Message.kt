package com.skysam.hchirinos.textingcurso.common.pojo

import android.net.Uri

class Message {
    var msg: String? = null
    var sender: String? = null
    var photoUrl: String? = null

    var sentByMe: Boolean? = null
    var uid: String? = null
    var loaded: Boolean? = null

    fun getPhotoValid(): String? {
        return if (photoUrl != null) photoUrl else ""
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Message

        if (uid != other.uid) return false

        return true
    }

    override fun hashCode(): Int {
        return uid?.hashCode() ?: 0
    }


}