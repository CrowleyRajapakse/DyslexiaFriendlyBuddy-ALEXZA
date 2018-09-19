package com.fsociety2.dyslexiafriendlybuddy;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import net.reduls.sanmoku.dic.Char;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MyDictionaryRequest extends AsyncTask<String, Integer, String> {

    final String app_id = "52b34dcb";
    final String app_key = "e5f510fa088c57d3cad7f1af28abe345";
    String myurl;
    String inflection;
    String def;

    Context context;
    TextView textView3;


    private String phon;

    MyDictionaryRequest(Context context, TextView textView3,String inflection) {
        this.context = context;
        this.textView3 = textView3;
        this.inflection=inflection;

    }


    @Override
    protected String doInBackground(String... voids) {
        myurl = voids[0];
        try {
            URL url = new URL(myurl);

            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("app_id", app_id);
            urlConnection.setRequestProperty("app_key", app_key);

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            return stringBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);


        try {
            JSONObject js = new JSONObject(result);
            JSONArray results = js.getJSONArray("results");

            JSONObject lEntries = results.getJSONObject(0);
            JSONArray laArray = lEntries.getJSONArray("lexicalEntries");

            JSONObject entries = laArray.getJSONObject(0);
            JSONArray e = entries.getJSONArray("entries");

            JSONObject jsonObject = e.getJSONObject(0);
            JSONArray sensesArray = jsonObject.getJSONArray("senses");

            JSONObject d = sensesArray.getJSONObject(0);
            JSONArray de = d.getJSONArray("definitions");
            def = de.getString(0);
            //  phon = phonNot.getString(0);

            textView3.setText(def);
            // textView6.setText(phon);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
