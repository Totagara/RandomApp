package com.randomapps.randomapp.ui;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private TextInputEditText editText;
    private Calendar myCalendar;
    Context context;
    boolean is24HourView;

    public TimePickerFragment(TextInputEditText editText, Context context, boolean is24HourView, Calendar cal) {
        this.editText = editText;
        this.context = context;
        this.is24HourView = is24HourView;
        this.myCalendar = cal;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, this, hour, minute, is24HourView);
        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String timeVal = null;

        if(!is24HourView) {
            timeVal = hourOfDay > 12? (hourOfDay - 12) +":" + minute + " PM": hourOfDay +":" + minute + " AM";
        } else if(is24HourView){
            timeVal = hourOfDay + ":" + minute;
        }
        this.editText.setText(timeVal);
    }
}