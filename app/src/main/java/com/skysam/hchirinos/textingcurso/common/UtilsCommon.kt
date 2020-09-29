package com.skysam.hchirinos.textingcurso.common

object UtilsCommon {

    fun getEmailEncoded(email: String) : String {
        val preKey = email.replace("_", "__")
        return preKey.replace(".", "_")
    }
}