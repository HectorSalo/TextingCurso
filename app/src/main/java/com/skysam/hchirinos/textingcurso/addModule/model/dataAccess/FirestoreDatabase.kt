package com.skysam.hchirinos.textingcurso.addModule.model.dataAccess

import com.skysam.hchirinos.textingcurso.common.UtilsCommon
import com.skysam.hchirinos.textingcurso.common.model.BasicEventsCallback
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseFirestoreAPI
import com.skysam.hchirinos.textingcurso.common.pojo.User
import com.skysam.hchirinos.textingcurso.common.pojo.UserConst
import java.util.HashMap

class FirestoreDatabase {

    fun addFriend(email: String, myUser: User, callback: BasicEventsCallback) {
        val myUserMap = HashMap<String, Any?>()
        myUserMap[UserConst.USERNAME] = myUser.username
        myUserMap[UserConst.EMAIL] = myUser.email
        myUserMap[UserConst.PHOTO_URL] = myUser.getPhotoValid()

        val emailEncoded = UtilsCommon.getEmailEncoded(email)

        FirebaseFirestoreAPI.getRequestReference(emailEncoded).document(myUser.uid!!)
            .set(myUserMap)
            .addOnSuccessListener {
                callback.onSuccess()
            }
            .addOnFailureListener {
                callback.onError()
            }
    }

}