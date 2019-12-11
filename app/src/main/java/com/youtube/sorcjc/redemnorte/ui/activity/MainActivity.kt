package com.youtube.sorcjc.redemnorte.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.youtube.sorcjc.redemnorte.Global
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.io.MyApiAdapter
import com.youtube.sorcjc.redemnorte.model.User
import com.youtube.sorcjc.redemnorte.util.PreferenceHelper
import com.youtube.sorcjc.redemnorte.util.PreferenceHelper.get
import com.youtube.sorcjc.redemnorte.util.PreferenceHelper.set
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

                val call = MyApiAdapter.getApiService().postLogin(username, password)
                call.enqueue(this)

                preferences["username"] = username
            }
            R.id.btnCall ->
                Toast.makeText(this, "Julisa Mendoza: 953 637 576", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResponse(call: Call<User>, response: Response<User>) {
        if (response.isSuccessful) {
            val user = response.body()
            user?.let { login(it) }
        } else {
            toast("Los datos ingresados no coinciden con ningún usuario")
        }
    }

    private fun login(user: User) {
        val intentPanel = Intent(this, PanelActivity::class.java)
        startActivity(intentPanel)

        toast("Bienvenido ${user.name}!")
    }

    override fun onFailure(call: Call<User>, t: Throwable) {
        toast(t.localizedMessage)
    }
}