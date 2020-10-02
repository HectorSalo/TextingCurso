package com.skysam.hchirinos.textingcurso.chatModule.view

import android.app.Dialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.common.UtilsCommon
import com.skysam.hchirinos.textingcurso.databinding.FragmentImageZoomBinding
import java.util.*

class ImageZoomFragment: DialogFragment(), DialogInterface.OnShowListener {
    private val bindingDialog = FragmentImageZoomBinding.inflate(layoutInflater)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.app_name)
            .setView(bindingDialog.root)
            .setPositiveButton(R.string.common_label_ok, null)

        val alertDialog = builder.create()
        alertDialog.setOnShowListener(this)

        return alertDialog
    }


    override fun onShow(p0: DialogInterface?) {
        if (activity != null){
            val photo = (requireActivity() as OnImageZoom).getMessageSelected().photoUrl

            val options = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.ic_timer_sand_160)


            Glide.with(requireActivity()).load(photo)
                .apply(options).into(bindingDialog.pvZoom)
        }
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val window = dialog!!.window
            window!!.setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            window.setGravity(Gravity.CENTER)
        }
        bindingDialog.contentMain.gravity = Gravity.CENTER
    }


}