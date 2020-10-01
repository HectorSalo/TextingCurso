package com.skysam.hchirinos.textingcurso.chatModule.view.adapters

import com.skysam.hchirinos.textingcurso.common.pojo.Message

interface onItemClickListener {
    fun onImageLoaded()
    fun onClickIamge(message: Message)
}