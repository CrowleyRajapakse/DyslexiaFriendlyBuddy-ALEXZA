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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.Locale;

public class DictionaryActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    MediaStore.Audio audioFile;

    String urlx;
    String def;
    String morph;
    String senttext;
    String data;
    EditText editText;
    ImageView exampleImage;


    TextView exampleText;
    TextView phoneticText;
    TextView morphText;
    TextView defText;
    ImageView ivHardWord;

    ImageView listenDef, listenExample, listenWord;
    TextToSpeech tts;

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
        ivHardWord = findViewById(R.id.ivHardWord);
        listenDef = findViewById(R.id.defsound);
        listenExample = findViewById(R.id.Examplesound);
        listenWord = findViewById(R.id.pronouncesound);

        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, 1);

        if (getIntent().getStringExtra(Intent.EXTRA_PROCESS_TEXT) != null) {
            senttext = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString();
            Log.d("DICTLOG", "Text : " + senttext);
            editText.setText(senttext);

        }

        listenWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dataString = editText.getText().toString();
                tts.speak(dataString, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        listenDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dataString = defText.getText().toString();
                tts.speak(dataString, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        listenExample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dataString = exampleText.getText().toString();
                tts.speak(dataString, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        ivHardWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = "Thanks For Contributing ALEXZA Project !";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(DictionaryActivity.this, text, duration);
                toast.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //create the TTS instance
                tts = new TextToSpeech(this, this);
            } else {
                //missing data will be installed
                Intent intent = new Intent();
                intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            if (tts.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE) {
                //loading a language
                tts.setLanguage(Locale.US);
            }
        }
        if (status == TextToSpeech.ERROR) {
            Toast.makeText(getApplicationContext(), "Sorry! Text to Speech failed ... ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tts != null) {
            //.setPitch(pitch);
            // textToSpeech.setSpeechRate(speed);
        } else {
            Intent intent = new Intent();
            intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(intent, 1);
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