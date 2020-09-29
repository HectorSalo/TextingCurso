package com.skysam.hchirinos.textingcurso.mainModule

import com.skysam.hchirinos.textingcurso.common.pojo.User
import com.skysam.hchirinos.textingcurso.mainModule.events.MainEvent
import com.skysam.hchirinos.textingcurso.mainModule.events.MainEventConst
import com.skysam.hchirinos.textingcurso.mainModule.model.MainInteractor
import com.skysam.hchirinos.textingcurso.mainModule.model.MainInteractorClass
import com.skysam.hchirinos.textingcurso.mainModule.view.MainView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainPresenterClass (private var mView: MainView?) : MainPresenter {
    private val mInteractor: MainInteractor = MainInteractorClass()


    override fun onCreate() {
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        mView = null
    }

    override fun onPause() {
        if (mView != null) {
            mInteractor.unsubscribeToUserList()
        }
    }

    override fun onResume() {
        if (mView != null) {
            mInteractor.subscribeToUserList()
        }
    }

    override fun signOff() {
        mInteractor.unsubscribeToUserList()
        mInteractor.signOff()
        onDestroy()
    }

    override fun getCurentUser(): User {
        return mInteractor.getCurrentUser()
    }

    override fun removedFriend(friendUser: String) {
        if (mView != null) {
            mInteractor.removedFriend(friendUser)
        }
    }

    override fun acceptRequest(user: User) {
        if (mView != null) {
            mInteractor.acceptRequest(user)
        }
    }

    override fun denyRequest(user: User) {
        mInteractor.denyRequest(user)
    }

    @Subscribe
    override fun onEventListener(event: MainEvent) {
        if (mView != null) {
            val user = event.user

            when (event.typeEvent) {
                MainEventConst.USER_ADDED -> mView!!.friendAdded(user!!)
                MainEventConst.USER_UPDATED -> mView!!.friendUpdated(user!!)
                MainEventConst.USER_REMOVED -> if (user != null) mView!!.friendRemoved(user) else mView!!.showFriendRemoved()
                MainEventConst.REQUEST_ADDED -> mView!!.requestAdded(user!!)
                MainEventConst.REQUEST_UPDATED -> mView!!.requestUpdated(user!!)
                MainEventConst.REQUEST_REMOVED -> mView!!.requestRemoved(user!!)
                MainEventConst.REQUEST_ACCEPTED -> mView!!.showRequestAccepted(user!!.username!!)
                MainEventConst.REQUEST_DENIED -> mView!!.showRequestDenied()
                MainEventConst.ERROR_SERVER -> mView!!.showError(event.resMsg!!)
            }
        }
    }
}