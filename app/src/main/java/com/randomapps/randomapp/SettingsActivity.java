package com.randomapps.randomapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            BindPreferenceSettingChanges();
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

        private void UpdateSharedpreference(String key, String value){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }

        /*private void UpdateEnableShakepreference(String key, Boolean value){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }*/

        private void BindPreferenceSettingChanges() {
            Preference language_preference = findPreference("settings_language");
            language_preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if(newValue.toString().equals("lang_english")) {
                        UpdateSharedpreference("settings_language", newValue.toString());
                        SetApplocaleSettings(null);

                    } else if(newValue.toString().equals("lang_kannada")) {
                        UpdateSharedpreference("settings_language", newValue.toString());
                        SetApplocaleSettings("kn");
                    }
                    return true;
                }
            });

            /*Preference shakeEnable_preference = findPreference("enableShakePref");
            shakeEnable_preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    UpdateEnableShakepreference("enableShakePref", (Boolean)newValue);
                    return true;
                }
            });*/
        }

        //Manual restarting the app
        private void ReStartApp(){
            Intent mStartActivity = new Intent(getActivity(), MainActivity.class);
            int mPendingIntentId = 123456;
            PendingIntent mPendingIntent = PendingIntent.getActivity(getActivity(), mPendingIntentId, mStartActivity,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
            System.exit(0);
        }
    }
}