package com.skysam.hchirinos.textingcurso.addModule

import android.provider.ContactsContract
import com.skysam.hchirinos.textingcurso.addModule.events.AddEvent

interface AddPresenter {
    fun onShow()
    fun onDestroy()

    fun addFriend(email: String)
    fun onEventListener(event: AddEvent)
}