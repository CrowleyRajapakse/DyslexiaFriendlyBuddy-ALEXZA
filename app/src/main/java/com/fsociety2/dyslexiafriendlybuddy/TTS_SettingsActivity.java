package com.fsociety2.dyslexiafriendlybuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.nfc.Tag;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class TTS_SettingsActivity extends AppCompatActivity {

    private TextView textViewPitch;
    private TextView textViewSpeed;
    private TextView textViewVolume;
    private SeekBar seekBarPitch;
    private SeekBar seekBarSpeed;
    private float pitch;
    private float speed;
    private SeekBar seekBarVolume;
    private AudioManager audioManager;
    private ImageView ttsBackButton, ttsSaveButton;

    final static String EXTRA_PITCH_RATE = "pitch";
    final static String EXTRA_SPEED_RATE = "speed";
    final static String EXTRA_VOLUME_RATE = "volume";
    private int pitchValue;
    private int maxValue;
    private int curValue;
    private String TAG = "speed rate";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tts__settings);

        seekBarPitch = (SeekBar) findViewById(R.id.seekBar_pitch);
        seekBarSpeed = (SeekBar) findViewById(R.id.seekBar_speed);
        textViewPitch = (TextView) findViewById(R.id.pitch_textView);
        textViewSpeed = (TextView) findViewById(R.id.speed_textView);
        ttsBackButton = findViewById(R.id.tts_backButton);
        ttsSaveButton = findViewById(R.id.tts_settingSave);

        seekBarPitch.setOnSeekBarChangeListener(pitchSeekBarListener);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TTS_SettingsActivity.this.getApplicationContext());

        Log.d("DASUSLOG", "Pitch GET Shared : " + sharedPreferences.getFloat(TTS_SettingsActivity.EXTRA_PITCH_RATE, 10));
        Log.d("DASUSLOG", "Speed GET Shared: " + sharedPreferences.getFloat(TTS_SettingsActivity.EXTRA_SPEED_RATE, 10));

        final float previousPitch = (float) (sharedPreferences.getFloat(EXTRA_PITCH_RATE, 10) * 100.0);
        seekBarPitch.setProgress((int) previousPitch);

        seekBarSpeed.setOnSeekBarChangeListener(speedSeekBarListener);
        float previousSpeed = (float) (sharedPreferences.getFloat(EXTRA_SPEED_RATE, 10) *100.0);
        Log.d(TAG, "speed rate: " + previousSpeed);
        seekBarSpeed.setProgress((int) previousSpeed);

        ttsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ttsSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DASUSLOG", "Pitch : " + pitch);
                Log.d("DASUSLOG", "Speed : " + speed);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat(EXTRA_PITCH_RATE, pitch);
                editor.putFloat(EXTRA_SPEED_RATE, speed);
                editor.apply();

                Log.d("DASUSLOG", "Pitch Shared : " + sharedPreferences.getFloat(TTS_SettingsActivity.EXTRA_PITCH_RATE, 10));
                Log.d("DASUSLOG", "Speed Shared: " + sharedPreferences.getFloat(TTS_SettingsActivity.EXTRA_SPEED_RATE, 10));

            }
        });

    }

    private SeekBar.OnSeekBarChangeListener pitchSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            textViewPitch.setText(" " + progress + "%");
            updatePitch(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    private SeekBar.OnSeekBarChangeListener speedSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            textViewSpeed.setText(" " + progress + "%");
            updateSpeed(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    private void updatePitch(float value) {

        Log.d("DASUSLOG", "Pitch update : " + value);
        if (value < 0.1) value = 0.1f;
        pitch = (float) (value / 100.0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(EXTRA_PITCH_RATE, value);
        editor.apply();
    }

    private void updateSpeed(float value) {
        Log.d("DASUSLOG", "Speed update : " + value);
        if (value < 0.1) value = 0.1f;
        speed = (float) (value / 100.0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(EXTRA_SPEED_RATE, value);
        editor.apply();
    }

}
