package com.skysam.hchirinos.textingcurso.mainModule.view

import com.skysam.hchirinos.textingcurso.common.pojo.User

interface MainView {
    fun friendAdded(user: User)
    fun friendUpdated(user: User)
    fun friendRemoved(user: User)

    fun requestAdded(user: User)
    fun requestUpdated(user: User)
    fun requestRemoved(user: User)

    fun showRequestAccepted(username: String)
    fun showRequestDenied()

    fun showFriendRemoved()

    fun showError(resMsg: Int)
}