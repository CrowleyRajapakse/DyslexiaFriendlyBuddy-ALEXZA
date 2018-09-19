package com.fsociety2.dyslexiafriendlybuddy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Arrays;
import java.util.Locale;

public class MainServiceActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ImageView listen;
    private TextToSpeech textToSpeech;
    private float pitch;
    private float speed;
    private ImageView stop;
    MotionEvent motionEvent;

    final String TAG = "LOGCATCHUNK";
    private int textSize;
    private int wordCount;
    private int fontcolor;
    private int backcolor;
    private String fontstyle;
    String word;

    private TextView dataView;

    int currentPage;
    int pageCount;
    int mOffset;
    String pages[];
    String words[];
    String textFromCam;
    private ImageView btnNextPage;
    private ImageView button2;

    private Activity activity;
    private Button btnMl;


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
        btnMl = findViewById(R.id.btn_hw);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainServiceActivity.this.getApplicationContext());
        textSize = sharedPreferences.getInt(ChunkingActivity.EXTRA_FONT_SIZE, 10);


        textFromCam = getIntent().getExtras().getString("data");
        dataView.setText(textFromCam);
        //   dataView.setOnLongClickListener(new View.OnLongClickListener() {

        //      @Override
        //   public boolean onLongClick(View v) {
        //    String text = "Reading Finished!";
        //   int duration = Toast.LENGTH_SHORT;
        //    Toast toast = Toast.makeText(MainServiceActivity.this, text, duration);
        //   toast.show();
        //   return false;
        // }
        // });
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

        btnMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isInternetConnection()){
                    String text = "No Internet Access!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(MainServiceActivity.this, text, duration);
                    toast.show();
                }else{

                    if(Arrays.asList(words).contains("Text")){
                        String text = "hard word ditected";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(MainServiceActivity.this, text, duration);
                        toast.show();
                    }
                    Log.d(TAG, "words length $$$$$$$$$$$$$ : " + words.length + words[1]);

                }

            }
        });

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
        fontstyle = sharedPreferences.getString(ChunkingActivity.EXTRA_FONT_STYLE, "Aller");
        fontcolor = sharedPreferences.getInt(ChunkingActivity.EXTRA_FONT_COLOR, 0xFF0000);
        backcolor = sharedPreferences.getInt(ChunkingActivity.EXTRA_BACK_COLOR, 0xFFFF00);

        Log.d(TAG, "style : " + fontstyle);

        switch (fontstyle) {

            case "Aller":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Aller.ttf"));
                break;

            case "Arimo":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Arimo.ttf"));
                break;
            case "BEBAS":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/BEBAS.ttf"));
                break;
            case "Cartoonist":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Cartoonist.ttf"));
                break;
            case "Caviar":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Caviar.ttf"));
                break;
            case "Helsinki":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Helsinki.ttf"));
                break;
            case "Molengo":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Molengo.ttf"));
                break;
            case "Lato":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lato.ttf"));
                break;
            case "Nobile":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Nobile.ttf"));
                break;
            case "Overlock":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Overlock.ttf"));
                break;
            case "Oswald":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Oswald.ttf"));
                break;
            case "Paprika":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Paprika.ttf"));
                break;
            case "Roboto":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto.ttf"));
                break;
            case "Resagnicto":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Resagnicto.ttf"));
                break;
            case "Sansumi":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Sansumi.ttf"));
                break;
            case "Walkway":
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Walkway.ttf"));
                break;
            default:
                dataView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Aller.ttf"));
                break;
        }
        dataView.setTextColor(fontcolor);
        dataView.setBackgroundColor(backcolor);


        Log.d(TAG, "Word count : " + wordCount);
        dataView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);


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

    /**
     * check internect connection of the device
     * @return
     */
    public boolean isInternetConnection(){

        ConnectivityManager cm = (ConnectivityManager) MainServiceActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}
