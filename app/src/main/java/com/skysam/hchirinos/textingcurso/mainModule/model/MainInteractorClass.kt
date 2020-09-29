package com.skysam.hchirinos.textingcurso.mainModule.model

import com.google.firebase.auth.FirebaseUser
import com.skysam.hchirinos.textingcurso.common.UtilsCommon
import com.skysam.hchirinos.textingcurso.common.model.BasicEventsCallback
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseAuthenticationAPI
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseFirestoreAPI
import com.skysam.hchirinos.textingcurso.common.pojo.User
import com.skysam.hchirinos.textingcurso.loginModule.events.LoginEvent
import com.skysam.hchirinos.textingcurso.mainModule.events.MainEvent
import com.skysam.hchirinos.textingcurso.mainModule.events.MainEventConst
import com.skysam.hchirinos.textingcurso.mainModule.model.dataAccess.Authentication
import com.skysam.hchirinos.textingcurso.mainModule.model.dataAccess.FirestoreDatabase
import com.skysam.hchirinos.textingcurso.mainModule.model.dataAccess.UserEventListener
import org.greenrobot.eventbus.EventBus

class MainInteractorClass : MainInteractor {

    private var mAuthentication: Authentication = Authentication()
    private var mDatabase: FirestoreDatabase = FirestoreDatabase()
    private var mMyUser: User? = null

    override fun subscribeToUserList() {
        mDatabase.subscribeToUserList(getCurrentUser().uid!!, object : UserEventListener {
            override fun onUserAdded(user: User) {
                post(MainEventConst.USER_ADDED, user)
            }

            override fun onUserUpdated(user: User) {
                post(MainEventConst.USER_UPDATED, user)
            }

            override fun onUserRemoved(user: User) {
                post(MainEventConst.USER_REMOVED, user)
            }

            override fun onError(resMsg: Int) {
                postError(resMsg)
            }

        })

        mDatabase.subscribeToRequests(getCurrentUser().email!!, object : UserEventListener{
            override fun onUserAdded(user: User) {
                post(MainEventConst.REQUEST_ADDED, user)
            }

            override fun onUserUpdated(user: User) {
                post(MainEventConst.REQUEST_UPDATED, user)
            }

            override fun onUserRemoved(user: User) {
                post(MainEventConst.REQUEST_REMOVED, user)
            }

            override fun onError(resMsg: Int) {
                post(MainEventConst.ERROR_SERVER)
            }

        })

        changeConnectionStatus(UtilsCommon.ONLINE)
    }

    override fun unsubscribeToUserList() {
        mDatabase.unsubscribeToUsers(getCurrentUser().uid!!)
        mDatabase.unsubscribeToRequests(getCurrentUser().email!!)

        changeConnectionStatus(UtilsCommon.OFFLINE)
    }

    override fun signOff() {
        mAuthentication.signOff()
    }

    override fun getCurrentUser(): User {
        return FirebaseAuthenticationAPI.getAuthUser()
    }

    override fun removedFriend(friendUid: String) {
        mDatabase.removeUser(friendUid, getCurrentUser().uid!!, object : BasicEventsCallback{
            override fun onSuccess() {
                post(MainEventConst.USER_REMOVED)
            }

            override fun onError() {
                post(MainEventConst.ERROR_SERVER)
            }

        })
    }

    override fun acceptRequest(user: User) {
        mDatabase.acceptRequest(user, getCurrentUser(), object : BasicEventsCallback{
            override fun onSuccess() {
                post(MainEventConst.REQUEST_ACCEPTED, user)
            }

            override fun onError() {
                post(MainEventConst.ERROR_SERVER)
            }

        })
    }

    override fun denyRequest(user: User) {
        mDatabase.denyRequest(user, getCurrentUser().email!!, object : BasicEventsCallback{
            override fun onSuccess() {
                post(MainEventConst.REQUEST_DENIED)
            }

            override fun onError() {
                post(MainEventConst.ERROR_SERVER)
            }
        })
    }



    private fun changeConnectionStatus(online: Boolean) {
        FirebaseFirestoreAPI.updateMyLastConnection(online, getCurrentUser().uid!!)
    }

    private fun postError(resMsg: Int) {
        post(MainEventConst.ERROR_SERVER, null, resMsg)
    }

    private fun post(typeEvent: Int) {
        post(typeEvent, null, 0)
    }

    private fun post(typeEvent: Int, user: User) {
        post(typeEvent, user, 0)
    }

    private fun post(typeEvent: Int, user: User?, resMsg: Int) {
        val event = MainEvent()
        event.typeEvent = typeEvent
        event.user = user
        event.resMsg = resMsg
        EventBus.getDefault().post(event)
    }
}