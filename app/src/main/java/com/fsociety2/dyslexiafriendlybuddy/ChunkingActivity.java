package com.fsociety2.dyslexiafriendlybuddy;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChunkingActivity extends AppCompatActivity {

    final static String EXTRA_FONT_SIZE = "fontsize";
    final static String EXTRA_WORD_COUNT = "wordcount";
    final static String EXTRA_FONT_STYLE = "fontstyle";
    final static String TAG = "CHUNKINGLOG";
    SharedPreferences sharedPreferences;
    int newTextSize;
    TextInputEditText txtWordCount;
    int newWordCount;
    ArrayList<String> FontStylesList = new ArrayList<String>();
    String fontstyle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chunking);
        SeekBar mySeekBar = (SeekBar) findViewById(R.id.chunkseekBar);
        mySeekBar.setOnSeekBarChangeListener(customSeekBarListener);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ChunkingActivity.this.getApplicationContext());
        int previousTextSize = sharedPreferences.getInt(ChunkingActivity.EXTRA_FONT_SIZE, 10);
        int previousWordCount = sharedPreferences.getInt(ChunkingActivity.EXTRA_WORD_COUNT, 0);
        String previousFontStyle = sharedPreferences.getString(ChunkingActivity.EXTRA_FONT_STYLE, "default");
        mySeekBar.setProgress(previousTextSize);
        Spinner spinner = (Spinner) findViewById(R.id.fontspinner);

        FontStylesList.add("Sans Serif");
        FontStylesList.add("Serif");
        FontStylesList.add("Monospace");
        FontStylesList.add("Default font");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChunkingActivity.this
                , android.R.layout.simple_spinner_item, FontStylesList);

        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                fontstyle = FontStylesList.get(i);

                Toast.makeText(ChunkingActivity.this, "You Selected : "

                        + FontStylesList.get(i) + " Level ", Toast.LENGTH_SHORT).show();

            }

            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });


        txtWordCount = findViewById(R.id.wordcountinput);
        txtWordCount.setText(String.valueOf(previousWordCount));

        // TextView spinner_text=(TextView)findViewById(R.id.text1);
        Button btnSave = findViewById(R.id.savebutton);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newWordCount = Integer.parseInt(txtWordCount.getText().toString());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(EXTRA_FONT_SIZE, newTextSize);
                editor.putInt(EXTRA_WORD_COUNT, newWordCount);
                editor.putString(EXTRA_FONT_STYLE, fontstyle);
                editor.apply();
            }
        });

        // spinner_text.setTypeface(externalFont);


    }

    private SeekBar.OnSeekBarChangeListener customSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            updateTextSize(progress);
            Log.d(TAG, "Text size : " + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private void updateTextSize(int sizeIncrease) {
        newTextSize = sizeIncrease;

    }
//from here i commented
/*

    public void Picker1Click(View arg0) {
        //no direct way to get background color as it could be a drawable
        if (ShowColor.getBackground() instanceof ColorDrawable) {
            ColorDrawable cd = (ColorDrawable) ShowColor.getBackground();
            int colorCode = cd.getColor();
            //pick a color (changed in the UpdateColor listener)
            new ColorPickerDialog(MainActivity.this, new UpdateColor(), colorCode).show();
        }
    }*/
/*
    public class UpdateColor implements ColorPickerDialog.OnColorChangedListener {
        public void colorChanged(int color) {
            ShowColor.setBackgroundColor(color);
            //show the color value
            ShowColor.setText("0x"+String.format("%08x", color));
            SeekA.setProgress(Color.alpha(color));
            SeekR.setProgress(Color.red(color));
            SeekG.setProgress(Color.green(color));
            SeekB.setProgress(Color.blue(color));
        }
    }*/
}
