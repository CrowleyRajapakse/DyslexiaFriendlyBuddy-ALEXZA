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
import android.widget.ImageView;

import android.widget.SeekBar;
import android.widget.Spinner;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

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
    int txtcount;
    int newWordCount;
    ArrayList<String> FontStylesList = new ArrayList<String>();
    ArrayList<String> FontColorsList = new ArrayList<String>();
    ArrayList<String> BackColorsList = new ArrayList<String>();
    String newfontstyle;
    String newfontcolor;
    String newbackcolor;



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
        String previousFontColor = sharedPreferences.getString(ChunkingActivity.EXTRA_FONT_COLOR, "Green");
        String previousBackColor = sharedPreferences.getString(ChunkingActivity.EXTRA_BACK_COLOR, "Grey");
        mySeekBar.setProgress(previousTextSize);
        Spinner spinner1 = (Spinner) findViewById(R.id.fontspinner);
        Spinner spinner2 = (Spinner) findViewById(R.id.fontcolorspinner);
        Spinner spinner3 = (Spinner) findViewById(R.id.backcolorspinner);
        Button changebackground = (Button) findViewById(R.id.colorbutton) ;

        txtWordCount = findViewById(R.id.wordcountinput);
        txtWordCount.setText(String.valueOf(previousWordCount));

        changebackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ColorPicker colorPicker = new ColorPicker(ChunkingActivity.this);
                ArrayList<String> colors = new ArrayList<>();
                colors.add("#82B926");
                colors.add("#a276eb");
                colors.add("#6a3ab2");
                colors.add("#666666");
                colors.add("#FFFF00");
                colors.add("#3C8D2F");
                colors.add("#FA9F00");
                colors.add("#FF0000");
                colors.add("#EF5400");
                colors.add("#3C4D26");

                colorPicker
                        .setDefaultColorButton(Color.parseColor("#f84c44"))
                        .setColors(colors)
                        .setColumns(5)
                        .setRoundColorButton(true)
                        .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                            @Override
                            public void onChooseColor(int position, int color) {
                                Log.d("position", "" + position);// will be fired only when OK button was tapped
                            }

                            @Override
                            public void onCancel() {

                            }
                        })
                        .addListenerButton("newButton", new ColorPicker.OnButtonListener() {
                            @Override
                            public void onClick(View v, int position, int color) {
                                Log.d("position", "" + position);
                            }
                        }).show();
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

        FontColorsList.add("Blue");
        FontColorsList.add("Green");
        FontColorsList.add("Red");
        FontColorsList.add("Black");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(ChunkingActivity.this
                , android.R.layout.simple_spinner_item, FontColorsList);

        spinner2.setAdapter(arrayAdapter2);
        int spinnerPosition2 = arrayAdapter2.getPosition(previousFontColor);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                newfontcolor = FontColorsList.get(i);
                updateFontColor(newfontcolor);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });
        BackColorsList.add("Yellow");
        BackColorsList.add("Light Blue");
        BackColorsList.add("Salmon Pink");
        BackColorsList.add("Grey");
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(ChunkingActivity.this
                , android.R.layout.simple_spinner_item, BackColorsList);

        spinner3.setAdapter(arrayAdapter3);
        int spinnerPosition3 = arrayAdapter3.getPosition(previousBackColor);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                newbackcolor = BackColorsList.get(i);
                updateBackgroundColor(newbackcolor);

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });



       // numpick.setValue(previousWordCount);
     //   txtWordCount.setText(String.valueOf(previousWordCount)); // asign selected value of picker
        spinner1.setSelection(spinnerPosition1);
        spinner2.setSelection(spinnerPosition2);
        spinner3.setSelection(spinnerPosition3);


        ImageView btnSave = findViewById(R.id.savebutton);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newWordCount = Integer.parseInt(txtWordCount.getText().toString());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(EXTRA_FONT_SIZE, newTextSize);
                editor.putInt(EXTRA_WORD_COUNT, newWordCount);
                editor.putString(EXTRA_FONT_STYLE, newfontstyle);
                editor.putString(EXTRA_FONT_COLOR, newfontcolor);
                editor.putString(EXTRA_BACK_COLOR, newbackcolor);
                editor.apply();
            }
        });


    }

   // NumberPicker.OnValueChangeListener onValueChangeListener =
           // new NumberPicker.OnValueChangeListener() {
             //   @Override
              //  public void onValueChange(NumberPicker numberPicker, int i, int i1) {
              //      txtcount = numberPicker.getValue();
               // }
          //  };

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

    private void updateFontColor(String newfcolor) {
        newfontcolor = newfcolor;

    }

    private void updateBackgroundColor(String newbcolor) {
        newbackcolor = newbcolor;

    }


}
