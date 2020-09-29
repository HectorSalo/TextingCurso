package com.skysam.hchirinos.textingcurso.mainModule.events

import com.google.firebase.auth.FirebaseUser
import com.skysam.hchirinos.textingcurso.common.pojo.User

class MainEvent {
    var user: User? = null
    var typeEvent: Int? = null
    var resMsg: Int? = null
}