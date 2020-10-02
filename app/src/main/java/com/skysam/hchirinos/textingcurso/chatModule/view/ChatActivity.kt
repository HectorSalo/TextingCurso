package com.skysam.hchirinos.textingcurso.chatModule.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.chatModule.ChatPresenter
import com.skysam.hchirinos.textingcurso.chatModule.ChatPresenterClass
import com.skysam.hchirinos.textingcurso.chatModule.view.adapters.ChatAdapter
import com.skysam.hchirinos.textingcurso.chatModule.view.adapters.onItemClickListener
import com.skysam.hchirinos.textingcurso.common.UtilsCommon
import com.skysam.hchirinos.textingcurso.common.pojo.Message
import com.skysam.hchirinos.textingcurso.common.pojo.UserConst
import com.skysam.hchirinos.textingcurso.databinding.ActivityChatBinding
import com.skysam.hchirinos.textingcurso.databinding.DialogImageUploadPreviewBinding
import com.skysam.hchirinos.textingcurso.mainModule.view.MainActivity
import com.skysam.hchirinos.textingcurso.profileModule.view.ProfileActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity(), onItemClickListener, ChatView, OnImageZoom {

    private lateinit var binding: ActivityChatBinding
    private lateinit var mAdapter: ChatAdapter
    private lateinit var mPresenter: ChatPresenter
    private lateinit var messageSelected: Message

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mPresenter = ChatPresenterClass(this)
        mPresenter.onCreate()

        mAdapter = ChatAdapter(ArrayList<Message>(), this)
        binding.includeContentChat.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.includeContentChat.recyclerView.adapter = mAdapter

        configToolbar(intent)

        binding.includeContentChat.btnSendMessage.setOnClickListener {
            if (UtilsCommon.validateMessage(binding.includeContentChat.etMessage)) {
                mPresenter.sendMessage(binding.includeContentChat.etMessage.text.toString().trim())
                binding.includeContentChat.etMessage.setText("")
            }
        }

        binding.includeContentChat.btnGallery.setOnClickListener {
            checkPermissionToApp(Manifest.permission.READ_EXTERNAL_STORAGE, UtilsCommon.RP_STORAGE)
        }
    }

    private fun configToolbar(intent: Intent?) {
        mPresenter.setupFriend(intent!!.getStringExtra(UserConst.UID)!!, intent.getStringExtra(UserConst.EMAIL)!!)
        binding.includeContentUser.tvName.text = intent.getStringExtra(UserConst.USERNAME)
        binding.includeContentUser.tvStatus.visibility = View.VISIBLE

        val options = RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.ic_emoticon_happy)
            .centerCrop()

        Glide.with(this).asBitmap()
            .load(intent.getStringExtra(UserConst.PHOTO_URL))
            .apply(options)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.includeContentUser.imgPhoto.setImageDrawable(ContextCompat.getDrawable(this@ChatActivity, R.drawable.ic_emoticon_sad))
                    return true
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.includeContentUser.imgPhoto.setImageBitmap(resource)
                    return true
                }
            }).into(binding.includeContentUser.imgPhoto)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        mPresenter.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finishAfterTransition()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mPresenter.result(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when(requestCode) {
                UtilsCommon.RP_STORAGE -> fromGallery()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun fromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, UtilsCommon.RC_PHOTO_PICKER)
    }

    private fun checkPermissionToApp (permissionStr: String, requestPermission: Int) {
        if (ContextCompat.checkSelfPermission(this, permissionStr) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permissionStr), requestPermission)
            return
        }

        when(requestPermission) {
            UtilsCommon.RP_STORAGE -> fromGallery()
        }
    }

    override fun onImageLoaded() {
        binding.includeContentChat.recyclerView.scrollToPosition(mAdapter.itemCount - 1)
    }

    override fun onClickIamge(message: Message) {
        ImageZoomFragment().show(supportFragmentManager, getString(R.string.app_name))
        messageSelected = message
    }

    override fun getMessageSelected(): Message {
        return this.messageSelected
    }

    override fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onStatusUser(connected: Boolean, lastConnection: Long) {
        if (connected) {
            binding.includeContentUser.tvStatus.setText(R.string.chat_status_connected)
        } else {
            binding.includeContentUser.tvStatus.text = getString(R.string.chat_status_last_connection,
                (SimpleDateFormat("dd-MM-yyyy - HH:mm", Locale.ROOT).format(Date(lastConnection))))
        }
    }

    override fun onError(resMsg: Int) {
        Snackbar.make(binding.contentMain, resMsg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onMessageReceived(msg: Message) {
        mAdapter.add(msg)
        binding.includeContentChat.recyclerView.scrollToPosition(mAdapter.itemCount - 1)
    }

    override fun openDialogPreview(data: Intent) {
        val urlLocal = data.dataString

        val bindingDialog = DialogImageUploadPreviewBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.chat_dialog_sendImage_title)
            .setView(bindingDialog.root)
            .setPositiveButton(R.string.chat_dialog_sendImage_accept) { _, _ ->
                mPresenter.sendImage(this, Uri.parse(urlLocal))
            }
            .setNegativeButton(R.string.common_label_cancel, null)

        val alertDialog = builder.create()
        alertDialog.setOnShowListener {
            val sizeImagePreview = resources.getDimensionPixelSize(R.dimen.chat_size_img_preview)
            val bitmap = UtilsCommon.reduceBitmap(this, binding.contentMain, urlLocal, sizeImagePreview, sizeImagePreview)

            if (bitmap != null) {
                bindingDialog.imgDialog.setImageBitmap(bitmap)
            }
            bindingDialog.tvMessage.text = String.format(Locale.ROOT, getString(R.string.chat_dialog_sendImage_message), binding.includeContentUser.tvName.text)
        }
        alertDialog.show()
    }

}