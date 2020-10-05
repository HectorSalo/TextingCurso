package com.skysam.hchirinos.textingcurso.loginModule.model

import com.google.firebase.auth.FirebaseUser
import com.skysam.hchirinos.textingcurso.common.model.EventErrorTypeListener
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseCloudMessagingAPI
import com.skysam.hchirinos.textingcurso.loginModule.events.LoginEvent
import com.skysam.hchirinos.textingcurso.loginModule.events.LoginEventConst
import com.skysam.hchirinos.textingcurso.loginModule.model.dataAccess.Authentication
import com.skysam.hchirinos.textingcurso.loginModule.model.dataAccess.FirestoreDatabase
import com.skysam.hchirinos.textingcurso.loginModule.model.dataAccess.StatusAuthCallback
import org.greenrobot.eventbus.EventBus

class LoginInteractorClass : LoginInteractor {
    private var mAuthentication: Authentication = Authentication()
    private var mDatabase: FirestoreDatabase = FirestoreDatabase()

    override fun onResume() {
        mAuthentication.onResume()
    }

    override fun onPause() {
        mAuthentication.onPause()
    }

    override fun getStatusAuth() {
        mAuthentication.getStatusAuth(object : StatusAuthCallback {
            override fun onGetUser(user: FirebaseUser) {
                post(LoginEventConst.STATUS_AUTH_SUCCESS, user)

                mDatabase.checkUserExist(mAuthentication.getCurrentUser().uid!!, object : EventErrorTypeListener {
                    override fun onError(typeEvent: Int, reaMsg: Int) {
                        if (typeEvent == LoginEventConst.USER_NOT_EXIST) {
                            registerUser()
                        } else {
                            post(typeEvent)
                        }
                    }
                })
                FirebaseCloudMessagingAPI.subscribeToMyTopic(user.email!!)
            }

            override fun onLaunchUILogin() {
                post(LoginEventConst.STATUS_AUTH_ERROR)
            }

        })
    }

    private fun registerUser() {
        val currentUser = mAuthentication.getCurrentUser()
        mDatabase.registerUser(currentUser)
    }

    private fun post(typeEvent: Int) {
        post(typeEvent, null)
    }

    private fun post(typeEvent: Int, user: FirebaseUser?) {
        val event = LoginEvent()
        event.typeEvent = typeEvent
        event.user = user
        EventBus.getDefault().post(event)
    }
}