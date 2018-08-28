package com.fsociety2.dyslexiafriendlybuddy;

import android.content.SharedPreferences;

import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.SeekBar;
import android.widget.Spinner;


import java.util.ArrayList;

import com.fsociety2.dyslexiafriendlybuddy.ColorPicker;

import java.util.ArrayList;

public class ChunkingActivity extends AppCompatActivity {

    final static String EXTRA_FONT_SIZE = "fontsize";
    final static String EXTRA_WORD_COUNT = "wordcount";
    final static String EXTRA_FONT_STYLE = "fontstyle";
    final static String EXTRA_FONT_COLOR = "fontcolor";
    final static String EXTRA_BACK_COLOR = "backcolor";
    final static String TAG = "CHUNKINGLOG";
    SharedPreferences sharedPreferences;
    int newTextSize;
    EditText txtWordCount;

    int newWordCount;

    ArrayList<String> FontStylesList = new ArrayList<String>();
    String newfontstyle;
    int newfontcolor;
    int newbackcolor;
    ColorPicker colorPickerB;
    ColorPicker colorPickerF;

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
        final int previousFontColor = sharedPreferences.getInt(ChunkingActivity.EXTRA_FONT_COLOR, 0xFF0000);
        final int previousBackColor = sharedPreferences.getInt(ChunkingActivity.EXTRA_BACK_COLOR, 0xFFFF00);

        mySeekBar.setProgress(previousTextSize);
        Spinner spinner1 = findViewById(R.id.fontspinner);
        Button changebackground = findViewById(R.id.colorbutton);
        Button changefontcolor = findViewById(R.id.fontcolorbutton);

        Log.d("position", "" + previousBackColor);
        txtWordCount = findViewById(R.id.wordcountinput);
        txtWordCount.setText(String.valueOf(previousWordCount));

        changebackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorPickerB = new ColorPicker(ChunkingActivity.this);
                ArrayList<String> colors = new ArrayList<>();
                colors.add("#B0FAF0");
                colors.add("#FAB0ED");
                colors.add("#FABC76");
                colors.add("#FFFF67");
                colors.add("#BFFF67");
                colors.add("#BBCFF8");
                colors.add("#EDBB99");
                colors.add("#CCD1D1");
                colors.add("#CAABC6");
                colors.add("#F98383");
                colors.add("#F4D03F");
                colors.add("#F0F3F4");
                colors.add("#FFA07A");
                colors.add("#D4ACAA");
                colors.add("#91F5A5");

                colorPickerB
                        .setDefaultColorButton(previousBackColor)
                        .setColors(colors)
                        .setColumns(5)
                        .setRoundColorButton(true)
                        .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {

                            @Override
                            public void onChooseColor(int position, int color) {
                                //colorPicker.setDefaultColorButton(previousBackColor);
                                newbackcolor = color;
                                updateBackgroundColor(newbackcolor);

                                // Log.d("position", "" + color);// will be fired only when OK button was tapped
                            }

                            @Override
                            public void onCancel() {
                                colorPickerB.setDefaultColorButton(previousBackColor);
                                updateBackgroundColor(previousBackColor);
                            }

                        })
                        .show();
            }
        });


        changefontcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorPickerF = new ColorPicker(ChunkingActivity.this);
                ArrayList<String> colors = new ArrayList<>();
                colors.add("#00EDE9");
                colors.add("#5B2C6F");
                colors.add("#922B21");
                colors.add("#2471A3");
                colors.add("#196F3D");
                colors.add("#F39C12");
                colors.add("#E34E0D");
                colors.add("#031970");
                colors.add("#8A026B");
                colors.add("#EA6604");
                colors.add("#08929B");
                colors.add("#9F9F03");
                colors.add("#784212");
                colors.add("#048406");
                colors.add("#000000");

                colorPickerF
                        .setDefaultColorButton(previousFontColor)
                        .setColors(colors)
                        .setColumns(5)
                        .setRoundColorButton(true)
                        .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {

                            @Override
                            public void onChooseColor(int position, int color) {

                                newfontcolor = color;
                                updateFontColor(newfontcolor);


                            }

                            @Override
                            public void onCancel() {
                                colorPickerF.setDefaultColorButton(previousFontColor);
                                updateFontColor(previousFontColor);
                            }

                        })
                        .show();
            }
        });


        FontStylesList.add("Aller");
        FontStylesList.add("Arial");
        FontStylesList.add("Arimo");
        FontStylesList.add("Caviar Dreams");
        FontStylesList.add("Lato");
        FontStylesList.add("Monospace");
        FontStylesList.add("Ostrich");
        FontStylesList.add("Oswald");
        FontStylesList.add("Playfair");
        FontStylesList.add("Roboto");
        FontStylesList.add("Sans Serif");
        FontStylesList.add("Serif");
        FontStylesList.add("Titillium");
        FontStylesList.add("Walkway");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChunkingActivity.this
                , android.R.layout.simple_spinner_dropdown_item, FontStylesList);

        spinner1.setAdapter(arrayAdapter);
        int spinnerPosition1 = arrayAdapter.getPosition(previousFontStyle);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                newfontstyle = FontStylesList.get(i);
                updateFontStyle(newfontstyle);

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });


        // numpick.setValue(previousWordCount);
        //   txtWordCount.setText(String.valueOf(previousWordCount)); // asign selected value of picker
        spinner1.setSelection(spinnerPosition1);
        updateBackgroundColor(previousBackColor);
        updateFontColor(previousFontColor);


        ImageView btnSave = findViewById(R.id.savebutton);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newWordCount = Integer.parseInt(txtWordCount.getText().toString());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(EXTRA_FONT_SIZE, newTextSize);
                editor.putInt(EXTRA_WORD_COUNT, newWordCount);
                editor.putString(EXTRA_FONT_STYLE, newfontstyle);
                editor.putInt(EXTRA_BACK_COLOR, newbackcolor);
                editor.putInt(EXTRA_FONT_COLOR, newfontcolor);
                editor.apply();
            }
        });


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

    private void updateFontStyle(String newfstyle) {
        newfontstyle = newfstyle;

    }


private void updateFontColor(int newfcolor)
{
    newfontcolor=newfcolor;
}

    private void updateBackgroundColor(int newbcolor) {
        newbackcolor = newbcolor;

    }


}
