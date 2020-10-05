package com.skysam.hchirinos.textingcurso.common.model.dataAccess

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.skysam.hchirinos.textingcurso.common.UtilsCommon

object FirebaseCloudMessagingAPI {
    private var mFirebaseMessaging: FirebaseMessaging? = null

    fun getInstance() : FirebaseMessaging {
        if (mFirebaseMessaging == null) {
            mFirebaseMessaging = FirebaseMessaging.getInstance()
        }
        return mFirebaseMessaging!!
    }

    fun subscribeToMyTopic(myEmail: String) {
        getInstance().subscribeToTopic(UtilsCommon.getEmailToTopic(myEmail)).addOnSuccessListener {  }
    }

    fun unsubscribeToMyTopic(myEmail: String) {
        getInstance().unsubscribeFromTopic(UtilsCommon.getEmailToTopic(myEmail)).addOnSuccessListener {  }
    }
}