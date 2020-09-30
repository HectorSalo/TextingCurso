package com.skysam.hchirinos.textingcurso.common.pojo

import android.net.Uri

class User {
    var lastConnectionWith: String? = null
    var username: String? = null
    var email: String? = null
    var photoUrl: String? = null
    var messageUnread: Int = -1

    var uid: String? = null

    var uri: Uri? = null

    fun getUsernameValid(): String? {
        return if (username == null) email else if (username!!.isEmpty()) email else username
    }

    fun getPhotoValid(): String? {
        return if (photoUrl != null) photoUrl else if (uri != null) uri.toString() else ""
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (uid != other.uid) return false

        return true
    }

    override fun hashCode(): Int {
        return uid?.hashCode() ?: 0
    }


}