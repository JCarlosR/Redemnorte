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

import com.youtube.sorcjc.redemnorte.Global;
import com.youtube.sorcjc.redemnorte.R;
import com.youtube.sorcjc.redemnorte.io.RedemnorteApiAdapter;
import com.youtube.sorcjc.redemnorte.io.response.SimpleResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Callback<SimpleResponse> {

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

        writeLastAuthenticatedUser();
    }

    private void writeLastAuthenticatedUser() {
        etUsername.setText(Global.getFromSharedPreferences(this, "username"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                Call<SimpleResponse> call = RedemnorteApiAdapter.getApiService().getLogin(username, password);
                call.enqueue(this);

                Global.saveInSharedPreferences(this, "username", username);
                break;
            case R.id.btnCall:
                Toast.makeText(this, "Julisa Mendoza: 953 637 576", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
        if (response.isSuccessful()) {
            SimpleResponse simpleResponse = response.body();

            if (! simpleResponse.isError()) {
                Intent intentPanel = new Intent(this, PanelActivity.class);
                startActivity(intentPanel);
            }

            // The same variable show a welcome message or error message
            Toast.makeText(this, simpleResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<SimpleResponse> call, Throwable t) {
        Toast.makeText(this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
