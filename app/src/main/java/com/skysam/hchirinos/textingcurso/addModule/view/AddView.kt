package com.skysam.hchirinos.textingcurso.addModule.view

interface AddView {
    fun enableUIElements()
    fun disableUIElements()
    fun showProgress()
    fun hideProgress()

    fun friendAdded()
    fun friendNotAdded()
    fun showMessageExist(resMsg: Int)
}