package com.fsociety2.dyslexiafriendlybuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainServiceActivity extends AppCompatActivity {

    TextView dataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_service);

        dataView =findViewById(R.id.textData);
        dataView.setText(getIntent().getExtras().getString("data"));

        Button mlButton = (Button)findViewById(R.id.mlBtn);
        mlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainServiceActivity.this, MLActivity.class);
                intent.putExtra("data",dataView.getText());
                startActivity(intent);
            }
        });
    }
}
