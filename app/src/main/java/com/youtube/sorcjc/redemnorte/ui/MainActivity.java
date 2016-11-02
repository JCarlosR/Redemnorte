package com.youtube.sorcjc.redemnorte.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.youtube.sorcjc.redemnorte.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        Button btnCall = (Button) findViewById(R.id.btnCall);
        btnCall.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                Intent intentPanel = new Intent(this, PanelActivity.class);
                startActivity(intentPanel);
                break;
            case R.id.btnCall:
                Toast.makeText(this, "Are you serious?", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
