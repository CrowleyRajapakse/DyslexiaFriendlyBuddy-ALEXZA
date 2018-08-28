package com.fsociety2.dyslexiafriendlybuddy;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DictionaryActivity extends AppCompatActivity {
    String url;
    String def;
    String phon;
    EditText editText;
    TextView textView3;
    TextView textView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        editText = findViewById(R.id.editText);
        textView3 = findViewById(R.id.textView3);
        // textView6 = findViewById(R.id.textView6);


    }


    public void requestApiButtonClick(View v) {
        MyDictionaryRequest myDictionaryRequest = new MyDictionaryRequest(this, textView3);


        url = dictionaryEntries();
        myDictionaryRequest.execute(url);
        textView3.setText(def);
        // textView6.setText(phon);

    }

    private String dictionaryEntries() {
        final String language = "en";
        final String word = editText.getText().toString();
        final String word_id = word.toLowerCase(); //word id is case sensitive and lowercase is required
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
    }
}
