package com.fsociety2.dyslexiafriendlybuddy;

import android.app.ActionBar;
import android.app.Activity;
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
    String morph;
    String senttext;
    String data;
    EditText editText;

  //  TextView textView3;


    TextView exampleText;
    TextView phoneticText;
    TextView morphText;
    TextView defText;


    MyDictionaryRequest myDictionaryRequest;
    MorphologicalStructure morphStructure;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getSupportActionBar().setTitle("Whatever title");
        super.onCreate(savedInstanceState);
        setTitle(R.string.your_title);
        setContentView(R.layout.activity_dictionary);
    //  final ActionBar actionBar = getActionBar();
     // getActionBar().setTitle("Your title");
        editText = findViewById(R.id.editText);
       // textView3 = findViewById(R.id.defText);
        exampleText=findViewById(R.id.exampleText);
        phoneticText=findViewById(R.id.phoneticText);
        morphText=findViewById(R.id.morphText);
        defText=findViewById(R.id.defText);

      //  listenDef = findViewById(R.id.defsound);
      //  listenExample = findViewById(R.id.morphSound);

        if(getIntent().getStringExtra(Intent.EXTRA_PROCESS_TEXT) != null){
            senttext = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString();
            Log.d("DICTLOG", "Text : " + senttext);
            editText.setText(senttext);

        }

    }


    public void requestApiButtonClick(View v) {
        defText.setText("");
        phoneticText.setText("");
        exampleText.setText("");
        myDictionaryRequest = new MyDictionaryRequest(this, defText, exampleText, phoneticText);
        morphStructure= new MorphologicalStructure ();
        urlx= dictionaryEntries();
        data= editText.getText().toString().toLowerCase();
        morph=morphStructure.Morphlogical(data);
        morphText.setText(morph);
        myDictionaryRequest.execute(urlx);
        defText.setText(def);



    }


      //  public void onClick(View v) {

          //  String dataString  = defText.getText().toString();
         //   Log.d("DICTLOG", "string : " + dataString);
         //   dictTTS.setPitch(pitch);
         //   dictTTS.setSpeechRate(speed);
          //  dictTTS.speak(dataString, dictTTS.QUEUE_ADD, null, null);
       // }



    private String dictionaryEntries() {
        final String language = "en";
        final String word = editText.getText().toString();
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
    }



}
