package com.skysam.hchirinos.textingcurso.addModule.model

import com.skysam.hchirinos.textingcurso.addModule.AddPresenter
import com.skysam.hchirinos.textingcurso.addModule.events.AddEvent
import com.skysam.hchirinos.textingcurso.addModule.events.AddEventConst
import com.skysam.hchirinos.textingcurso.addModule.model.dataAccess.FirestoreDatabase
import com.skysam.hchirinos.textingcurso.common.model.BasicEventsCallback
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseAuthenticationAPI
import org.greenrobot.eventbus.EventBus

class AddInteractorClass : AddInteractor {
    private var mDatabase: FirestoreDatabase = FirestoreDatabase()

    override fun addFriend(email: String) {
        mDatabase.addFriend(email, FirebaseAuthenticationAPI.getAuthUser(), object : BasicEventsCallback{
            override fun onSuccess() {
                post(AddEventConst.SEND_REQUEST_SUCCESS)
            }

            override fun onError() {
                post(AddEventConst.ERROR_SERVER)
            }
        })
    }

    private fun post(typeEvent: Int) {
        val event = AddEvent()
        event.typeEvent = typeEvent
        EventBus.getDefault().post(event)
    }

}