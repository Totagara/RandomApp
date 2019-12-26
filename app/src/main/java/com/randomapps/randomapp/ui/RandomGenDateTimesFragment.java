package com.randomapps.randomapp.ui;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.randomapps.randomapp.R;
import com.randomapps.randomapp.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import random.RandomDateTimeGenTask;
import random.RandomTextFormatterTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class RandomGenDateTimesFragment extends Fragment {
    final int MAX_GENERATION_LIMIT = 50000;



    Boolean formatToVertical = true;

    //Random Date time UI elements
    TextInputEditText startDateEditText, endDateEditText, howManyDatesTimeEditText, endTimeEditText, startTimeEditText;
    CheckBox dateCheckbox, timeCheckbox, repeatDateTimeCheckBox;
    Button generateRandom;
    EditText resultsEditText;
    ImageView copyImageView, shareImageView, resSeparatorImageView;

    public RandomGenDateTimesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_random_gen_date_times, container, false);

        //Random Date time generator UI handling
        InitializeRandomDateTimeGeneratorUIElements(root);
        return root;
    }

    private void InitializeRandomDateTimeGeneratorUIElements(View root) {
        resultsEditText = root.findViewById(R.id.resultTextBox);
        resultsEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        copyImageView = root.findViewById(R.id.copyImageView);
        copyImageView.setOnClickListener(v -> CopyToClipBoard(v));

        shareImageView = root.findViewById(R.id.shareImageView);
        shareImageView.setOnClickListener(v -> ShareTheRandomResult(v));

        resSeparatorImageView = root.findViewById(R.id.resSeparatorImageView);
        resSeparatorImageView.setOnClickListener(v -> UpdateResultsWithSeparator(v));

        generateRandom = root.findViewById(R.id.butGenerateRandom);
        generateRandom.setOnClickListener(v -> GenerateRandomHandler(v));

        //Start date
        startDateEditText = root.findViewById(R.id.startDateEditText);
        startDateEditText.setOnClickListener(v -> SelectStartDateHandler(v));
        startDateEditText.setFocusable(false);
        startDateEditText.setClickable(true);

        Calendar startDateCalendar = Calendar.getInstance();
        startDateEditText.setText(GetDateFromCalendarValue(startDateCalendar));

        //End date
        endDateEditText = root.findViewById(R.id.endDateEditText);
        endDateEditText.setOnClickListener(v -> SelectEndDateHandler(v));
        endDateEditText.setFocusable(false);
        endDateEditText.setClickable(true);

        Calendar endDateCalendar = Calendar.getInstance();
        endDateCalendar.add(Calendar.DATE, 30);
        endDateEditText.setText(GetDateFromCalendarValue(endDateCalendar));

        //Start time
        startTimeEditText = root.findViewById(R.id.startTimeEditText);
        startTimeEditText.setOnClickListener(v -> SelectStartTimeHandler(v));
        startTimeEditText.setFocusable(false);
        startTimeEditText.setClickable(true);

        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeEditText.setText(GetTimeFromCalendarValue(startDateCalendar, false));

        //End time
        endTimeEditText = root.findViewById(R.id.endTimeEditText);
        endTimeEditText.setOnClickListener(v -> SelectEndTimeHandler(v));
        endTimeEditText.setFocusable(false);
        endTimeEditText.setClickable(true);

        Calendar endTimeCalendar = Calendar.getInstance();
        endTimeCalendar.add(Calendar.HOUR, 1);
        endTimeEditText.setText(GetTimeFromCalendarValue(endTimeCalendar, false));

        dateCheckbox = root.findViewById(R.id.dateCheckBox);
        dateCheckbox.setOnClickListener(v -> DateCheckBoxClickHandler(v));
        timeCheckbox = root.findViewById(R.id.timeCheckBox);
        timeCheckbox.setOnClickListener(v -> TimeCheckBoxClickHandler(v));

        repeatDateTimeCheckBox = root.findViewById(R.id.repeatdateTimeCheckBox);
        howManyDatesTimeEditText = root.findViewById(R.id.howManyDateTimeEditText);
        howManyDatesTimeEditText.setFilters(new InputFilter[] {Utils.AttachInputFilter(howManyDatesTimeEditText, 1,MAX_GENERATION_LIMIT, null, getActivity()) });
        howManyDatesTimeEditText.setText("1");

        //Set default checkbox options
        dateCheckbox.setChecked(true);
        timeCheckbox.setChecked(false);
        startTimeEditText.setEnabled(false);
        endTimeEditText.setEnabled(false);
    }

    private void ShareTheRandomResult(View v) {
        String content = resultsEditText.getText().toString();
        if(!content.equals("") && content != null) {
            try {
                Utils.ShareTheResultContent(content, getActivity());
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), getString(R.string.too_large_to_share_error), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_resultsTo_share_error), Toast.LENGTH_LONG).show();
        }
    }

    private void CopyToClipBoard(View v) {
        String content = resultsEditText.getText().toString();
        if(!content.equals("") && content != null) {
            try {
                String toastMsg = getString(R.string.results_copied_success_msg);
                Utils.CopyContentToClipBoard(getActivity(), getString(R.string.text_results), content, toastMsg);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), getString(R.string.too_large_to_copy_error), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_resultsTo_copy_error), Toast.LENGTH_LONG).show();
        }
    }

    private void UpdateResultsWithSeparator(View v) {
        String fromSeparator, toSeparator;
        String content = resultsEditText.getText().toString();

        if(!content.equals("") && content != null) {
            boolean isToBeVertical = formatToVertical;
            if (formatToVertical) {
                fromSeparator = ", ";
                toSeparator = "\n";
                formatToVertical = false;
            } else {
                toSeparator = ", ";
                fromSeparator = "\n";
                formatToVertical = true;
            }

            RandomTextFormatterTask randomTextFormatterTask = null;
            //randomTextFormatterTask = new RandomTextFormatterTask(getActivity(), fromSeparator, toSeparator, content , resultsEditText, resSeparatorImageView, isToBeVertical);
            randomTextFormatterTask = new RandomTextFormatterTask(getActivity(), fromSeparator, toSeparator, content, resultsEditText, resSeparatorImageView, isToBeVertical);


            if (Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
                randomTextFormatterTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                randomTextFormatterTask.execute();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_resultsTo_format_error), Toast.LENGTH_LONG).show();
        }
    }

    private void GenerateRandomHandler(View v) {
        //Hide the keyBoard
        Utils.hideKeyboard(v, getActivity());

        //For Random Date and Time
        GenerateRandomDatesTimesHandler(v);
    }

    private void GenerateRandomDatesTimesHandler(View v) {
        resultsEditText.setText("");
        boolean isRepeatAllowed = repeatDateTimeCheckBox.isChecked();
        boolean onlyDates = dateCheckbox.isChecked();
        boolean onlyTimes = timeCheckbox.isChecked();
        boolean is24HoursFormat = false;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date startDate = null, endDate = null;
        SimpleDateFormat resultsFormat = null;
        
        //if(ValidateGenericInputs(onlyDates, onlyTimes, quantity)) {
        if(ValidateGenericInputs(onlyDates, onlyTimes)) {
            int quantity = Integer.valueOf(howManyDatesTimeEditText.getText().toString());
            if (onlyDates && onlyTimes) {  //both dates and times
                resultsFormat = dateTimeFormat;

                String fromTime24FormatVal = Get24TimeFormatValueInString(startTimeEditText.getText().toString());
                String endTime24FormatVal = Get24TimeFormatValueInString(endTimeEditText.getText().toString());
                try {
                    startDate = dateTimeFormat.parse(startDateEditText.getText().toString() + " " + fromTime24FormatVal);
                    endDate = dateTimeFormat.parse(endDateEditText.getText().toString() + " " + endTime24FormatVal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (onlyDates && !onlyTimes) { //Only dates
                resultsFormat = dateFormat;

                //Parse start and end dates
                try {
                    startDate = dateFormat.parse(startDateEditText.getText().toString());
                    endDate = dateFormat.parse(endDateEditText.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (!onlyDates && onlyTimes) {   //Only times
                resultsFormat = timeFormat;

                String fromTime24FormatVal = Get24TimeFormatValueInString(startTimeEditText.getText().toString());
                String endTime24FormatVal = Get24TimeFormatValueInString(endTimeEditText.getText().toString());
                try {
                    startDate = dateTimeFormat.parse(dateFormat.format(new Date()) + " " + fromTime24FormatVal);
                    endDate = dateTimeFormat.parse(dateFormat.format(new Date()) + " " + endTime24FormatVal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            //boolean isToBeVertical  = (resSeparatorImageView.getDrawable().getConstantState() == ContextCompat.getDrawable(getContext(), R.drawable.ic_format_vertical).getConstantState())? true: false;
            //String resultsSeparator = isToBeVertical? ", " : "\n";

            String resultsSeparator = formatToVertical? ", " : "\n";

            RandomDateTimeGenTask randomDateTimeGenTask = null;
            randomDateTimeGenTask = new RandomDateTimeGenTask(getActivity(),startDate.getTime(), endDate.getTime(),
                    quantity, isRepeatAllowed, onlyDates, onlyTimes, is24HoursFormat, resultsFormat, resultsEditText, resultsSeparator);

            if(ValidateStartEndDate(startDate, endDate, onlyDates, onlyTimes, randomDateTimeGenTask, isRepeatAllowed, quantity)){
                randomDateTimeGenTask.execute();
            } else {
                //if validation fails empty asynctask thread
                randomDateTimeGenTask = null;
            }
        }
    }

    private boolean ValidateStartEndDate(Date startDate, Date endDate, boolean onlyDates, boolean onlyTimes, RandomDateTimeGenTask randomDateTimeGenTask, boolean isRepeatAllowed, int quantity) {
        if(onlyDates && onlyTimes) {
            if (endDate.before(startDate)) {
                startDateEditText.requestFocus();
                startDateEditText.setError(getString(R.string.max__dateTime_is_more_than_min_msg, getString(R.string.text_date_and_time), getString(R.string.text_date_and_time)));
                return false;
            } else {
                startDateEditText.setError(null);
            }

        } else if(onlyDates){
            if (endDate.before(startDate)) {
                startDateEditText.requestFocus();
                startDateEditText.setError(getString(R.string.max__dateTime_is_more_than_min_msg, getString(R.string.text_date), getString(R.string.text_date)));
                return false;
            } else {
                startDateEditText.setError(null);
            }
        } else {
            if (endDate.before(startDate)) {
                startTimeEditText.requestFocus();
                startDateEditText.setError(getString(R.string.max__dateTime_is_more_than_min_msg, getString(R.string.text_time), getString(R.string.text_time)));
                return false;
            } else {
                startTimeEditText.setError(null);
            }
        }

        //Range validations
        BigInteger range = randomDateTimeGenTask.GetDateTimeRange(startDate, endDate, onlyDates, onlyTimes, isRepeatAllowed);
        BigInteger decimalQuantity = BigInteger.valueOf(quantity);
        if (!isRepeatAllowed && decimalQuantity.compareTo(range) == 1) {
            howManyDatesTimeEditText.requestFocus();
            String generationType = (onlyDates && onlyTimes)? getString(R.string.text_date_time_combinations): onlyDates? getString(R.string.text_dates) : getString(R.string.text_times);
            howManyDatesTimeEditText.setError(getString(R.string.dateTime_range_exceed_error_msg, range, generationType));
            return false;
        } else {
            howManyDatesTimeEditText.setError(null);
        }
        return true;
    }

    private boolean ValidateGenericInputs(boolean onlyDates, boolean onlyTimes) {

        //Check empty validation
        if(howManyDatesTimeEditText.getText().toString().isEmpty() || howManyDatesTimeEditText.getText().toString() == null){
            howManyDatesTimeEditText.setError(getString(R.string.value_empty_error_msg));
            howManyDatesTimeEditText.requestFocus();
            return false;
        } else {
            howManyDatesTimeEditText.setError(null);
        }

        /*if(howManyDatesTimeEditText.getText().toString().isEmpty() || howManyDatesTimeEditText.getText().toString() == null){
            howManyDatesTimeEditText.setError(getString(R.string.value_greater_than_zero_error_msg));
            howManyDatesTimeEditText.requestFocus();
            return false;
        } else {
            howManyDatesTimeEditText.setError(null);
        }*/

        if(!onlyDates && !onlyTimes){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this.getActivity())
                    .setMessage(getString(R.string.no_date_time_selected_error))
                    .setTitle(getString(R.string.dialog_header_usage_notice)).setPositiveButton(getString(R.string.button_ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
            dialog.show();
            return false;
        }

        //Quantity input validations
        int quantity = Integer.valueOf(howManyDatesTimeEditText.getText().toString());
        if(quantity < 1){
            howManyDatesTimeEditText.setError(getString(R.string.value_greater_than_zero_error_msg));
            howManyDatesTimeEditText.requestFocus();
            return false;
        } else {
            howManyDatesTimeEditText.setError(null);
        }

        //MAX Generation limit
        if(quantity > MAX_GENERATION_LIMIT){
            howManyDatesTimeEditText.setError(getString(R.string.max_generation_limit_error_msg_dateTime, MAX_GENERATION_LIMIT));
            howManyDatesTimeEditText.requestFocus();
            return false;
        } else {
            howManyDatesTimeEditText.setError(null);
        }

        if(dateCheckbox.isChecked()) {
            if (startDateEditText.getText() == null || startDateEditText.getText().toString().isEmpty()) {
                startDateEditText.setError(getString(R.string.valid_input_required_error, getString(R.string.text_date)));
                startDateEditText.requestFocus();
                return false;
            } else {
                startDateEditText.setError(null);
            }

            if (endDateEditText.getText() == null || endDateEditText.getText().toString().isEmpty()) {
                endDateEditText.setError(getString(R.string.valid_input_required_error, getString(R.string.text_date)));
                endDateEditText.requestFocus();
                return false;
            } else {
                endDateEditText.setError(null);
            }
        }

        if(timeCheckbox.isChecked()) {
            if (startTimeEditText.getText() == null || startTimeEditText.getText().toString().isEmpty()) {
                startTimeEditText.setError(getString(R.string.valid_input_required_error, getString(R.string.text_time)));
                startTimeEditText.requestFocus();
                return false;
            } else {
                startTimeEditText.setError(null);
            }

            if (endTimeEditText.getText() == null || endTimeEditText.getText().toString().isEmpty()) {
                endTimeEditText.setError(getString(R.string.valid_input_required_error, getString(R.string.text_time)));
                endTimeEditText.requestFocus();
                return false;
            } else {
                endTimeEditText.setError(null);
            }
        }
        return true;
    }

    private String Get24TimeFormatValueInString(String timeVal){
        int hour, min;
        if(timeVal != null && !timeVal.isEmpty()){
            String[] splitItems = timeVal.split(" ");
            if(splitItems[1].equals("PM")){
                hour = Integer.valueOf(splitItems[0].split(":")[0]) + 12;
            } else {
                hour = Integer.valueOf(splitItems[0].split(":")[0]);
            }
            min = Integer.valueOf(splitItems[0].split(":")[1]);
            return hour + ":" + min + ":00";
        }
        return null;
    }

    private void TimeCheckBoxClickHandler(View v) {
        if(((CheckBox)v).isChecked()){
            startTimeEditText.setEnabled(true);
            endTimeEditText.setEnabled(true);
        } else {
            startTimeEditText.setEnabled(false);
            endTimeEditText.setEnabled(false);
        }
    }

    private void DateCheckBoxClickHandler(View v) {
        if(((CheckBox)v).isChecked()){
            startDateEditText.setEnabled(true);
            endDateEditText.setEnabled(true);
        } else {
            startDateEditText.setEnabled(false);
            endDateEditText.setEnabled(false);
        }
    }

    private String GetTimeFromCalendarValue(Calendar cal, boolean is24HourFormat){
        SimpleDateFormat resultsFormat = new SimpleDateFormat("HH:mm");
        Date current = cal.getTime();
        String timeText = null;
        if(!is24HourFormat){
            int hour = Integer.valueOf(resultsFormat.format(current).split(":")[0]);
            if(hour > 12){
                timeText = (hour - 12) + ":" + resultsFormat.format(current).split(":")[0] + " PM";
            } else {
                timeText = hour + ":" + resultsFormat.format(current).split(":")[0] + " AM";
            }
        } else {
            timeText = resultsFormat.format(current);
        }
        return timeText;
    }

    private String GetDateFromCalendarValue(Calendar cal){
        SimpleDateFormat resultsFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date current = cal.getTime();
        return resultsFormat.format(current);
    }

    private void SelectStartDateHandler(View v) {
        Calendar calendar = Calendar.getInstance();
        DialogFragment newFragment = new DatePickerFragment(startDateEditText, "dd-MMM-yyyy", calendar);
        newFragment.show(this.getActivity().getSupportFragmentManager(), "DatePicker");
    }

    private void SelectEndDateHandler(View v) {
        Calendar calendar =Calendar.getInstance();
        calendar.add(Calendar.DATE, 30);
        DialogFragment newFragment = new DatePickerFragment(endDateEditText, "dd-MMM-yyyy", calendar);
        newFragment.show(this.getActivity().getSupportFragmentManager(), "DatePicker");
    }

    private void SelectStartTimeHandler(View v) {
        Calendar startTimeCalendar = Calendar.getInstance();
        DialogFragment newFragment = new TimePickerFragment(startTimeEditText, this.getActivity(), false, startTimeCalendar);
        newFragment.show(this.getActivity().getSupportFragmentManager(), "TimePicker");
    }

    private void SelectEndTimeHandler(View v) {
        Calendar endTimeCalendar = Calendar.getInstance();
        endTimeCalendar.add(Calendar.HOUR, 1);
        DialogFragment newFragment = new TimePickerFragment(endTimeEditText, this.getActivity(), false, endTimeCalendar);
        newFragment.show(this.getActivity().getSupportFragmentManager(), "TimePicker");
    }
}
