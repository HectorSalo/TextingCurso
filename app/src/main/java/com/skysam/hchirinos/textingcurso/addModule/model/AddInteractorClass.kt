package com.skysam.hchirinos.textingcurso.addModule.model

import com.skysam.hchirinos.textingcurso.addModule.AddPresenter
import com.skysam.hchirinos.textingcurso.addModule.events.AddEvent
import com.skysam.hchirinos.textingcurso.addModule.events.AddEventConst
import com.skysam.hchirinos.textingcurso.addModule.model.dataAccess.FirestoreDatabase
import com.skysam.hchirinos.textingcurso.common.model.BasicEventsCallback
import com.skysam.hchirinos.textingcurso.common.model.EventsCallback
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseAuthenticationAPI
import org.greenrobot.eventbus.EventBus

class AddInteractorClass : AddInteractor {
    private var mDatabase: FirestoreDatabase = FirestoreDatabase()

    override fun addFriend(email: String) {
        mDatabase.checkUserExist(email, object : EventsCallback {
            override fun onSuccess() {
                mDatabase.addFriend(email, FirebaseAuthenticationAPI.getAuthUser(), object : BasicEventsCallback{
                    override fun onSuccess() {
                        post(AddEventConst.SEND_REQUEST_SUCCESS)
                    }

                    override fun onError() {
                        post(AddEventConst.ERROR_SERVER)
                    }
                })
            }

            override fun onError(typeEvent: Int, reaMsg: Int) {
                post(typeEvent, reaMsg)
            }
        })
    }

    private fun post(typeEvent: Int, resMsg: Int) {
        val event = AddEvent()
        event.typeEvent = typeEvent
        event.resMsg = resMsg
        EventBus.getDefault().post(event)
    }

    private fun post(typeEvent: Int) {
       post(typeEvent, 0)
    }

}