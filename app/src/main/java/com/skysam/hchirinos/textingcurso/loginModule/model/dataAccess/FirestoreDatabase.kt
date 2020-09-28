package com.skysam.hchirinos.textingcurso.loginModule.model.dataAccess

import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.common.model.EventErrorTypeListener
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseFirestoreAPI
import com.skysam.hchirinos.textingcurso.common.pojo.User
import com.skysam.hchirinos.textingcurso.common.pojo.UserConst
import com.skysam.hchirinos.textingcurso.loginModule.events.LoginEvent
import com.skysam.hchirinos.textingcurso.loginModule.events.LoginEventConst

class FirestoreDatabase {

    fun registerUser(user: User) {
        val values = HashMap<String, Any?>()
        values[UserConst.USERNAME] = user.username
        values[UserConst.EMAIL] = user.email
        values[UserConst.PHOTO_URL] = user.photoUrl

        FirebaseFirestoreAPI.getUserReferenceByUid(user.uid!!).update(values)
    }

    fun checkUserExist(uid: String, listener: EventErrorTypeListener) {
        FirebaseFirestoreAPI.getUserReferenceByUid(uid).get().addOnSuccessListener { document ->
            if (document == null) {
                listener.onError(LoginEventConst.USER_NOT_EXIST, R.string.login_error_user_exist)
            }
        }
            .addOnCanceledListener {
                listener.onError(LoginEventConst.ERROR_SERVER, R.string.login_message_error)
            }
    }
}