package com.randomapps.randomapp.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.randomapps.randomapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Utils {

    public static void hideKeyboard(View view, Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch(Exception ignored) {
        }
    }

    public static List<String> getItemsListFromText(String itemsText){
        List<String> items = new ArrayList<>();
        if(!itemsText.isEmpty() && itemsText != null) {
            String[] itemsArray = itemsText.split(",");
            items = Arrays.asList(itemsArray);
        }
        return items;
    }

    public static void CopyContentToClipBoard(Context context, String copyLabel, String content, String toastMessage) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(copyLabel, content);
        if (clipboard == null) return;
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context,toastMessage, Toast.LENGTH_SHORT).show();
    }

    public static void ShareTheResultContent(String content, FragmentActivity activity) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        activity.startActivity(shareIntent);
    }

    /*private void SetApplocaleSettings(String lang_code, FragmentActivity activity){
        Locale locale;
        if(lang_code != null){
            locale = new Locale(lang_code.toLowerCase());
        } else {
            locale = Locale.getDefault();
        }


        Resources res = activity.getResources();
        DisplayMetrics disMetrics = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(locale);
        res.updateConfiguration(conf, disMetrics);
    }*/

    public static InputFilter AttachInputFilter(EditText editText, Integer minBound, Integer maxBound, String errorMsg, Context context){
        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String displayMsg;
                //String inputString = dest.toString() + source.toString();
                String inputString = dest.subSequence(0,dstart)+source.toString()+dest.subSequence(dstart,dest.length());
                try {
                    double val = Double.parseDouble(inputString);
                    Integer valInt = (int) val;
                    //Double val = Double.valueOf(dest.toString() + source.toString());
                    if (val < minBound || val > maxBound) {
                        displayMsg = (errorMsg == null)? context.getString(R.string.values_range_error_msg, minBound, maxBound) : errorMsg;
                        editText.setError(displayMsg);
                        return "";
                    } else {
                        editText.setError(null);
                        return null;
                    }
                } catch (Exception e){
                    if(!inputString.matches(".*\\..*\\..*")) { //if strng contains single point its valid
                        return  null;
                    } else if(inputString.matches(".*\\..*\\..*")) { //if it contains more than one dot then invalid
                        return "";
                    } else {  // any other exceptions are invalid so throw error
                        displayMsg = (errorMsg == null) ? "Value should be within the range of " + minBound + " to " + maxBound : errorMsg;
                        editText.setError(displayMsg);
                        return "";
                    }
                }
            }
        };
        return inputFilter;
    }

    public static InputFilter AttachInputFilterForDoubles(EditText editText, Integer minBound, Integer maxBound, String errorMsg){
        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String displayMsg;
                //String inputString = dest.toString() + source.toString();
                String inputString = dest.subSequence(0,dstart)+source.toString()+dest.subSequence(dstart,dest.length());
                try {
                    double val = Double.parseDouble(inputString);
                    Integer valInt = (int) val;
                    //Double val = Double.valueOf(dest.toString() + source.toString());
                    if (val < minBound || val > maxBound) {
                        //if (val < Double.valueOf(minBound) || val > Double.valueOf(maxBound)) {
                        //if (val < Double.valueOf(minBound) || val > Double.valueOf(maxBound)) {
                        displayMsg = (errorMsg == null)? "Value should be within the range of " + minBound + " to " + maxBound : errorMsg;
                        editText.setError(displayMsg);
                        return "";
                    } else {
                        editText.setError(null);
                        return null;
                    }
                } catch (Exception e){
                    if(!inputString.matches(".*\\..*\\..*")) { //if strng contains single point its valid
                        return  null;
                    } else if(inputString.matches(".*\\..*\\..*")) { //if it contains more than one dot then invalid
                        return "";
                    } else {  // any other exceptions are invalid so throw error
                        displayMsg = (errorMsg == null) ? "Value should be within the range of " + minBound + " to " + maxBound : errorMsg;
                        editText.setError(displayMsg);
                        return "";
                    }
                }
            }
        };

        return inputFilter;
    }

}
