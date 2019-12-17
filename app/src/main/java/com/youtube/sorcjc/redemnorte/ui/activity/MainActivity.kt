package com.youtube.sorcjc.redemnorte.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.io.MyApiAdapter
import com.youtube.sorcjc.redemnorte.model.User
import com.youtube.sorcjc.redemnorte.util.PreferenceHelper
import com.youtube.sorcjc.redemnorte.util.PreferenceHelper.get
import com.youtube.sorcjc.redemnorte.util.PreferenceHelper.set
import com.youtube.sorcjc.redemnorte.util.showConfirmDialog
import com.youtube.sorcjc.redemnorte.util.showInfoDialog
import com.youtube.sorcjc.redemnorte.util.toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), View.OnClickListener, Callback<User> {

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin.setOnClickListener(this)
        btnCall.setOnClickListener(this)

        writeLastAuthenticatedUser()
    }

    private fun writeLastAuthenticatedUser() {
        etUsername.setText(preferences["username", ""])
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnLogin -> {
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()

                if (username.trim().isEmpty() || password.trim().isEmpty())
                    return

                btnLogin.isEnabled = false

                val call = MyApiAdapter.getApiService().postLogin(username, password)
                call.enqueue(this)

                preferences["username"] = username
            }
            R.id.btnCall ->
                toast(getString(R.string.support_contact_info))
        }
    }

    override fun onResponse(call: Call<User>, response: Response<User>) {
        if (response.isSuccessful) {
            val user = response.body()

            user?.let {
                preferences["user_id"] = user.id
                showConfirmDialog(
                        getString(R.string.dialog_welcome_title),
                        getString(R.string.dialog_welcome_message, user.name),
                        getString(R.string.dialog_welcome_btn_positive),
                        getString(R.string.dialog_welcome_btn_negative)
                ) {
                    login(it)
                }
            }
        } else {
            toast(getString(R.string.error_login_invalid_credentials))
        }

        btnLogin.isEnabled = true
    }

    private fun login(user: User) {
        val intentPanel = Intent(this, PanelActivity::class.java)
        startActivity(intentPanel)
    }

    override fun onFailure(call: Call<User>, t: Throwable) {
        toast(t.localizedMessage ?: "")
        btnLogin.isEnabled = true
    }
}