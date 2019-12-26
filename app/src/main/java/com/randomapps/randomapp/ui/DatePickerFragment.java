package com.randomapps.randomapp.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private TextInputEditText edit;
    String dateFormat;
    Calendar calendar;

    public DatePickerFragment(TextInputEditText edit, String dateFormat, Calendar calendar) {
        this.edit = edit;
        this.calendar = calendar;
        if(dateFormat == null || dateFormat.isEmpty()){
            this.dateFormat = "dd-MMM-YYYY";
        } else {
            this.dateFormat = dateFormat;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);

        Calendar minCal = Calendar.getInstance();
        minCal.add(Calendar.YEAR, -40);
        long minDateLimit = minCal.getTimeInMillis();
        datePickerDialog.getDatePicker().setMinDate(minDateLimit);

        Calendar maxCal = Calendar.getInstance();
        maxCal.add(Calendar.YEAR, 17);
        long maxDateLimit = maxCal.getTimeInMillis();
        datePickerDialog.getDatePicker().setMaxDate(maxDateLimit);

        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Time chosenDate = new Time();
        chosenDate.set(day, month, year);
        long dtDob = chosenDate.toMillis(true);

        CharSequence selectedDate = DateFormat.format(dateFormat, dtDob);
        edit.setText(selectedDate.toString());
    }
}