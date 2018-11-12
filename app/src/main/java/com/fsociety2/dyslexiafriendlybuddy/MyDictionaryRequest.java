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
    String myUrl;
    String definition;
    String example;
    String phoneticNotation;
    Context context;
    TextView defText;
    TextView exampleText;
    TextView phoneticText;


    MyDictionaryRequest(Context context, TextView defText, TextView exampleText, TextView phoneticText) {
        this.context = context;
        this.defText = defText;
        this.exampleText = exampleText;
        this.phoneticText = phoneticText;
    }

    @Override
    protected String doInBackground(String... voids) {
        myUrl = voids[0];
        try {
            URL url = new URL(myUrl);

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

            JSONObject lexEntries = results.getJSONObject(0);
            JSONArray lexArray = lexEntries.getJSONArray("lexicalEntries");

            JSONObject entries = lexArray.getJSONObject(0);
            JSONArray entryRows = entries.getJSONArray("entries");

            JSONObject jsonObject = entryRows.getJSONObject(0);
            JSONArray sensesArray = jsonObject.getJSONArray("senses");

            JSONObject d = sensesArray.getJSONObject(0);
            JSONArray defineWord = d.getJSONArray("definitions");

            definition = defineWord.getString(0);
            defText.setText(definition);

            JSONObject exes = sensesArray.getJSONObject(0);
            JSONArray examples = exes.getJSONArray("examples");

            JSONObject exampleValue = examples.getJSONObject(0);
            example = exampleValue.getString("text");
            exampleText.setText(example);

            JSONObject pronounciations = lexArray.getJSONObject(0);
            JSONArray pronounce = pronounciations.getJSONArray("pronunciations");


            JSONObject notations = pronounce.getJSONObject(0);
            phoneticNotation = notations.optString("phoneticSpelling");

            phoneticText.setText(phoneticNotation);
            Log.d("DICTLOG", "PhoText : " + notations);
            Log.d("DICTLOG", "PhoText : " + phoneticNotation);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
