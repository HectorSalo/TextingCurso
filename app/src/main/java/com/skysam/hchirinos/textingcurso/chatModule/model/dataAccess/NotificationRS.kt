package com.skysam.hchirinos.textingcurso.chatModule.model.dataAccess

import android.net.Uri
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.JsonObject
import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.TextingApplication
import com.skysam.hchirinos.textingcurso.chatModule.events.ChatEventConst
import com.skysam.hchirinos.textingcurso.common.UtilsCommon
import com.skysam.hchirinos.textingcurso.common.model.EventErrorTypeListener
import com.skysam.hchirinos.textingcurso.common.pojo.UserConst
import org.json.JSONException
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


        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, TEXTING_RS, params,
            {response ->
                try {
                    when (response.getInt(UtilsCommon.SUCCESS)) {
                        ChatEventConst.SEND_NOTIFICATION_SUCCESS -> {}
                        ChatEventConst.ERROR_METHOD_NOT_EXIST -> listener.onError(ChatEventConst.ERROR_METHOD_NOT_EXIST, R.string.method_not_exist)
                        else -> listener.onError(ChatEventConst.ERROR_SERVER, R.string.common_error_server)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onError(ChatEventConst.ERROR_PROCESS_DATA, R.string.common_error_server)
                }
            }, {error ->
                Log.e("Volley error", error.localizedMessage!!)
                listener.onError(ChatEventConst.ERROR_VOLLEY, R.string.common_error_server)
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json; charset=utf-8"
                return headers
            }
        }

        TextingApplication.TextingApplicationObject.addToReqQueue(jsonObjectRequest)
    }
}