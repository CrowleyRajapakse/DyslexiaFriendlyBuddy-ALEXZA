package com.fsociety2.dyslexiafriendlybuddy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpConnection;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ResponseCache;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DictionaryActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {


    String dictURL;
    String definition;
    String morphologicalStructure;
    String sentText;
    String data;
    EditText enteredWord;
    ImageView exampleImage;
    TextView exampleText;
    TextView phoneticText;
    TextView morphText;
    TextView defText;


    Button ivHardWord;

    ImageView listenDef, listenExample, listenWord;
    TextToSpeech tts;

    MyDictionaryRequest myDictionaryRequest;
    MorphologicalStructure morphStructure;

    HttpClient client;
    HttpResponse response;

    Volley volley;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle(R.string.your_title);
        setContentView(R.layout.activity_dictionary);

        enteredWord = findViewById(R.id.enteredWord);
        exampleText = findViewById(R.id.exampleText);
        phoneticText = findViewById(R.id.phoneticText);
        morphText = findViewById(R.id.morphText);
        defText = findViewById(R.id.defText);
        exampleImage = findViewById(R.id.exampleImage);


        ivHardWord = findViewById(R.id.ivHardWord);
        listenDef = findViewById(R.id.defSound);
        listenExample = findViewById(R.id.exampleSound);
        listenWord = findViewById(R.id.pronounceSound);

        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, 1);

        if (getIntent().getStringExtra(Intent.EXTRA_PROCESS_TEXT) != null) {
            sentText = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString();
            Log.d("DICTLOG", "Text : " + sentText);
            enteredWord.setText(sentText);

        }

        listenWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dataString = enteredWord.getText().toString();
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

        requestQueue = Volley.newRequestQueue(this);
        ivHardWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HttpParams httpParams = new BasicHttpParams();

                //timeouts just in case the users internet connection is slow
                int timeoutSocket = 20000;
                int timeoutConnection = 20000;

                //set the timeouts
                HttpConnectionParams.setConnectionTimeout(httpParams, timeoutSocket);
                HttpConnectionParams.setSoTimeout(httpParams, timeoutConnection);

                client = new DefaultHttpClient(httpParams);
                String[] datas = new String[1];
                datas[0] = enteredWord.getText().toString().toLowerCase();
                ;
                sendWordRequest(datas);

                String text = "Thanks For Contributing ALEXZA !";
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
        dictURL = dictionaryEntries();
        myDictionaryRequest.execute(dictURL);

        morphStructure = new MorphologicalStructure();
        data = enteredWord.getText().toString().toLowerCase();
        morphologicalStructure = morphStructure.Morphlogical(data);

        morphText.setText(morphologicalStructure);

        addImage();

    }

    private String dictionaryEntries() {

        final String language = "en";
        final String word = enteredWord.getText().toString();
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
    }


    private void addImage() {

        final String word = enteredWord.getText().toString().toLowerCase().trim();
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

//    protected void onPostExecute(String Result) {
//
//        if (Result != null) {
//            Log.i("myAppTag", "(onpostExecute method) Result = Posted");
//        } else {
//            Log.i("myAppTag", "(onPostExecute method) Result = Failed to post!");
//        }
//    }

//    public class HardWordRequest extends AsyncTask<String, Integer, String> {
//
//        final static String url = "http://192.168.1.3:5000/train";
//
//
//        @Override
//        protected String doInBackground(String... arg0) {
//
//            try {
//                HttpPost post = new HttpPost(url.toString());
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//                int i = 0;
//                String[] data = null;
//                data[0] = "test";
//                //holds the values that will sent to the rest service
//
//                //nameValuePairs.add(new BasicNameValuePair("data[1]","contemperory"));
//
//                //set the headers to josn type
//                //post.setHeader("Accept", "application/json");
//                post.setHeader("Content-type", "application/json");
//
//                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                HttpResponse response = client.execute(post);
//
//                int status = response.getStatusLine().getStatusCode();
//                Log.i("myAppTag", "error in doInBg..." + response.getStatusLine().getReasonPhrase());
//
//            } catch (Exception e) {
//                Log.i("myAppTag", "error in doInBg..." + e.toString());
//                return null;
//            }
//            return null;
//        }
//    }

    public void sendWordRequest(String[] data) {

        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        for (String dat : data) {
            array.put(dat);
        }
        try {
            object.put("data", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Mytag", "JSON : " + object);

        String url = "http://172.28.1.224:5000/train";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
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
    }
}