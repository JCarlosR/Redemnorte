package com.youtube.sorcjc.redemnorte;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String headerCode = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             headerCode = extras.getString("headerCode");
        }

        TextView tvHeaderCode = (TextView) findViewById(R.id.tvHeaderCode);
        tvHeaderCode.setText(headerCode);
    }
}
