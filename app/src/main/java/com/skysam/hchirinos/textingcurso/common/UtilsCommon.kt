package com.skysam.hchirinos.textingcurso.common

object UtilsCommon {

    const val ONLINE = true
    const val OFFLINE = false
    const val ONLINE_VALUE: Long = -1

    fun getEmailEncoded(email: String) : String {
        val preKey = email.replace("_", "__")
        return preKey.replace(".", "_")
    }
}