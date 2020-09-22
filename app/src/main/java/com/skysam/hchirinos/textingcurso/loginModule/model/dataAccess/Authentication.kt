package com.skysam.hchirinos.textingcurso.loginModule.model.dataAccess

import com.google.firebase.auth.FirebaseAuth
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseAuthenticationAPI
import com.skysam.hchirinos.textingcurso.common.pojo.User

class Authentication {
    private var mAuthenticationAPI : FirebaseAuth = FirebaseAuthenticationAPI.getInstance()
    private var mAuthStateListener: FirebaseAuth.AuthStateListener? = null

    fun onResume() {
        mAuthenticationAPI.addAuthStateListener(mAuthStateListener!!)
    }

    fun onPause() {
        mAuthenticationAPI.removeAuthStateListener(mAuthStateListener!!)
    }

    fun getStatusAuth (callback: StatusAuthCallback) {
        mAuthStateListener = FirebaseAuth.AuthStateListener {firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                callback.onGetUser(user)
            } else {
                callback.onLaunchUILogin()
            }
        }
    }

    fun getCurrentUser(): User {
        return FirebaseAuthenticationAPI.getAuthUser()
    }
}