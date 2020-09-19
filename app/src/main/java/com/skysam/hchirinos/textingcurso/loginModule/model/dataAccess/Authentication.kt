package com.skysam.hchirinos.textingcurso.loginModule.model.dataAccess

import com.google.firebase.auth.FirebaseAuth
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseAuthenticationAPI

class Authentication {
    private var mAuthenticationAPI: FirebaseAuthenticationAPI? = null
    private var mAuthStateListener: FirebaseAuth.AuthStateListener? = null
}