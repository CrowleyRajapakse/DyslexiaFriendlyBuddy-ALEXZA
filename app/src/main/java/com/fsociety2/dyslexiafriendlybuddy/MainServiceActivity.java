package com.fsociety2.dyslexiafriendlybuddy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Locale;

public class MainServiceActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ImageView listen;
    private TextToSpeech textToSpeech;
    private float pitch;
    private float speed;
    private ImageView stop;

    final String TAG = "LOGCATCHUNK";
    int textSize;
    int wordCount;
    String fontstyle;
    String fontcolor;
    String backcolor;
    private TextView dataView;

    int currentPage;
    int pageCount;
    String pages[];
    String words[];
    String textFromCam;
    private ImageView btnNextPage;
    private ImageView button2;

    private Activity activity;
    private Button mlButton;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_service);


        dataView = findViewById(R.id.textData);
        btnNextPage = findViewById(R.id.nextchunk);
        listen = findViewById(R.id.btn_play);
        stop = findViewById(R.id.btn_stop);
        button2 = findViewById(R.id.tochunkinterface);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainServiceActivity.this.getApplicationContext());
        textSize = sharedPreferences.getInt(ChunkingActivity.EXTRA_FONT_SIZE, 10);

        //dataView.setText(getIntent().getExtras().getString("data"));
        textFromCam = getIntent().getExtras().getString("data");
        dataView.setText(textFromCam);

        Log.d("LOGCATCHUNK", "Text : " + dataView.getText());
        dataView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        String[] dataArr = textFromCam.split(" ");

        ImageView button2 = findViewById(R.id.tochunkinterface);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainServiceActivity.this, ChunkingActivity.class);
                MainServiceActivity.this.startActivity(intent);
            }
        });


        btnNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < pageCount - 1) {
                    currentPage++;
                    dataView.setText(pages[currentPage]);
                } else {
                    String text = "Reading Finished!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(MainServiceActivity.this, text, duration);
                    toast.show();

                }
            }
        });


        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = dataView.getText().toString();
                textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textToSpeech != null) {
                    textToSpeech.stop();
                }
            }
        });
        //check the presence of the TTS resources
        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, 1);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //create the TTS instance
                textToSpeech = new TextToSpeech(this, this);
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
            if (textToSpeech.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE) {
                //loading a language
                textToSpeech.setLanguage(Locale.US);
                pitch = sharedPreferences.getFloat(TTS_SettingsActivity.EXTRA_PITCH_RATE, 10);
                textToSpeech.setPitch(pitch);
                speed = sharedPreferences.getFloat(TTS_SettingsActivity.EXTRA_SPEED_RATE, 10);
                textToSpeech.setSpeechRate(speed);
            }
        }
        if (status == TextToSpeech.ERROR) {
            Toast.makeText(getApplicationContext(), "Sorry! Text to Speech failed ... ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        textSize = sharedPreferences.getInt(ChunkingActivity.EXTRA_FONT_SIZE, 10);
        wordCount = sharedPreferences.getInt(ChunkingActivity.EXTRA_WORD_COUNT, 0);
        fontstyle = sharedPreferences.getString(ChunkingActivity.EXTRA_FONT_STYLE, "default");
        fontcolor = sharedPreferences.getString(ChunkingActivity.EXTRA_FONT_COLOR, "Green");
        backcolor = sharedPreferences.getString(ChunkingActivity.EXTRA_BACK_COLOR, "Grey");
        Log.d(TAG, "style : " + fontstyle);

        switch (fontstyle) {

            case "Sans Serif":
                dataView.setTypeface(Typeface.SANS_SERIF);
                break;
            case "Serif":
                dataView.setTypeface(Typeface.SERIF);
                break;
            case "Monospace":
                dataView.setTypeface(Typeface.MONOSPACE);
                break;
            case "Aller":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Aller_Rg.ttf"));
                break;
            case "Arial":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/arial.ttf"));
                break;
            case "Arimo":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Arimo.ttf"));
                break;
            case "Caviar Dreams":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams.ttf"));
                break;
            case "Lato":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lato.ttf"));
                break;
            case "Ostrich":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ostrich.ttf"));
                break;
            case "Oswald":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Oswald.ttf"));
                break;
            case "Playfair":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/PlayfairDisplay.otf"));
                break;
            case "Roboto":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto.ttf"));
                break;
            case "Titillium":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Titillium.otf"));
                break;
            case "Walkway":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Walkway.ttf"));
                break;
            default:
                break;
        }


        switch (fontcolor) {
            case "Blue":
                dataView.setTextColor((Color.rgb(82, 96, 168)));
                break;
            case "Green":
                dataView.setTextColor((Color.rgb(31, 186, 71)));
                break;
            case "Red":
                dataView.setTextColor((Color.rgb(255, 0, 0)));
                break;
            case "Black":
                dataView.setTextColor((Color.rgb(0, 0, 0)));
                break;
            default:
                break;
        }

        switch (backcolor) {
            case "Yellow":
                dataView.setBackgroundColor((Color.rgb(250, 252, 103)));
                break;
            case "Light Blue":
                dataView.setBackgroundColor((Color.rgb(170, 249, 225)));
                break;
            case "Salmon Pink":
                dataView.setBackgroundColor((Color.rgb(255, 160, 122)));
                break;
            case "Grey":
                dataView.setBackgroundColor((Color.rgb(192, 192, 192)));
                break;
            default:
                break;
        }


        Log.d(TAG, "WOrd count : " + wordCount);
        dataView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        // dataView.setTextColor((Color.rgb(0, 87, 48)));
        // dataView.setBackgroundColor((Color.rgb(192, 192, 192)));

        if (wordCount > 0) {
            chunckText();
            btnNextPage.setVisibility(View.VISIBLE);
        } else {
            btnNextPage.setVisibility(View.GONE);
            dataView.setText(textFromCam);
        }


        pitch = sharedPreferences.getFloat(TTS_SettingsActivity.EXTRA_PITCH_RATE, 10);
        speed = sharedPreferences.getFloat(TTS_SettingsActivity.EXTRA_SPEED_RATE, 10);

        if (textToSpeech != null) {
            textToSpeech.setPitch(pitch);
            textToSpeech.setSpeechRate(speed);
        } else {
            Intent intent = new Intent();
            intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(intent, 1);
        }
    }


    private void chunckText() {
        if (wordCount > 0) {
            words = textFromCam.split("\\W+");
            Log.d(TAG, "words length : " + words.length);
            pageCount = (int) Math.ceil(words.length / (wordCount * 1.0));
            Log.d(TAG, "Page count : " + pageCount);
            Log.d(TAG, "Word count : " + wordCount);

            pages = new String[pageCount];
            Log.d(TAG, "CHUNKING pages length : " + pages.length);

            for (int i = 0; i < pageCount; i++) {
                String page = "";
                Log.d(TAG, "CHUNKING init page : " + page);
                for (int j = i * wordCount; j < i * wordCount + wordCount; j++) {
                    if (j < words.length) {
                        page = page.concat(words[j]);
                        page = page.concat(" ");
                        Log.d(TAG, "CHUNKING true word: " + words[j]);
                        Log.d(TAG, "PAge : " + i + " Text : " + page);
                    } else
                        break;
                }
                pages[i] = page;
                Log.d(TAG, "CHUNKING page : " + pages[i]);
            }

            currentPage = 0;
            dataView.setText(pages[currentPage]);
        }

    }
}
