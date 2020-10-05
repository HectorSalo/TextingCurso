package com.skysam.hchirinos.textingcurso.chatModule.model.dataAccess

import android.net.Uri
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.JsonObject
import com.skysam.hchirinos.textingcurso.common.UtilsCommon
import com.skysam.hchirinos.textingcurso.common.model.EventErrorTypeListener
import com.skysam.hchirinos.textingcurso.common.pojo.UserConst
import org.json.JSONObject

object NotificationRS {
    const val TEXTING_RS = "https://invskysam.000webhostapp.com/texting/dataAccess/TextingRS.php"
    const val SEND_NOTIFICATION = "sendNotification"

    fun sendNotification (title: String, message: String, email: String, uid: String,
                          myEmail: String, photoUrl: Uri, listener: EventErrorTypeListener) {
        val params = JSONObject()
        params.put(UtilsCommon.METHOD, SEND_NOTIFICATION)
        params.put(UtilsCommon.TITLE, title)
        params.put(UtilsCommon.MESSAGE, message)
        params.put(UtilsCommon.TOPIC, UtilsCommon.getEmailToTopic(email))
        params.put(UserConst.UID, uid)
        params.put(UserConst.EMAIL, myEmail)
        params.put(UserConst.PHOTO_URL, photoUrl)
        params.put(UserConst.USERNAME, title)


        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, TEXTING_RS, params,
            {

            }, {

            }).headers
    }
}