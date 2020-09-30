package com.skysam.hchirinos.textingcurso.addModule.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.addModule.AddPresenter
import com.skysam.hchirinos.textingcurso.addModule.AddPresenterClass
import com.skysam.hchirinos.textingcurso.databinding.DialogAboutBinding
import com.skysam.hchirinos.textingcurso.databinding.FragmentAddBinding


class AddFragment : DialogFragment(), DialogInterface.OnShowListener, AddView {

    private var _binding: FragmentAddBinding? = null
    private val binding: FragmentAddBinding get() = _binding!!

    private lateinit var positiveButton : Button

    private lateinit var mPresenter: AddPresenter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        mPresenter = AddPresenterClass(this)

        _binding = FragmentAddBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(R.string.addFriend_title)
            .setView(binding.root)
            .setPositiveButton(getString(R.string.common_label_accept), null)
            .setNegativeButton(getString(R.string.common_label_cancel), null)

        val dialog: AlertDialog = builder.create()
        dialog.setOnShowListener(this)
        return dialog
    }

    override fun onShow(p0: DialogInterface?) {
        val dialog = dialog as AlertDialog

        positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)
        val negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE)

        positiveButton.setOnClickListener {
            if (binding.etEmail.text.isNullOrEmpty()) {
                binding.tfEmail.error = getString(R.string.common_validate_field_required)
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString().trim()).matches()) {
                binding.tfEmail.error = getString(R.string.common_validate_email_invalid)
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }
            mPresenter.addFriend(binding.etEmail.text.toString().trim())
        }
        negativeButton.setOnClickListener { dismiss() }
        mPresenter.onShow()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

    override fun enableUIElements() {
        binding.etEmail.isEnabled = true
        positiveButton.isEnabled = true
    }

    override fun disableUIElements() {
        binding.etEmail.isEnabled = false
        positiveButton.isEnabled = false
    }

    override fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }

    override fun friendAdded() {
        Toast.makeText(requireActivity(), R.string.addFriend_message_request_dispatched, Toast.LENGTH_SHORT).show()
        dismiss()
    }

    override fun friendNotAdded() {
        binding.tfEmail.error = getString(R.string.addFriend_error_message)
        binding.etEmail.requestFocus()
    }

}