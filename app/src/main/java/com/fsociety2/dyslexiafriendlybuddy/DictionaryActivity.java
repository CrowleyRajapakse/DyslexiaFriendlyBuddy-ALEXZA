package com.fsociety2.dyslexiafriendlybuddy;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
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
    MediaStore.Audio audioFile;
    String urlx;
    String def;
    String senttext;
    EditText editText;
    TextView textView3;
    TextView exampleText;
    TextView phoneticText;
    TextView morphText;
    private TextToSpeech textToSpeech;
    MyDictionaryRequest myDictionaryRequest;
    ImageView sound;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
     //   final ActionBar actionBar = getActionBar();
     //   getActionBar().setTitle("Your title");
        editText = findViewById(R.id.editText);
        textView3 = findViewById(R.id.textView3);
        exampleText=findViewById(R.id.exampleText);
        phoneticText=findViewById(R.id.phoneticText);
        morphText=findViewById(R.id.morphText);


        if(getIntent().getStringExtra(Intent.EXTRA_PROCESS_TEXT) != null){
            senttext = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString();
            Log.d("DICTLOG", "Text : " + senttext);
            editText.setText(senttext);

        }

    }


    public void requestApiButtonClick(View v) {
        myDictionaryRequest = new MyDictionaryRequest(this, textView3, exampleText, phoneticText);

        urlx= dictionaryEntries();
        myDictionaryRequest.execute(urlx);
        textView3.setText(def);
        morphologicalStrcuture(senttext);



    }


    private String dictionaryEntries() {
        final String language = "en";
        final String word = editText.getText().toString();
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
    }


    public void morphologicalStrcuture(String word){

        morphText.setText(word);
        Log.d("DICTLOG", "morphText : " + word);

    }
}
