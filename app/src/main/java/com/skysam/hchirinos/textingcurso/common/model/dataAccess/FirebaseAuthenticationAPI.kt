package com.skysam.hchirinos.textingcurso.common.model.dataAccess

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthenticationAPI {
    private var mFirebaseAuth: FirebaseAuth
    private var INSTANCE: FirebaseAuthenticationAPI? = null

    private constructor() {
        this.mFirebaseAuth = FirebaseAuth.getInstance()
    }

    fun getInstance(): FirebaseAuthenticationAPI {
        if (INSTANCE == null) {
            INSTANCE = FirebaseAuthenticationAPI()
        }
        return INSTANCE as FirebaseAuthenticationAPI
    }


}
