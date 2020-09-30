package com.skysam.hchirinos.textingcurso.profileModule.view

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.common.UtilsCommon
import com.skysam.hchirinos.textingcurso.common.pojo.UserConst
import com.skysam.hchirinos.textingcurso.databinding.ActivityProfileBinding
import com.skysam.hchirinos.textingcurso.profileModule.ProfilePresenter
import com.skysam.hchirinos.textingcurso.profileModule.ProfilePresenterClass

class ProfileActivity : AppCompatActivity(), ProfileView {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var mCurrentMenuItem: MenuItem
    private lateinit var mPresenter: ProfilePresenter

    object Const {
        const val RC_PHOTO_PICKER = 22
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mPresenter = ProfilePresenterClass(this)
        mPresenter.onCreate()
        mPresenter.setupUser(intent.getStringExtra(UserConst.USERNAME)!!, intent.getStringExtra(UserConst.EMAIL)!!,
            intent.getStringExtra(UserConst.PHOTO_URL)!!)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                if (UtilsCommon.hasMaterialDesing()) {
                    finishAfterTransition()
                } else {
                    finish()
                }
            }
            R.id.action_save_profile -> {
                mCurrentMenuItem = item
                if (!binding.etUsername.text.isNullOrEmpty()) {
                    mPresenter.updateUsername(binding.etUsername.text.toString().trim())
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

    private fun setImageProfile(photoUrl: String) {
        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.ic_timer_sand)
            .error(R.drawable.ic_emoticon_sad)
            .centerCrop()

        Glide.with(this).asBitmap()
            .load(photoUrl)
            .apply(options)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    //hideProgressImage()
                    binding.imgProfile.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_upload))
                    return true
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    //hideProgressImage()
                    binding.imgProfile.setImageBitmap(resource)
                    return true
                }
            }).into(binding.imgProfile)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mPresenter.result(requestCode, resultCode, data)
    }


    override fun enableUIElements() {
        setInputs(true)
    }

    override fun disableUIElements() {
        setInputs(false)
    }

    override fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showProgressImage() {
        binding.progressBarImage.visibility = View.VISIBLE
    }

    override fun hideProgressImage() {
        binding.progressBarImage.visibility = View.GONE
    }

    override fun showUserData(username: String, email: String, photoUrl: String) {
        setImageProfile(photoUrl)
        binding.etUsername.setText(username)
        binding.etEmail.setText(email)

    }

    override fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, Const.RC_PHOTO_PICKER)
    }

    override fun openDialogPreview(data: Intent) {
        TODO("Not yet implemented")
    }

    override fun menuEditMode() {
        mCurrentMenuItem.icon = ContextCompat.getDrawable(this, R.drawable.ic_check)
    }

    override fun menuNormalMode() {
        if (mCurrentMenuItem != null) {
            mCurrentMenuItem.isEnabled = true
            mCurrentMenuItem.icon = ContextCompat.getDrawable(this, R.drawable.ic_pencil)
        }
    }

    override fun saveUsernameSuccess() {
        Snackbar.make(binding.contentMain, R.string.profile_message_userUpdated, Snackbar.LENGTH_SHORT).show()
    }

    override fun updateImageSucces(photoUrl: String) {
       setImageProfile(photoUrl)
        Snackbar.make(binding.contentMain, R.string.profile_message_imageUpdated, Snackbar.LENGTH_SHORT).show()
    }

    override fun setResultOK(username: String, photoUrl: String) {
        
    }

    override fun onErrorUpload(resMsg: Int) {
        TODO("Not yet implemented")
    }

    override fun onError(resMsg: Int) {
        TODO("Not yet implemented")
    }

    private fun setInputs(enable: Boolean) {
        binding.etEmail.isEnabled = enable
        binding.btnEditPhoto.visibility = if (enable) View.VISIBLE else View.GONE
        if (mCurrentMenuItem != null) mCurrentMenuItem.isEnabled = enable
    }
}