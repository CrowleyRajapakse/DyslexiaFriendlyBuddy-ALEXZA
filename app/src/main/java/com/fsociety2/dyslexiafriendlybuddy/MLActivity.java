package com.fsociety2.dyslexiafriendlybuddy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

public class MLActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ml);

        TextView textView = (TextView)findViewById(R.id.hwtxt);

        String capturedText = getIntent().getExtras().getString("data");

        String [] words = capturedText.split(" ");
        ArrayList<String> hardWords = new ArrayList<>();

        for(String s: words){

            if(s.length()>12 && !s.matches(".*\\d+.*")){
                hardWords.add(s+"\n");
            }
        }

        textView.setText(hardWords.toString().substring(1, hardWords.toString().length()-1).replaceAll("[\\.,\" \"]",""));

    }
}
