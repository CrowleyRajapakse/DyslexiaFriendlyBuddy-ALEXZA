package com.fsociety2.dyslexiafriendlybuddy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.InputStream;

public class DictionaryActivity extends AppCompatActivity {
    MediaStore.Audio audioFile;
    String urlx;
    String def;
    String morph;
    String senttext;
    String data;
    EditText editText;
    ImageView exampleImage;

    //  TextView textView3;


    TextView exampleText;
    TextView phoneticText;
    TextView morphText;
    TextView defText;


    MyDictionaryRequest myDictionaryRequest;
    MorphologicalStructure morphStructure;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle(R.string.your_title);
        setContentView(R.layout.activity_dictionary);

        editText = findViewById(R.id.editText);

        exampleText = findViewById(R.id.exampleText);
        phoneticText = findViewById(R.id.phoneticText);
        morphText = findViewById(R.id.morphText);
        defText = findViewById(R.id.defText);
        exampleImage = findViewById(R.id.exampleImage);

        if (getIntent().getStringExtra(Intent.EXTRA_PROCESS_TEXT) != null) {
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
        morphStructure = new MorphologicalStructure();
        urlx = dictionaryEntries();
        data = editText.getText().toString().toLowerCase();
        morph = morphStructure.Morphlogical(data);
        morphText.setText(morph);
        myDictionaryRequest.execute(urlx);
        defText.setText(def);
        addImage();


    }


    private String dictionaryEntries() {
        final String language = "en";
        final String word = editText.getText().toString();
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
    }


    private void addImage() {

        final String word = editText.getText().toString();
        int id = getResources().getIdentifier(word,
                "raw", getPackageName());

        if (id == 0) {
            id = getResources().getIdentifier("noimage",
                    "raw", getPackageName());
        }

        InputStream imageStream = this.getResources().openRawResource(id);
        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
        exampleImage.setImageBitmap(bitmap);

    }


}
