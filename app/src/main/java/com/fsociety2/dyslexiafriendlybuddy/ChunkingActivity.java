package com.fsociety2.dyslexiafriendlybuddy;

import android.content.SharedPreferences;

import android.graphics.Typeface;
import android.media.Image;
import android.preference.PreferenceManager;
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
    int newfontcolor;
    int newbackcolor;


    EditText txtWordCount;

    TextView textPreview;

    String wordCase;
    String newfontstyle;
    String caseText;

    List<String> FontStylesList = new ArrayList<String>();

    ColorPicker colorPickerB;
    ColorPicker colorPickerF;

    RadioGroup radioCaseGroup;
    RadioButton radioCaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_chunking);

        SeekBar mySeekBar = (SeekBar) findViewById(R.id.chunkseekBar);
        mySeekBar.setOnSeekBarChangeListener(customSeekBarListener);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ChunkingActivity.this.getApplicationContext());
        int previousTextSize = sharedPreferences.getInt(ChunkingActivity.EXTRA_FONT_SIZE, 10);
        int previousWordCount = sharedPreferences.getInt(ChunkingActivity.EXTRA_WORD_COUNT, 0);
        String previousFontStyle = sharedPreferences.getString(ChunkingActivity.EXTRA_FONT_STYLE, "Aller");
        final int previousFontColor = sharedPreferences.getInt(ChunkingActivity.EXTRA_FONT_COLOR, 0xFF0000);
        final int previousBackColor = sharedPreferences.getInt(ChunkingActivity.EXTRA_BACK_COLOR, 0xFFFF00);

        mySeekBar.setProgress(previousTextSize);
        Spinner fontspinner = findViewById(R.id.fontspinner);
        Button changebackground = findViewById(R.id.colorbutton);
        Button changefontcolor = findViewById(R.id.fontcolorbutton);
        ImageView backbutton = findViewById(R.id.backbutton);

        txtWordCount = findViewById(R.id.wordcountinput);
        textPreview = findViewById(R.id.textPreview);
        txtWordCount.setText(String.valueOf(previousWordCount));

        textPreview.setBackgroundColor(previousBackColor);
        textPreview.setTextColor(previousFontColor);
        textPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, previousTextSize);
        textPreview.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/" + previousFontStyle + ".ttf"));

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        changebackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorPickerB = new ColorPicker(ChunkingActivity.this);
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


                colorPickerB
                        .setDefaultColorButton(previousBackColor)
                        .setColors(colors)
                        .setColumns(5)
                        .setRoundColorButton(true)
                        .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {

                            @Override
                            public void onChooseColor(int position, int color) {
                                colorPickerB.setDefaultColorButton(previousBackColor);
                                newbackcolor = color;
                                updateBackgroundColor(newbackcolor);
                                textPreview.setBackgroundColor(newbackcolor);


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

                colorPickerF
                        .setDefaultColorButton(previousFontColor)
                        .setColors(colors)
                        .setColumns(5)
                        .setRoundColorButton(true)
                        .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {

                            @Override
                            public void onChooseColor(int position, int color) {
                                colorPickerF.setDefaultColorButton(previousFontColor);
                                newfontcolor = color;
                                updateFontColor(newfontcolor);
                                textPreview.setTextColor(newfontcolor);


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

//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChunkingActivity.this
//                , android.R.layout.simple_list_item_1, FontStylesList);

        CustomSpinnerAdapter arrayAdapter = new CustomSpinnerAdapter(ChunkingActivity.this, android.R.layout.simple_list_item_1, FontStylesList);
        fontspinner.setAdapter(arrayAdapter);
        int spinnerPosition1 = arrayAdapter.getPosition(previousFontStyle);
        fontspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                newfontstyle = FontStylesList.get(i);
                updateFontStyle(newfontstyle);
                textPreview.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/" + newfontstyle + ".ttf"));

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });


        fontspinner.setSelection(spinnerPosition1);
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
            textPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, seekBar.getProgress());

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private void updateTextSize(int sizeIncrease) {
        newTextSize = sizeIncrease;
//

    }

    private void updateFontStyle(String newfstyle) {
        newfontstyle = newfstyle;

    }


    private void updateFontColor(int newfcolor) {
        newfontcolor = newfcolor;
    }

    private void updateBackgroundColor(int newbcolor) {
        newbackcolor = newbcolor;

    }


}
