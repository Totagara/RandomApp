package com.randomapps.randomapp;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.text_about));

        //enable hyper links of test contents
        TextView aboutForUsersContent = findViewById(R.id.aboutForUsersContent);
        //aboutForUsersContent.setText(Html.fromHtml(getString(R.string.text_aboutForUsersContent)));
        aboutForUsersContent.setMovementMethod(LinkMovementMethod.getInstance());

        TextView aboutLicencingContent = findViewById(R.id.aboutLicencingContent);
        aboutLicencingContent.setMovementMethod(LinkMovementMethod.getInstance());

        TextView aboutDataPrivacyContent = findViewById(R.id.aboutDataPrivacyContent);
        aboutDataPrivacyContent.setMovementMethod(LinkMovementMethod.getInstance());

        Button button_contactUs = findViewById(R.id.button_contactUs);
        button_contactUs.setOnClickListener(v -> ContactUsHandler(v));
        Button button_rateUs = findViewById(R.id.button_rateUs);
        button_rateUs.setOnClickListener(v -> RateUsHandler(v));
        Button button_shareApp = findViewById(R.id.button_shareApp);
        button_shareApp.setOnClickListener(v -> ShareAppHandler(v));
    }

    private void ShareAppHandler(View v) {
        //take user to app store
        /*Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.android.example"));
        startActivity(intent);*/

        final String appPackageName = getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        //sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out the all in one super randomness application - RandomApp at: https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shareapp_text) + " https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private Intent getRateIntent()
    {
        String url        = isMarketAppInstalled() ? "market://details" : "https://play.google.com/store/apps/details";
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int intentFlags   = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        intentFlags      |= Build.VERSION.SDK_INT >= 21 ? Intent.FLAG_ACTIVITY_NEW_DOCUMENT : Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        rateIntent.addFlags(intentFlags);
        return rateIntent;
    }

    private boolean isMarketAppInstalled()
    {
        Intent marketIntent = new Intent();
        marketIntent.setAction(Intent.ACTION_VIEW);
        marketIntent.setData(Uri.parse("market://search?q=foo"));
        List<ResolveInfo> activitiesCanHandleIntent = getPackageManager().queryIntentActivities(marketIntent, 0);
        return activitiesCanHandleIntent.size() > 0;
    }

    private void RateUsHandler(View v) {
        startActivity(getRateIntent());
    }

    private void ContactUsHandler(View v) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","contact.randomapp@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "RandomApp - Feedback");
        //emailIntent.putExtra(Intent.EXTRA_TEXT, "Thanks for reaching out!. Please enter your query details below.");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
        //intent.putExtra(Intent.EXTRA_EMAIL, addresses); // String[] addresses

        /*Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:dave.ktr@gmail.com"));
        startActivity(Intent.createChooser(emailIntent, "Send feedback"));*/

        /*Intent intent = new Intent(Intent.ACTION_SEND);//common intent
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Your Subject Here");
        intent.putExtra(Intent.EXTRA_TEXT, "E-mail body" );
        startActivity(intent);*/
    }
}
