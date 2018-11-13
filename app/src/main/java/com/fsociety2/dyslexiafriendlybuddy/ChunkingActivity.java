package com.fsociety2.dyslexiafriendlybuddy;

import android.content.SharedPreferences;

import android.graphics.Typeface;
import android.media.Image;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;

import com.fsociety2.dyslexiafriendlybuddy.ColorPicker;

import java.util.ArrayList;
import java.util.List;

public class ChunkingActivity extends AppCompatActivity {

    final static String EXTRA_FONT_SIZE = "fontsize";
    final static String EXTRA_WORD_COUNT = "wordcount";
    final static String EXTRA_FONT_STYLE = "fontstyle";
    final static String EXTRA_FONT_COLOR = "fontcolor";
    final static String EXTRA_BACK_COLOR = "backcolor";
    final static String TAG = "CHUNKINGLOG";

    SharedPreferences sharedPreferences;
    int newTextSize;
    int newWordCount;
    int newFontColor;
    int newBackColor;

    EditText textWordCount;

    TextView textPreview;

    String newFontStyle;

    List<String> FontStylesList = new ArrayList<String>();

    ColorPicker colorPickerBack;
    ColorPicker colorPickerFont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_chunking);

        SeekBar mySeekBar = findViewById(R.id.chunkSeekBar);
        mySeekBar.setOnSeekBarChangeListener(customSeekBarListener);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ChunkingActivity.this.getApplicationContext());
        int previousTextSize = sharedPreferences.getInt(ChunkingActivity.EXTRA_FONT_SIZE, 10);
        int previousWordCount = sharedPreferences.getInt(ChunkingActivity.EXTRA_WORD_COUNT, 0);
        String previousFontStyle = sharedPreferences.getString(ChunkingActivity.EXTRA_FONT_STYLE, "Aller");
        final int previousFontColor = sharedPreferences.getInt(ChunkingActivity.EXTRA_FONT_COLOR, 0xFF0000);
        final int previousBackColor = sharedPreferences.getInt(ChunkingActivity.EXTRA_BACK_COLOR, 0xFFFF00);


        mySeekBar.setProgress(previousTextSize);
        Spinner fontSpinner = findViewById(R.id.fontSpinner);
        Button changeBackground = findViewById(R.id.backColorButton);
        Button changeFontColor = findViewById(R.id.fontColorButton);
        ImageView backButton = findViewById(R.id.backButton);


        textWordCount = findViewById(R.id.wordCountInput);
        textPreview = findViewById(R.id.textPreview);
        textWordCount.setText(String.valueOf(previousWordCount));

        textPreview.setBackgroundColor(previousBackColor);
        textPreview.setTextColor(previousFontColor);
        textPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, previousTextSize);
        textPreview.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/" + previousFontStyle + ".ttf"));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        changeBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorPickerBack = new ColorPicker(ChunkingActivity.this);
                ArrayList<String> colors = new ArrayList<>();
                colors.add("#F5A9A9");
                colors.add("#F78181");
                colors.add("#F7D358");
                colors.add("#FAAC58");
                colors.add("#F7D358");

                colors.add("#F7819F");
                colors.add("#F5A9D0");
                colors.add("#D0A9F5");
                colors.add("#DA81F5");
                colors.add("#F781F3");

                colors.add("#81F7BE");
                colors.add("#01DFA5");
                colors.add("#58D3F7");
                colors.add("#81BEF7");
                colors.add("#5882FA");

                colors.add("#F7FE2E");
                colors.add("#BFFF67");
                colors.add("#81F781");
                colors.add("#2EFE64");
                colors.add("#01DF3A");


                colors.add("#F3E2A9");
                colors.add("#EDBB99");
                colors.add("#CCD1D1");
                colors.add("#F0F3F4");
                colors.add("#FFFFFF");


                colorPickerBack
                        .setDefaultColorButton(previousBackColor)
                        .setColors(colors)
                        .setColumns(5)
                        .setRoundColorButton(true)
                        .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {

                            @Override
                            public void onChooseColor(int position, int color) {
                                colorPickerBack.setDefaultColorButton(previousBackColor);
                                newBackColor = color;
                                updateBackgroundColor(newBackColor);
                                textPreview.setBackgroundColor(newBackColor);
                            }

                            @Override
                            public void onCancel() {
                                colorPickerBack.setDefaultColorButton(previousBackColor);
                                updateBackgroundColor(previousBackColor);
                            }

                        })
                        .show();
            }
        });


        changeFontColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorPickerFont = new ColorPicker(ChunkingActivity.this);
                ArrayList<String> colors = new ArrayList<>();
                colors.add("#F39C12");
                colors.add("#EA6604");
                colors.add("#E34E0D");
                colors.add("#DF0101");
                colors.add("#8A0808");

                colors.add("#FF0080");
                colors.add("#FF0040");
                colors.add("#B40486");
                colors.add("#8904B1");
                colors.add("#5B2C6F");


                colors.add("#00EDE9");
                colors.add("#04B4AE");
                colors.add("#0040FF");
                colors.add("#08088A");
                colors.add("#0B0B3B");


                colors.add("#D7DF01");
                colors.add("#86B404");
                colors.add("#01DF01");
                colors.add("#088A08");
                colors.add("#0B3B0B");

                colors.add("#8A2908");
                colors.add("#3B0B0B");
                colors.add("#6E6E6E");
                colors.add("#424242");
                colors.add("#000000");

                colorPickerFont
                        .setDefaultColorButton(previousFontColor)
                        .setColors(colors)
                        .setColumns(5)
                        .setRoundColorButton(true)
                        .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {

                            @Override
                            public void onChooseColor(int position, int color) {
                                colorPickerFont.setDefaultColorButton(previousFontColor);
                                newFontColor = color;
                                updateFontColor(newFontColor);
                                textPreview.setTextColor(newFontColor);


                            }

                            @Override
                            public void onCancel() {
                                colorPickerFont.setDefaultColorButton(previousFontColor);
                                updateFontColor(previousFontColor);
                            }

                        })
                        .show();
            }
        });


        FontStylesList.add("Aller");
        FontStylesList.add("Arimo");
        FontStylesList.add("BEBAS");
        FontStylesList.add("Cartoonist");
        FontStylesList.add("Caviar");

        FontStylesList.add("Helsinki");
        FontStylesList.add("Lato");
        FontStylesList.add("Molengo");
        FontStylesList.add("Nobile");
        FontStylesList.add("Oswald");

        FontStylesList.add("Overlock");
        FontStylesList.add("Roboto");
        FontStylesList.add("Paprika");
        FontStylesList.add("Resagnicto");
        FontStylesList.add("Rubik");

        FontStylesList.add("Sansumi");
        FontStylesList.add("Walkway");


        CustomSpinnerAdapter arrayAdapter = new CustomSpinnerAdapter(ChunkingActivity.this, android.R.layout.simple_list_item_1, FontStylesList);
        fontSpinner.setAdapter(arrayAdapter);
        int spinnerPosition1 = arrayAdapter.getPosition(previousFontStyle);
        fontSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                newFontStyle = FontStylesList.get(i);
                updateFontStyle(newFontStyle);
                textPreview.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/" + newFontStyle + ".ttf"));

            }

            public void onNothingSelected(AdapterView<?> arg0) {


            }

        });


        fontSpinner.setSelection(spinnerPosition1);
        updateBackgroundColor(previousBackColor);
        updateFontColor(previousFontColor);


        ImageView btnSave = findViewById(R.id.saveButton);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newWordCount = Integer.parseInt(textWordCount.getText().toString());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(EXTRA_FONT_SIZE, newTextSize);
                editor.putInt(EXTRA_WORD_COUNT, newWordCount);
                editor.putString(EXTRA_FONT_STYLE, newFontStyle);
                editor.putInt(EXTRA_BACK_COLOR, newBackColor);
                editor.putInt(EXTRA_FONT_COLOR, newFontColor);
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
            textPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, seekBar.getProgress());

        }
    };

    private void updateTextSize(int sizeIncrease) {
        newTextSize = sizeIncrease;
//

    }

    private void updateFontStyle(String newfstyle) {
        newFontStyle = newfstyle;

    }


    private void updateFontColor(int newfcolor) {
        newFontColor = newfcolor;
    }

    private void updateBackgroundColor(int newbcolor) {
        newBackColor = newbcolor;

    }


}
