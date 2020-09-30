package com.skysam.hchirinos.textingcurso.profileModule.model.dataAccess

interface UpdateUserListener {
    fun onSuccess()
    fun onNotifyContacts()
    fun onError(resMsg: Int)
}