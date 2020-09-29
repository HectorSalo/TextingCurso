package com.skysam.hchirinos.textingcurso.mainModule.view.adapters

import com.skysam.hchirinos.textingcurso.common.pojo.User

interface OnItemClickListener {
    fun onItemClick(user: User)
    fun onItemLongClick(user: User)

    fun onAcceptRequest(user: User)
    fun onDenyRequest(user: User)
}