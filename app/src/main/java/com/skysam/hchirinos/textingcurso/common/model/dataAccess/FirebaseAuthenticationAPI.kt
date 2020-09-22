package com.skysam.hchirinos.textingcurso.common.model.dataAccess

import com.google.firebase.auth.FirebaseAuth
import com.skysam.hchirinos.textingcurso.common.pojo.User

object FirebaseAuthenticationAPI {
    private var mFirebaseAuth: FirebaseAuth? = null


    fun getInstance(): FirebaseAuth {
        if (mFirebaseAuth == null) {
            mFirebaseAuth = FirebaseAuth.getInstance()
        }
        return mFirebaseAuth!!
    }

    fun getAuthUser(): User {
        var user = User()
        if (mFirebaseAuth != null && mFirebaseAuth!!.currentUser != null) {
            user.uid = mFirebaseAuth!!.currentUser!!.uid
            user.username = mFirebaseAuth!!.currentUser!!.displayName
            user.email = mFirebaseAuth!!.currentUser!!.email
            user.uri = mFirebaseAuth!!.currentUser!!.photoUrl
        }
        return user
    }
}

