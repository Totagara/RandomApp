package com.randomapps.randomapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.randomapps.randomapp.utils.AppStrings;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

import DB.DBHelper;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    DBHelper randomDatabase;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        randomDatabase = new DBHelper(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_flipCoin, R.id.nav_dice, R.id.nav_cardPicker,
                R.id.nav_randomNumbers, R.id.nav_randomColors, R.id.nav_randomDatesTimes,
                R.id.nav_randomPasswords, R.id.nav_uuids, R.id.nav_seriesGenerator, R.id.nav_divideAndAssign,
                R.id.nav_yesNo, R.id.nav_rockPaperScissors, R.id.nav_savedLists)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Check the preferences before loading the page to update locale
        UpdateLocale();
    }

    private void SetApplocaleSettings(String lang_code){
        Locale locale;
        if(lang_code != null){
            locale = new Locale(lang_code.toLowerCase());
        } else {
            locale = Locale.getDefault();
        }

        Resources res = getResources();
        DisplayMetrics disMetrics = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(locale);
        res.updateConfiguration(conf, disMetrics);
    }

    private void UpdateLocale() {
        String language = sharedPreferences.getString(AppStrings.SETTINGS_LANGUAGE_KEY, "lang_english");
        if(language.equals("lang_english")){
            SetApplocaleSettings(null);
        } else {
            SetApplocaleSettings("kn");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_aboutus){
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
        } else if(item.getItemId() == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        } /*else {
            Intent testIntent = new Intent(this, TestActivity.class);
            startActivity(testIntent);
        }*/
        return super.onOptionsItemSelected(item);
    }
}
