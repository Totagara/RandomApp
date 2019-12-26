package random;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.EditText;

import com.randomapps.randomapp.R;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

final public class RandomDateTimeGenTask extends AsyncTask<Void, Integer, ArrayList<String>> {
    Context context;
    long startDateInMs, endDateInMs;
    ProgressDialog progressBarDialog;
    Integer quantity;
    Boolean isRepeatAllowed, onlyDates, onlyTimes, is24HoursFormat;
    SimpleDateFormat resultsFormat;
    EditText editText;
    String generationType;
    String resultsSeparator;

    public RandomDateTimeGenTask(Context context, long startDateInMs, long endDateInMs, Integer quantity, Boolean isRepeatAllowed, Boolean onlyDates, Boolean onlyTimes, Boolean is24HoursFormat, SimpleDateFormat resultsFormat, EditText editText, String resultsSeparator) {
        this.context = context;
        this.startDateInMs = startDateInMs;
        this.endDateInMs = endDateInMs;
        this.quantity = quantity;
        this.isRepeatAllowed = isRepeatAllowed;
        this.onlyDates = onlyDates;
        this.onlyTimes = onlyTimes;
        this.is24HoursFormat = is24HoursFormat;
        this.resultsFormat =   resultsFormat;
        this.editText = editText;
        this.resultsSeparator = resultsSeparator;
    }

    @Override
    protected void onPreExecute() {
        generationType = (onlyDates && onlyTimes)? context.getString(R.string.text_dates_and_times): onlyDates? context.getString(R.string.text_dates) : context.getString(R.string.text_times);
        progressBarDialog = new ProgressDialog(context);
        progressBarDialog.setCanceledOnTouchOutside(false);
        progressBarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBarDialog.setTitle(context.getString(R.string.text_random) + " " +  generationType);
        progressBarDialog.setMessage(context.getString(R.string.text_generating) + " " + generationType + " : ");
        progressBarDialog.setMax(100);
        //Cancel on backbutton click
        progressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.cancel();
                cancel(true);
            }
        });
        progressBarDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, context.getString(R.string.but_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                cancel(true);
                //progressBarDialog.dismiss();
            }
        });
        progressBarDialog.show();
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        ArrayList<String> randomDates = new ArrayList<String>();
        if(resultsFormat == null) resultsFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");

        for (int i = 0; i < quantity; i++) {
            String dateText = ProduceRandomDate();
            if(isRepeatAllowed){
                randomDates.add(dateText);
            } else {
                while (randomDates.contains(dateText)){
                    dateText = ProduceRandomDate();
                }
                randomDates.add(dateText);
            }

            Double percentageFact = (double) i/quantity;
            int progressCount1 = (int) (percentageFact * 100.0000);
            String decimalString = BigDecimal.valueOf(progressCount1).toPlainString();
            int progressCount = Integer.parseInt(decimalString);
            publishProgress(progressCount, i, quantity);
        }
        return randomDates;
    }

    private String ProduceRandomDate(){
        long random = ThreadLocalRandom.current().nextLong(startDateInMs, endDateInMs);
        Date date = new Date(random);
        String dateText = GetResultInTimeFormat(date, resultsFormat,is24HoursFormat, onlyDates, onlyTimes);
        return dateText;
    }

    @Override
    protected void onPostExecute(ArrayList<String> randomStrings) {
        //String randomNumbersResult = TextUtils.join(", ", randomStrings);
        String randomNumbersResult = TextUtils.join(resultsSeparator, randomStrings);
        editText.setText(randomNumbersResult);
        progressBarDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progressCount = values[0];
        progressBarDialog.setMessage(context.getString(R.string.text_generating) + " " + generationType + " : " + values[1] +"/"+values[2]);
        progressBarDialog.setProgress(progressCount);
    }

    public ArrayList<String> GetRandomDates(long startDate, long endDate, boolean isRepeatAllowed, int howMany, boolean onlyDates, boolean onlyTimes, SimpleDateFormat format, boolean is24HoursFormat){
        ArrayList<Date> randomDates = new ArrayList<Date>();
        ArrayList<String> randomDatesInFormat = new ArrayList<String>();
        //SimpleDateFormat dateFormat = null;
        SimpleDateFormat resultsFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");

        if(format != null){
            resultsFormat = format;
        }

        String dateText = null;
        for (int i = 0; i < howMany; i++) {
            long random = ThreadLocalRandom.current().nextLong(startDate, endDate);
            Date date = new Date(random);
            dateText = GetResultInTimeFormat(date, resultsFormat,is24HoursFormat, onlyDates, onlyTimes);
            if(isRepeatAllowed){
                randomDatesInFormat.add(dateText);
            } else {
                while (randomDatesInFormat.contains(dateText)){
                    random = ThreadLocalRandom.current().nextLong(startDate, endDate);
                    date = new Date(random);
                    dateText = GetResultInTimeFormat(date, resultsFormat,is24HoursFormat, onlyDates, onlyTimes);
                }
                randomDatesInFormat.add(dateText);
            }
        }
        return randomDatesInFormat;
    }

    String GetResultInTimeFormat(Date date, SimpleDateFormat resultsFormat, boolean is24HoursFormat, boolean onlyDates, boolean onlyTimes){
        String timeText = null;

        if(is24HoursFormat){
            return resultsFormat.format(date);
        } else if(onlyDates && !onlyTimes){
            return resultsFormat.format(date);
        } else if(onlyDates && onlyTimes){
            int hour = 0;
            hour = Integer.parseInt(resultsFormat.format(date).split(" ")[1].split(":")[0]);
            if (hour > 12) {
                timeText = (hour - 12) + ":" + resultsFormat.format(date).split(" ")[1].split(":")[1] + " PM";
            } else {
                timeText = hour + ":" + resultsFormat.format(date).split(" ")[1].split(":")[1] + " AM";
            }
            return resultsFormat.format(date).split(" ")[0] + " " + timeText;
        } else if(!onlyDates && onlyTimes){
            int hour = 0;
            hour = Integer.parseInt(resultsFormat.format(date).split(":")[0]);
            if (hour > 12) {
                timeText = (hour - 12) + ":" + resultsFormat.format(date).split(":")[1] + " PM";
            } else {
                timeText = hour + ":" + resultsFormat.format(date).split(":")[1] + " AM";
            }
            return timeText;
        }
        return null;
    }

    public BigInteger GetDateTimeRange(Date startDate, Date endDate, boolean onlyDates, boolean onlyTimes, boolean isRepeatAllowed) {
        long diff = endDate.getTime() - startDate.getTime();
        if(onlyDates && onlyTimes){ //Both dates & times
                return BigInteger.valueOf(TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS));
        } else if(onlyDates) {   //only dates
            return BigInteger.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } else {    //only times
            return BigInteger.valueOf(TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS));
        }
    }
}
