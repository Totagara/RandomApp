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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

final public class RandomDecimalNumbersGenTask extends AsyncTask<Void, Integer, ArrayList<BigDecimal>> {
    Boolean isToBeSorted, isAscendingSort, isRepeatAllowed;
    Double minRange, maxRange;
    Integer quantity, precision;
    Context context;
    EditText editText;
    ProgressDialog progressBarDialog;
    String resultsSeparator;

    public RandomDecimalNumbersGenTask(Context context, Double dMinVal, Double dMaxVal, Integer quantityVal, int precision, boolean isRepeatAllowed, boolean isToBeSorted, boolean isAscendingSort, EditText resultsEditText, String resultsSeparator) {
        this.minRange = dMinVal;
        this.maxRange = dMaxVal;
        this.quantity = quantityVal;
        this.precision = precision;
        this.context = context;
        this.isToBeSorted = isToBeSorted;
        this.isAscendingSort = isAscendingSort;
        this.isRepeatAllowed = isRepeatAllowed;
        this.editText = resultsEditText;
        this.resultsSeparator = resultsSeparator;
    }

    @Override
    protected void onPreExecute() {
        progressBarDialog = new ProgressDialog(context);
        progressBarDialog.setCanceledOnTouchOutside(false);
        progressBarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBarDialog.setTitle(context.getString(R.string.text_random_numbers));
        progressBarDialog.setMessage(context.getString(R.string.text_generating_decimals));
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
    protected ArrayList<BigDecimal> doInBackground(Void... voids) {
        ArrayList<BigDecimal> randomNumbers = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            Double randomNumber = GenerateDoubleRandomNumber(minRange, maxRange, precision);

            if(isRepeatAllowed){
                randomNumbers.add(BigDecimal.valueOf(randomNumber));
            } else {
                while (randomNumbers.contains(randomNumber)){
                    randomNumber = GenerateDoubleRandomNumber(minRange, maxRange, precision);
                }
                randomNumbers.add(BigDecimal.valueOf(randomNumber));
            }

            Double percentageFact = (double) i/quantity;
            int progressCount = (int) (percentageFact * 100.0);
            publishProgress(progressCount, i, quantity);
        }

        //sort the array
        if(isToBeSorted){
            if(isAscendingSort){
                Collections.sort(randomNumbers);
            }
            else {
                Collections.sort(randomNumbers, Collections.reverseOrder());
            }
        }
        return randomNumbers;
    }

    @Override
    protected void onPostExecute(ArrayList<BigDecimal> randomNumbers) {
        String randomNumbersResult = TextUtils.join(resultsSeparator, randomNumbers);
        editText.setText(randomNumbersResult);
        progressBarDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progressCount = values[0];
        progressBarDialog.setMessage(context.getString(R.string.text_generating_decimals) + " " + values[1] +"/"+values[2]);
        progressBarDialog.setProgress(progressCount);
    }



    double GenerateDoubleRandomNumber(Double minVal, Double maxVal, int pricision){
        return RoundUp(ThreadLocalRandom.current().nextDouble(minVal, maxVal), pricision);
    }

    private double RoundUp(double val, int precision){
        if (precision < 0)
                precision = 1;

        BigDecimal bd = new BigDecimal(Double.toString(val));
        bd = bd.setScale(precision, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static BigInteger GetDoubleRange(double minVal, double maxVal, int precision) {
        BigInteger minRange = RemoveDoubleDecimalPrecisionToBigInteger(minVal, precision);
        BigInteger maxRange = RemoveDoubleDecimalPrecisionToBigInteger(maxVal, precision);
        BigInteger range =  maxRange.subtract(minRange);
        return range;
    }

    private static BigInteger RemoveDoubleDecimalPrecisionToBigInteger(double minVal, int precision) {
        String minValStr = BigDecimal.valueOf(minVal).toPlainString();
        String[] minValDigits = minValStr.split(Pattern.quote("."));
        BigInteger rangeVal = null;
        if(minValDigits.length == 2){
            if(minValDigits[1].length() < precision){
                while(minValDigits[1].length() < precision)
                    minValDigits[1] += "0";
            } else {
                minValDigits[1] = minValDigits[1].substring(0,precision);
            }
            rangeVal = new BigInteger(minValDigits[0] + minValDigits[1]);
        } else if(minValDigits.length == 1){
                StringBuilder decimalDigits = new StringBuilder();
                while(decimalDigits.length() < precision)
                    decimalDigits.append("0");

                rangeVal = new BigInteger(minValDigits[0] + decimalDigits.toString());
            }
        return rangeVal;
    }

    /*private static String GetPrecisionFormatter(int precision) {
        String formatter = "";
        for (int i = 0; i < precision; i++) {
            formatter += "#";
        }
        return formatter;
    }*/
}
