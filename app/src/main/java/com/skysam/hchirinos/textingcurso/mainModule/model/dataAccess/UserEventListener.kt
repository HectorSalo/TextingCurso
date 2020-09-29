package com.skysam.hchirinos.textingcurso.mainModule.model.dataAccess

import com.skysam.hchirinos.textingcurso.common.pojo.User


interface UserEventListener {
    fun onUserAdded(user: User)
    fun onUserUpdated(user: User)
    fun onUserRemoved(user: User)

    fun onError(resMsg: Int)
}