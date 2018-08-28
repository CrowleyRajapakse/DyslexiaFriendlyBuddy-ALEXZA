package com.fsociety2.dyslexiafriendlybuddy;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MyDictionaryRequest extends AsyncTask<String, Integer, String> {

    final String app_id = "52b34dcb";
    final String app_key = "e5f510fa088c57d3cad7f1af28abe345";
    String myurl;
    Context context;

    TextView textView3;
 // TextView textView6;

    private String phon;

    MyDictionaryRequest(Context context, TextView textView3) {
        this.context = context;
        this.textView3 = textView3;
        //this.textView6 = textView6;

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
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
       // textView6.setText(s);
        String def;
        String phon;
        try {
            JSONObject js = new JSONObject(s);
            JSONArray results = js.getJSONArray("results");

            JSONObject lEntries = results.getJSONObject(0);
            JSONArray laArray = lEntries.getJSONArray("lexicalEntries");

            JSONObject entries = laArray.getJSONObject(0);
            JSONArray e = entries.getJSONArray("entries");

            JSONObject jsonObject = e.getJSONObject(0);
            JSONArray sensesArray = jsonObject.getJSONArray("senses");

            JSONObject d = sensesArray.getJSONObject(0);
            JSONArray de = d.getJSONArray("definitions");

           // JSONObject jsonObject2 = e.getJSONObject(0);
           // JSONArray pronounceArray = jsonObject2.getJSONArray("pronunciations");

          //  JSONObject p = pronounceArray.getJSONObject(0);




         //   JSONObject pEntries = results.getJSONObject(0);
         //   JSONArray pArray = pEntries.getJSONArray("pronunciations");
         //   JSONObject p = pArray.getJSONObject(0);
         //   JSONArray phonNot = p.getJSONArray("phoneticNotation");
         //   JSONArray phonSpell = p.getJSONArray("phoneticSpelling");

           // JSONObject pnEntries = laArray.getJSONObject(0);
           // JSONArray phonic = pnEntries.getJSONArray("phoneticNotation");

            def = de.getString(0);
          //  phon = phonNot.getString(0);

            textView3.setText(def);
           // textView6.setText(phon);



        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}
