package com.skysam.hchirinos.textingcurso.common.model

import android.net.Uri

interface StorageUploadImageCallback {
    fun onSuccess(newUri: Uri)
    fun onError(resMsg: Int)
}