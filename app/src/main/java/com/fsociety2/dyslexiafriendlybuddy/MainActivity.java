package com.fsociety2.dyslexiafriendlybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private CardView cameraCard, voiceCard, dictionaryCard, chunkCard, settingsCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ContactUs.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cameraCard = (CardView) findViewById(R.id.cameracardId);
        voiceCard = (CardView) findViewById(R.id.voicecardid);
        dictionaryCard = (CardView) findViewById(R.id.dictionarycardid);
        chunkCard = (CardView) findViewById(R.id.chunkcardid);
        settingsCard = (CardView) findViewById(R.id.settingscardid);

        cameraCard.setOnClickListener(this);
        voiceCard.setOnClickListener(this);
        dictionaryCard.setOnClickListener(this);
        chunkCard.setOnClickListener(this);
        settingsCard.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, OCRActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_voice) {
            Intent intent = new Intent(this, TTS_SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_dictionary) {
            Intent intent = new Intent(this, ChunkingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out this app 'Dyslexia Friendly Buddy', soon will be available at play store");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Sharing"));

        } else if (id == R.id.nav_send) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, download this awesome app!");
            startActivity(Intent.createChooser(shareIntent, "Sending"));

        } else if (id == R.id.nav_chunk) {
            Intent intent = new Intent(this, ChunkingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_contact_us) {
            Intent intent = new Intent(this, ContactUs.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

        Intent i;

        switch (view.getId()) {
            case R.id.cameracardId:
                i = new Intent(this, OCRActivity.class);
                startActivity(i);
                break;
            case R.id.settingscardid:
                i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.voicecardid:
                i = new Intent(this, TTS_SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.chunkcardid:
                i = new Intent(this, ChunkingActivity.class);
                startActivity(i);
                break;
            case R.id.dictionarycardid:
                i = new Intent(this, DictionaryActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
