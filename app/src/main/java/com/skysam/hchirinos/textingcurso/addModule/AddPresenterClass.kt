package com.skysam.hchirinos.textingcurso.addModule

import com.skysam.hchirinos.textingcurso.addModule.events.AddEvent
import com.skysam.hchirinos.textingcurso.addModule.events.AddEventConst
import com.skysam.hchirinos.textingcurso.addModule.model.AddInteractor
import com.skysam.hchirinos.textingcurso.addModule.model.AddInteractorClass
import com.skysam.hchirinos.textingcurso.addModule.view.AddView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class AddPresenterClass(private var mView: AddView?) : AddPresenter{
    private val mInteractor: AddInteractor = AddInteractorClass()

    override fun onShow() {
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        mView = null
    }

    override fun addFriend(email: String) {
        if (mView != null) {
            mView!!.disableUIElements()
            mView!!.showProgress()

            mInteractor.addFriend(email)
        }
    }

    @Subscribe
    override fun onEventListener(event: AddEvent) {
        if (mView != null) {
            mView!!.hideProgress()
            mView!!.enableUIElements()

            when(event.typeEvent) {
                AddEventConst.SEND_REQUEST_SUCCESS -> mView!!.friendAdded()
                AddEventConst.ERROR_SERVER -> mView!!.friendNotAdded()
                AddEventConst.ERROR_EXIST -> mView!!.showMessageExist(event.resMsg!!)
            }
        }
    }
}