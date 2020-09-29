package com.skysam.hchirinos.textingcurso.mainModule.model

import com.skysam.hchirinos.textingcurso.common.pojo.User

interface MainInteractor {
    fun subscribeToUserList()
    fun unsubscribeToUserList()

    fun signOff()

    fun getCurrentUser() : User
    fun removedFriend(friendUid: String)

    fun acceptRequest(user: User)
    fun denyRequest(user: User)
}