package com.fsociety2.dyslexiafriendlybuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.TextView;

public class MainServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_service);

        TextView dataView =findViewById(R.id.textData);
        dataView.setText(getIntent().getExtras().getString("data"));
    }
}
