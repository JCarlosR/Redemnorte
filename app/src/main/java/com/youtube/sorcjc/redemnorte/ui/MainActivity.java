package com.youtube.sorcjc.redemnorte.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.youtube.sorcjc.redemnorte.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        Button btnCall = (Button) findViewById(R.id.btnCall);
        btnCall.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                if (username.equals("76474871") && password.equals("123123")) {
                    saveLoginSharedPreferences(username);

                    Intent intentPanel = new Intent(this, PanelActivity.class);
                    startActivity(intentPanel);
                } else {
                    Toast.makeText(this, R.string.error_login, Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btnCall:
                Toast.makeText(this, "Are you serious?", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void saveLoginSharedPreferences(String username) {
        SharedPreferences sharedPref = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", username);
        editor.apply();
    }
}
