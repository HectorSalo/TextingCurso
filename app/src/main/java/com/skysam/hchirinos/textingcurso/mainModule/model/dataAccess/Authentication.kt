package com.skysam.hchirinos.textingcurso.mainModule.model.dataAccess

import com.google.firebase.auth.FirebaseAuth
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseAuthenticationAPI

class Authentication {
    private var mAuthenticationAPI: FirebaseAuth = FirebaseAuthenticationAPI.getInstance()

    fun getmAuthenticacionAPI() : FirebaseAuth {
        return mAuthenticationAPI
    }

    fun signOff() {
        mAuthenticationAPI.signOut()
    }
}