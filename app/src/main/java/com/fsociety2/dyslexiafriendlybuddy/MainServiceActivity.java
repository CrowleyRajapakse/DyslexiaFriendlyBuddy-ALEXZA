package com.fsociety2.dyslexiafriendlybuddy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainServiceActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ImageView listen, ttsSettingBtn, backwardBtn;
    private TextToSpeech textToSpeech;
    private float pitch;
    private float speed;
    private ImageView stop;


    final String TAG = "LOGCATCHUNK";
    private int textSize;
    private int wordCount;
    private int fontColor;
    private int backColor;
    private String fontStyle;
    private TextView dataView;

    int currentPage;
    int pageCount;
    String pages[];
    String words[];
    String textFromCam;
    private ImageView btnNextPage;
    private ImageView chunkInterface;

    private Activity activity;
    private Button btnMl;
    private SpannableString spnString;
    Volley volley;
    RequestQueue requestQueue;


    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_service);


        dataView = findViewById(R.id.textData);
        btnNextPage = findViewById(R.id.nextChunk);


        listen = findViewById(R.id.btn_play);
        stop = findViewById(R.id.btn_stop);
        chunkInterface = findViewById(R.id.toChunkInterface);
        btnMl = findViewById(R.id.btn_hw);
        ttsSettingBtn = findViewById(R.id.tts_settings);
        backwardBtn = findViewById(R.id.backward);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainServiceActivity.this.getApplicationContext());
        textSize = sharedPreferences.getInt(ChunkingActivity.EXTRA_FONT_SIZE, 100);
        wordCount = sharedPreferences.getInt(ChunkingActivity.EXTRA_WORD_COUNT, 0);
        fontStyle = sharedPreferences.getString(ChunkingActivity.EXTRA_FONT_STYLE, "Aller");
        fontColor = sharedPreferences.getInt(ChunkingActivity.EXTRA_FONT_COLOR, 0xFF0000);
        backColor = sharedPreferences.getInt(ChunkingActivity.EXTRA_BACK_COLOR, 0xFFFF00);


        textFromCam = getIntent().getExtras().getString("data");
        //textFromCam = "Dyslexia is a common learning difficulty that can cause problems with reading, writing and spelling. It's a specific learning difficulty, which means it causes problems with certain abilities used for learning, such as reading and writing. Unlike a learning disability, intelligence isn't affected.";

        dataView.setTextColor(Color.parseColor("#FF0000"));
        dataView.setBackgroundColor(Color.parseColor("#FFFF00"));
        dataView.setText(textFromCam);
        dataView.setTextSize(100);

        spnString = new SpannableString(textFromCam);

        Log.d("LOGCATCHUNK", "Text : " + dataView.getText());
        dataView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        final String[] dataArr = textFromCam.split(" ");

        ImageView button2 = findViewById(R.id.toChunkInterface);
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

        backwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage > 0) {
                    currentPage--;
                    dataView.setText(pages[currentPage]);
                } else {
                    String text = "No pages left!";
                    Toast toast = Toast.makeText(MainServiceActivity.this, text, Toast.LENGTH_SHORT);
                    toast.show();

                }
            }
        });

        ttsSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainServiceActivity.this, TTS_SettingsActivity.class);
                MainServiceActivity.this.startActivity(intent);
            }
        });

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataString = dataView.getText().toString();
                setupHighlighter(dataString);
                textToSpeech.speak(dataString, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textToSpeech != null) {
                    textToSpeech.stop();
                    timer.cancel();
                }
            }
        });
        //check the presence of the TTS resources
        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, 1);

        requestQueue = Volley.newRequestQueue(this);

        btnMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isInternetConnection()) {
                    String text = "No Internet Access!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(MainServiceActivity.this, text, duration);
                    toast.show();
                } else {

                    final ProgressDialog progress = new ProgressDialog(MainServiceActivity.this);
                    progress.setTitle("Connecting...");
                    progress.setMessage("Fetching Data...");
                    progress.show();

                    Runnable progressRunnable = new Runnable() {
                        @Override
                        public void run() {
                            progress.cancel();

                            String fileName = "hard_words";
                            String[] savedWords = null;
                            ArrayList<String> hw = new ArrayList<>();
                            try{
                                FileInputStream fin = openFileInput(fileName);

                                DataInputStream in = new DataInputStream(fin);
                                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                                String strLine;

                                while ((strLine = br.readLine()) != null){
                                    savedWords = strLine.split("\\s+");
                                }

                                in.close();
                                fin.close();
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            ArrayList<String> feed = new ArrayList<>();
                            for (String word : dataArr) {
                                if (word.length() > 4) {
                                    feed.add(word);
                                }
                            }

                            hw = sendWordRequest(savedWords, feed);
                            System.out.println("############## "+hw.size());
                            for (String word : hw) {
                                int primaryColor = Color.RED;
                                dataView.setText(highlight(primaryColor, spnString, word));
                                spnString = new SpannableString(dataView.getText());
                            }
                        }
                    };

                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable, 3000);
                }
            }
        });

    }

    Timer timer;
    TimerTask timerTask;

    private void setupHighlighter(final String dataString) {
        timer = new Timer();
        final int[] i = {-1};
        final String[] arr = dataString.split(" ");
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        i[0]++;
                        if (i[0] < arr.length) {
                            Log.i(TAG, "highlighted text : " + arr[i[0]]);
                            final String toSpeak = arr[i[0]].toUpperCase();
                            String preString = "";
                            int start = 0;
                            int end = 0;
                            if (i[0] > 0) {
                                for (int j = 0; j < i[0]; j++) {
                                    arr[j] = arr[j].toLowerCase();
                                    preString = preString.concat(arr[j]);
                                    preString = preString.concat(" ");
                                }
                            }
                            Log.i(TAG, "highlighted text  start text: " + preString);
                            Log.i(TAG, "highlighted text  start: " + start);
                            start = preString.length();
                            preString = preString.concat(arr[i[0]]);
                            end = preString.length();
                            Log.i(TAG, "highlighted text  end text: " + preString);
                            Log.i(TAG, "highlighted text  end: " + end);


                            final SpannableString spanString = new SpannableString(dataString);
                            spanString.setSpan(new ForegroundColorSpan(Color.YELLOW), start, end, 0);
                            dataView.setText(spanString);
                            Log.i(TAG, "highlighted text  high: " + toSpeak);
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, (long) (speed * 1000));
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
                pitch = (sharedPreferences.getFloat(TTS_SettingsActivity.EXTRA_PITCH_RATE, 10));
                textToSpeech.setPitch(pitch);
                speed = (sharedPreferences.getFloat(TTS_SettingsActivity.EXTRA_SPEED_RATE, 10));
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
        textSize = sharedPreferences.getInt(ChunkingActivity.EXTRA_FONT_SIZE, 100);
        wordCount = sharedPreferences.getInt(ChunkingActivity.EXTRA_WORD_COUNT, 0);
        fontStyle = sharedPreferences.getString(ChunkingActivity.EXTRA_FONT_STYLE, "Aller");
        fontColor = sharedPreferences.getInt(ChunkingActivity.EXTRA_FONT_COLOR, 0xFF0000);
        backColor = sharedPreferences.getInt(ChunkingActivity.EXTRA_BACK_COLOR, 0xFFFF00);

        Log.d(TAG, "style : " + fontStyle);

        switch (fontStyle) {

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
        dataView.setTextColor(fontColor);
        dataView.setBackgroundColor(backColor);


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
            words = textFromCam.split("\\s");
            Log.d(TAG, "words length : " + words.length);
            pageCount = (int) Math.ceil(words.length / (wordCount * 1.0));
            Log.d(TAG, "Page count : " + pageCount);
            Log.d(TAG, "Word count : " + wordCount);

            pages = new String[pageCount];
            Log.d(TAG, "CHUNKING pages length : " + pages.length);
//iterate no of pages
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
     *
     * @return
     */
    public boolean isInternetConnection() {

        ConnectivityManager cm = (ConnectivityManager) MainServiceActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    volatile static ArrayList<String> hardWords = new ArrayList<>();
    /**
     * hard word identification process from the captured feed
     * @param savedWords
     * @param feed
     */
    public ArrayList<String> sendWordRequest(String[] savedWords, List<String> feed) {

        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        //final static ArrayList<String> hardWords = new ArrayList<>();

        for (String dat : savedWords) {
            array.put(dat);
        }

        for(String word : feed){
            array2.put(word);
        }

        try {
            object.put("data", array);
            object.put("feed", array2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Mytag", "JSON : " + object);

        String url = "http://192.168.8.100:5000/predict";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        JSONArray array3 = new JSONArray();
                        try {
                            array3 = response.getJSONArray("hard_words");

                            if(array3 != null){
                                for(int i=0; i<array3.length(); i++){
                                    hardWords.add(array3.getString(i));
                                }
                                System.out.println("inner: "+hardWords.size());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("Mytag", "Result : " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mytag", "error : " + error.getMessage());
                error.printStackTrace();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
        System.out.println("outer: "+hardWords.size());
        return hardWords;
    }

    /**
     * highlight a given word
     *
     * @param color
     * @param original
     * @param word
     * @return
     */
    private Spannable highlight(int color, Spannable original, String word) {
        String normalized = Normalizer.normalize(original, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        int start = normalized.indexOf(word);
        if (start < 0) {
            return original;
        } else {
            Spannable highlighted = new SpannableString(original);
            while (start >= 0) {
                int spanStart = Math.min(start, original.length());
                int spanEnd = Math.min(start + word.length(), original.length());

                highlighted.setSpan(new ForegroundColorSpan(color), spanStart,
                        spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                start = normalized.indexOf(word, spanEnd);
            }
            return highlighted;
        }
    }
}