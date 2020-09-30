package com.skysam.hchirinos.textingcurso.mainModule

import com.skysam.hchirinos.textingcurso.common.pojo.User
import com.skysam.hchirinos.textingcurso.mainModule.events.MainEvent

interface MainPresenter {
    fun onCreate()
    fun onDestroy()
    fun onPause()
    fun onResume()

    fun signOff()
    fun getCurrentUser() : User
    fun removedFriend(friendUser: String)

    fun acceptRequest(user: User)
    fun denyRequest(user: User)

    fun onEventListener(event: MainEvent)
}