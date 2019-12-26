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
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

final public class RandomIntegerNumbersGenTask extends AsyncTask<Void, Integer, ArrayList<Integer>> {
    Boolean isToBeSorted, isAscendingSort, isRepeatAllowed;
    Integer quantity, minRange, maxRange;
    Context context;
    EditText editText;
    ProgressDialog progressBarDialog;
    String resultsSeparator;

    public RandomIntegerNumbersGenTask(Context context, Boolean isToBeSorted, Boolean isAscendingSort, Boolean isRepeatAllowed, Integer quantity, Integer minRange, Integer maxRange, EditText resultsEditText, String resultsSeparator) {
        this.context = context;
        this.isToBeSorted = isToBeSorted;
        this.isAscendingSort = isAscendingSort;
        this.isRepeatAllowed = isRepeatAllowed;
        this.quantity = quantity;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.editText = resultsEditText;
        this.resultsSeparator = resultsSeparator;
    }

    @Override
    protected void onPreExecute() {
        progressBarDialog = new ProgressDialog(context);
        progressBarDialog.setCanceledOnTouchOutside(false);
        progressBarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBarDialog.setTitle(context.getString(R.string.text_random_numbers));
        progressBarDialog.setMessage(context.getString(R.string.text_generating_integers));
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
    protected ArrayList<Integer> doInBackground(Void... voids) {
        ArrayList<Integer> randomNumbers = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            Integer randomNumber = ThreadLocalRandom.current().nextInt(minRange, maxRange+1);
            if(isRepeatAllowed){
                randomNumbers.add(randomNumber);
            } else {
                while (randomNumbers.contains(randomNumber)){
                    randomNumber = ThreadLocalRandom.current().nextInt(minRange, maxRange+1);
                }
                randomNumbers.add(randomNumber);
            }

            Double percentageFact = ((double) i/quantity) * 100.0;
            String decimalString = BigDecimal.valueOf(percentageFact).toPlainString();
            int progressCount = (int) Double.parseDouble(decimalString);
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
    protected void onPostExecute(ArrayList<Integer> randomNumbers) {
        String randomNumbersResult = TextUtils.join(resultsSeparator, randomNumbers);
        editText.setText(randomNumbersResult);
        progressBarDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progressCount = values[0];
        progressBarDialog.setMessage(context.getString(R.string.text_generating_integers) + " " + values[1] +"/"+values[2]);
        progressBarDialog.setProgress(progressCount);
    }

    public static BigInteger GetIntegerRange(Integer minVal, Integer maxVal) {
        //BigInteger range = BigInteger.valueOf((maxVal - minVal) + 1);
        BigInteger range = BigInteger.valueOf(maxVal).subtract(BigInteger.valueOf(minVal));
        BigInteger upperRange = range.add(BigInteger.valueOf(1));
        return upperRange;
    }
}
