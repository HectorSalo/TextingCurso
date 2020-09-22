package com.skysam.hchirinos.textingcurso.loginModule.model.dataAccess

import com.skysam.hchirinos.textingcurso.common.model.EventErrorTypeListener
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseFirestoreAPI
import com.skysam.hchirinos.textingcurso.common.pojo.User
import com.skysam.hchirinos.textingcurso.common.pojo.UserConst

class FirestoreDatabase {

    fun registerUser(user: User) {
        val values = HashMap<String, Any?>()
        values[UserConst.USERNAME] = user.username
        values[UserConst.EMAIL] = user.email
        values[UserConst.PHOTO_URL] = user.photoUrl

        FirebaseFirestoreAPI.getUserReferenceByUid(user.uid!!).update(values)
    }

    fun checkUserExist(uid: String, listener: EventErrorTypeListener) {

    }
}