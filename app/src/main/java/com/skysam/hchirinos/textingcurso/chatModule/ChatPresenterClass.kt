package com.skysam.hchirinos.textingcurso.chatModule

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.skysam.hchirinos.textingcurso.chatModule.events.ChatEvent
import com.skysam.hchirinos.textingcurso.chatModule.events.ChatEventConst
import com.skysam.hchirinos.textingcurso.chatModule.model.ChatInteractoClass
import com.skysam.hchirinos.textingcurso.chatModule.model.ChatInteractor
import com.skysam.hchirinos.textingcurso.chatModule.view.ChatView
import com.skysam.hchirinos.textingcurso.common.UtilsCommon
import com.skysam.hchirinos.textingcurso.mainModule.events.MainEventConst
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ChatPresenterClass(private var mView: ChatView?): ChatPresenter {

    private val mInteractor: ChatInteractor = ChatInteractoClass()
    private lateinit var mFriendUid: String
    private lateinit var mFriendEmail: String

    override fun onCreate() {
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        mView = null
    }

    override fun onPause() {
        if (mView != null) {
            mInteractor.unsubscribeToUFriend(mFriendUid)
            mInteractor.unsubscribeToMessages()
        }
    }

    override fun onResume() {
        if (mView != null) {
            mInteractor.subscribeToFriend(mFriendUid, mFriendEmail)
            mInteractor.subscribeToMessages()
        }
    }

    override fun setupFriend(uid: String, email: String) {
        mFriendEmail = email
        mFriendUid = uid
    }

    override fun sendMessage(msg: String) {
        if (mView != null) {
            mInteractor.sendMessage(msg)
        }
    }

    override fun sendImage(activity: Activity, imageUri: Uri) {
        if (mView != null) {
            mView!!.showProgress()
            mInteractor.sendImage(activity, imageUri)
        }
    }

    override fun result(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == UtilsCommon.RC_PHOTO_PICKER && resultCode == Activity.RESULT_OK) {
            mView!!.openDialogPreview(data)
        }
    }


    @Subscribe
    override fun onEventListener(event: ChatEvent) {
        if (mView != null) {

            when (event.typeEvent) {
                ChatEventConst.MESSAGE_ADDED -> mView!!.onMessageReceived(event.message!!)
                ChatEventConst.IMAGE_UPLOAD_SUCCESS -> mView!!.hideProgress()
                ChatEventConst.GET_STATUS_FRIEND -> mView!!.onStatusUser(event.connected!!, event.lastConnection!!)
                else -> {
                    mView!!.hideProgress()
                    mView!!.onError(event.resMsg!!)
                }
            }
        }
    }
}