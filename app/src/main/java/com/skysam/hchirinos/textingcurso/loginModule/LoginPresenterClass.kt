package com.skysam.hchirinos.textingcurso.loginModule

import android.app.Activity
import android.content.Intent
import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.loginModule.events.LoginEvent
import com.skysam.hchirinos.textingcurso.loginModule.events.LoginEventConst
import com.skysam.hchirinos.textingcurso.loginModule.model.LoginInteractor
import com.skysam.hchirinos.textingcurso.loginModule.model.LoginInteractorClass
import com.skysam.hchirinos.textingcurso.loginModule.view.LoginActivity
import com.skysam.hchirinos.textingcurso.loginModule.view.LoginView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class LoginPresenterClass(private var mView: LoginView?) : LoginPresenter {
    private val mInteractor: LoginInteractor = LoginInteractorClass()

    override fun onCreate() {
        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        if (setProgress()) {
            mInteractor.onResume()
        }
    }

    private fun setProgress(): Boolean {
        if (mView != null) {
            mView!!.showProgress()
            return true
        }
        return false
    }

    override fun onPause() {
        if (setProgress()) {
           mInteractor.onPause()
        }
    }

    override fun onDestroy() {
        mView = null
        EventBus.getDefault().unregister(this)
    }

    override fun result(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode){
                LoginActivity.Const.RC_SIGN_IN -> {
                    mView!!.showLoginSuccessfully(data)
                }
            }
        } else {
            mView!!.showError(R.string.login_message_error)
        }
    }

    override fun getStatusAuth() {
        if (setProgress()) {
            mInteractor.getStatusAuth()
        }
    }

    @Subscribe
    override fun onEventListener(event: LoginEvent) {
        mView!!.hideProgress()

        when(event.typeEvent) {
            LoginEventConst.STATUS_AUTH_SUCCESS -> {
                if (setProgress()) {
                    mView!!.showMessageStarting()
                    mView!!.openMainActivity()
                }
            }
            LoginEventConst.STATUS_AUTH_ERROR -> mView!!.openUILogin()
            LoginEventConst.ERROR_SERVER -> mView!!.showError(event.resMsg!!)
        }
    }
}