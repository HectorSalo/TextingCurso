package com.skysam.hchirinos.textingcurso.loginModule.events

import com.google.firebase.auth.FirebaseUser

class LoginEvent {
    var user: FirebaseUser? = null
    var typeEvent: Int? = null
    var resMsg: Int? = null
}
