package com.fsociety2.dyslexiafriendlybuddy;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DictionaryActivity extends AppCompatActivity {
    String url;
    String urlx;
    String def;
    String inf;
    String inflection;
    EditText editText;
    TextView textView3;
    private TextToSpeech textToSpeech;
    MyDictionaryRequest myDictionaryRequest;
    ImageView sound;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        editText = findViewById(R.id.editText);
        textView3 = findViewById(R.id.textView3);


        if(getIntent().getStringExtra(Intent.EXTRA_PROCESS_TEXT) != null){
            String text = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString();
            Log.d("DICTLOG", "Text : " + text);
            editText.setText(text);

        }

    }


    public void requestApiButtonClick(View v) {
        myDictionaryRequest = new MyDictionaryRequest(this, textView3, inflection);

        urlx= dictionaryEntries();
        myDictionaryRequest.execute(urlx);
        textView3.setText(def);

    }


    private String dictionaryEntries() {
        final String language = "en";
        final String word = editText.getText().toString();
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
    }
}
