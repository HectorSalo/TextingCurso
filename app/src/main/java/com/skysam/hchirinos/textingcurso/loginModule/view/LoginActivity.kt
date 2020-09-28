package com.skysam.hchirinos.textingcurso.loginModule.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.skysam.hchirinos.textingcurso.R
import com.skysam.hchirinos.textingcurso.databinding.ActivityLoginBinding
import com.skysam.hchirinos.textingcurso.loginModule.LoginPresenterClass
import com.skysam.hchirinos.textingcurso.mainModule.MainActivity
import java.util.*

class LoginActivity : AppCompatActivity(), LoginView {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mPresenter: LoginPresenterClass

    object Const {
        const val RC_SIGN_IN = 21
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mPresenter = LoginPresenterClass(this)
        mPresenter.onCreate()
        mPresenter.getStatusAuth()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mPresenter.result(requestCode, resultCode, data!!)
    }



    override fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }

    override fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    override fun openUILogin() {
        val googleIdp = AuthUI.IdpConfig.GoogleBuilder().build()

        startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setIsSmartLockEnabled(false)
            .setTosAndPrivacyPolicyUrls("www.policy.cursos-android-ant.com", "www.privacity.cursos-android-ant.com")
            .setAvailableProviders(listOf(AuthUI.IdpConfig.EmailBuilder().build(), googleIdp))
            .setTheme(R.style.BlueTheme)
            .setLogo(R.mipmap.ic_launcher)
            .build(), Const.RC_SIGN_IN)
    }

    override fun showLoginSuccessfully(data: Intent) {
        val response = IdpResponse.fromResultIntent(data)
        var email = ""

        if (response != null) {
            email = response.email!!
        }

        Toast.makeText(this, getString(R.string.login_message_success, email), Toast.LENGTH_LONG).show()
    }

    override fun showMessageStarting() {
        binding.tvMessage.setText(R.string.login_message_loading)
    }

    override fun showError(reaMsg: Int) {
        Toast.makeText(this, reaMsg, Toast.LENGTH_LONG).show()
    }
}