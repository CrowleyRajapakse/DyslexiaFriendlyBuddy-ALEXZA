package com.fsociety2.dyslexiafriendlybuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.nfc.Tag;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class TTS_SettingsActivity extends AppCompatActivity {

    private TextView textViewPitch;
    private TextView textViewSpeed;
    private TextView textViewVolume;
    private SeekBar seekBarPitch;
    private SeekBar seekBarSpeed;
    private SeekBar seekBarVolume;
    private AudioManager audioManager;

    final static String EXTRA_PITCH_RATE = "pitch";
    final static String EXTRA_SPEED_RATE = "speed";
    final static String EXTRA_VOLUME_RATE = "volume";
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
        seekBarVolume = (SeekBar) findViewById(R.id.seekBar_volume);
        textViewPitch = (TextView) findViewById(R.id.pitch_textView);
        textViewSpeed = (TextView) findViewById(R.id.speed_textView);
        textViewVolume = (TextView) findViewById(R.id.volume_textView);

        seekBarPitch.setOnSeekBarChangeListener(pitchSeekBarListener);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(TTS_SettingsActivity.this.getApplicationContext());
        final float previousPitch = sharedPreferences.getFloat(TTS_SettingsActivity.EXTRA_PITCH_RATE, 10);
        seekBarPitch.setProgress((int) previousPitch);

        seekBarSpeed.setOnSeekBarChangeListener(speedSeekBarListener);
        float previousSpeed = sharedPreferences.getFloat(TTS_SettingsActivity.EXTRA_SPEED_RATE, 10);
        Log.d(TAG, "speed rate: " + previousSpeed);
        seekBarSpeed.setProgress((int) previousSpeed);

//        seekBarVolume.setOnSeekBarChangeListener(volumeSeekBarListener);
//        int previousVolume = sharedPreferences.getInt(TTS_SettingsActivity.EXTRA_VOLUME_RATE, 10);
//        seekBarVolume.setProgress(previousVolume);


        //setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // initControls();

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

//    private SeekBar.OnSeekBarChangeListener volumeSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            textViewVolume.setText(" " + progress + "%");
//            initControls();
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar) {
//
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar) {
//
//        }
//    };

    private void updatePitch(float value) {
        float pitch = value / 50;
        if (pitch < 0.1) pitch = 0.1f;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(EXTRA_PITCH_RATE, pitch);
        editor.apply();
    }

    private void updateSpeed(float value) {
        float speed = value / 50;
        if (speed < 0.1) speed = 0.1f;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(EXTRA_SPEED_RATE, speed);
        editor.apply();
    }

//    private void initControls() {
//        try {
//            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//            maxValue = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//            curValue = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//            seekBarVolume.setMax(maxValue);
//            seekBarVolume.setProgress(curValue);
//
//        } catch (Exception e) {
//
//        }
//    }
}
