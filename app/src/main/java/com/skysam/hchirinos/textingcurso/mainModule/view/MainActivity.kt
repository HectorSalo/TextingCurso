package com.skysam.hchirinos.textingcurso.mainModule.view

import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.addModule.view.AddFragment
import com.skysam.hchirinos.textingcurso.common.UtilsCommon
import com.skysam.hchirinos.textingcurso.common.pojo.User
import com.skysam.hchirinos.textingcurso.databinding.ActivityMainBinding
import com.skysam.hchirinos.textingcurso.databinding.DialogAboutBinding
import com.skysam.hchirinos.textingcurso.loginModule.view.LoginActivity
import com.skysam.hchirinos.textingcurso.mainModule.MainPresenter
import com.skysam.hchirinos.textingcurso.mainModule.MainPresenterClass
import com.skysam.hchirinos.textingcurso.mainModule.view.adapters.OnItemClickListener
import com.skysam.hchirinos.textingcurso.mainModule.view.adapters.RequestAdapter
import com.skysam.hchirinos.textingcurso.mainModule.view.adapters.UserAdapter
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), OnItemClickListener, MainView {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mUserAdapter: UserAdapter
    private lateinit var mRequestAdapter: RequestAdapter
    private lateinit var mUser: User
    private lateinit var mPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mPresenter = MainPresenterClass(this)
        mPresenter.onCreate()
        mUser = mPresenter.getCurrentUser()

        binding.toolbar.title = mUser.getUsernameValid()
        UtilsCommon.loadImage(this, mUser.getPhotoValid()!!, binding.imgProfile)
        setSupportActionBar(binding.toolbar)

        mUserAdapter = UserAdapter(ArrayList(), this)
        mRequestAdapter = RequestAdapter(ArrayList(), this)

        binding.includeMain.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.includeMain.rvUsers.adapter = mUserAdapter

        binding.includeMain.rvRequests.layoutManager = LinearLayoutManager(this)
        binding.includeMain.rvRequests.adapter = mRequestAdapter

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            AddFragment().show(supportFragmentManager, getString(R.string.addFriend_title))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_logout -> {
                mPresenter.signOff()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            R.id.action_about -> openAbout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openAbout() {
        val binding = DialogAboutBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.main_menu_about)
            .setView(binding.root)
            .setPositiveButton(getString(R.string.common_label_ok)) { _, _ ->
            }

        builder.create()
        builder.show()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()

        clearNotification()
    }

    override fun onPause() {
        super.onPause()
        mPresenter.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDestroy()
    }

    private fun clearNotification() {
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    override fun onItemClick(user: User) {
        TODO("Not yet implemented")
    }

    override fun onItemLongClick(user: User) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.main_dialog_title_confirmDelete)
            .setMessage(String.format(Locale.ROOT, getString(R.string.main_dialog_message_confirmDelete), user.getUsernameValid()))
            .setPositiveButton(getString(R.string.main_dialog_accept)) { _, _ ->
                mPresenter.removedFriend(user.uid!!)
            }
            .setNegativeButton(R.string.common_label_cancel
            ) { _, _ ->

            }

        builder.create()
        builder.show()
    }

    override fun onAcceptRequest(user: User) {
        mPresenter.acceptRequest(user)
    }

    override fun onDenyRequest(user: User) {
        mPresenter.denyRequest(user)
    }

    override fun friendAdded(user: User) {
        mUserAdapter.add(user)
    }

    override fun friendUpdated(user: User) {
        mUserAdapter.update(user)
    }

    override fun friendRemoved(user: User) {
        mUserAdapter.remove(user)
    }

    override fun requestAdded(user: User) {
        mRequestAdapter.add(user)
    }

    override fun requestUpdated(user: User) {
        mRequestAdapter.update(user)
    }

    override fun requestRemoved(user: User) {
        mRequestAdapter.remove(user)
    }

    override fun showRequestAccepted(username: String) {
        Snackbar.make(binding.contentMain, getString(R.string.main_message_request_accepted, username), Snackbar.LENGTH_SHORT).show()
    }

    override fun showRequestDenied() {
        Snackbar.make(binding.contentMain, R.string.main_message_request_denied, Snackbar.LENGTH_SHORT).show()
    }

    override fun showFriendRemoved() {
        Snackbar.make(binding.contentMain, R.string.main_message_user_removed, Snackbar.LENGTH_LONG).show()
    }

    override fun showError(resMsg: Int) {
        Snackbar.make(binding.contentMain, resMsg, Snackbar.LENGTH_LONG).show()
    }
}