package com.youtube.sorcjc.redemnorte.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.youtube.sorcjc.redemnorte.Global
import com.youtube.sorcjc.redemnorte.R
import com.youtube.sorcjc.redemnorte.io.RedemnorteApiAdapter
import com.youtube.sorcjc.redemnorte.io.response.SimpleResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), View.OnClickListener, Callback<SimpleResponse?> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin.setOnClickListener(this)
        btnCall.setOnClickListener(this)

        writeLastAuthenticatedUser()
    }

    private fun writeLastAuthenticatedUser() {
        etUsername.setText(Global.getFromSharedPreferences(this, "username"))
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnLogin -> {
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()

                /*
                val call = RedemnorteApiAdapter.getApiService().getLogin(username, password)
                call.enqueue(this)
                */
                login() // (temporal) direct access

                Global.saveInSharedPreferences(this, "username", username)
            }
            R.id.btnCall ->
                Toast.makeText(this, "Julisa Mendoza: 953 637 576", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResponse(call: Call<SimpleResponse?>, response: Response<SimpleResponse?>) {
        if (response.isSuccessful) {
            val simpleResponse = response.body()
            if (simpleResponse != null && !simpleResponse.isError) {
                login()
            }
            // The same variable show a welcome message or error message
            Toast.makeText(this, simpleResponse!!.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun login() {
        val intentPanel = Intent(this, PanelActivity::class.java)
        startActivity(intentPanel)
    }

    override fun onFailure(call: Call<SimpleResponse?>, t: Throwable) {
        Toast.makeText(this, t.localizedMessage, Toast.LENGTH_SHORT).show()
    }
}