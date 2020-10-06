package com.skysam.hchirinos.textingcurso.addModule.model.dataAccess

import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.addModule.events.AddEventConst
import com.skysam.hchirinos.textingcurso.common.UtilsCommon
import com.skysam.hchirinos.textingcurso.common.model.BasicEventsCallback
import com.skysam.hchirinos.textingcurso.common.model.EventsCallback
import com.skysam.hchirinos.textingcurso.common.model.dataAccess.FirebaseFirestoreAPI
import com.skysam.hchirinos.textingcurso.common.pojo.User
import com.skysam.hchirinos.textingcurso.common.pojo.UserConst
import java.util.HashMap

class FirestoreDatabase {

    fun checkUserExist(email: String, callback: EventsCallback) {
        FirebaseFirestoreAPI.getUsersReference().whereEqualTo(UserConst.EMAIL, email).limit(1)
            .get().addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    callback.onSuccess()
                } else {
                    callback.onError(AddEventConst.ERROR_EXIST, R.string.addFriend_error_user_exist)
                }
            }
            .addOnFailureListener { callback.onError(AddEventConst.ERROR_SERVER, R.string.addFriend_error_message) }
    }

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