package random;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.EditText;
import com.randomapps.randomapp.R;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

final public class RandomStringsGenTask extends AsyncTask<Void, Integer, ArrayList<String>> {
    Context context;
    Integer minLength, maxLength, quantity;
    Boolean isUpperCaseAllowed, isLowerCaseAllowed,isDigitsAllowed, isSpecialCharsAllowed;
    String specCharSet;
    EditText editText;
    ProgressDialog progressBarDialog;
    String resultsSeparator;

    public RandomStringsGenTask(Context context, Integer minLength, Integer maxLength, Integer quantity, Boolean isUpperCaseAllowed, Boolean isLowerCaseAllowed, Boolean isDigitsAllowed, Boolean isSpecialCharsAllowed, String specCharSet, EditText editText, String resultsSeparator) {
        this.context = context;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.quantity = quantity;
        this.isUpperCaseAllowed = isUpperCaseAllowed;
        this.isLowerCaseAllowed = isLowerCaseAllowed;
        this.isDigitsAllowed = isDigitsAllowed;
        this.isSpecialCharsAllowed = isSpecialCharsAllowed;
        this.specCharSet = specCharSet;
        this.editText = editText;
        this.resultsSeparator = resultsSeparator;
    }


    @Override
    protected void onPreExecute() {
        progressBarDialog = new ProgressDialog(context);
        progressBarDialog.setCanceledOnTouchOutside(false);
        progressBarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBarDialog.setTitle(context.getString(R.string.text_random_strings));
        progressBarDialog.setMessage(context.getString(R.string.text_generating_strings));
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
        ArrayList<String> randomStrings = null;
        String[] randomStringsArray = new String[quantity];
        StringBuilder randomCharSet = new StringBuilder();
        if(isUpperCaseAllowed){
            randomCharSet.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        }
        if(isLowerCaseAllowed){
            randomCharSet.append("abcdefghijklmnopqrstuvwxyz");
        }
        if(isDigitsAllowed){
            randomCharSet.append("0123456789");
        }
        if(isSpecialCharsAllowed){
            if(specCharSet.length() > 0){
                randomCharSet.append(specCharSet);
            }
        }

        for (int count=0; count < randomStringsArray.length; count++) {
            int randomLength = (minLength == maxLength)?minLength:ThreadLocalRandom.current().nextInt(minLength, maxLength);
            StringBuilder randomString = new StringBuilder();
            for (int i = 0; i < randomLength; i++) {
                double ran = Math.random();
                int len = randomCharSet.length();
                int ranInd = (int)(ran * len);

                // add Character one by one in end of sb
                randomString.append(randomCharSet
                        .charAt(ranInd));
            }
            randomStringsArray[count] = randomString.toString();

            Double percentageFact = (double) count/quantity;
            int progressCount1 = (int) (percentageFact * 100.0000);
            String decimalString = BigDecimal.valueOf(progressCount1).toPlainString();
            int progressCount = Integer.parseInt(decimalString);
            publishProgress(progressCount, count, quantity);
        }
        randomStrings = new ArrayList(Arrays.asList(randomStringsArray));
        return randomStrings;
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
        progressBarDialog.setMessage(context.getString(R.string.text_generating_strings) + " " + values[1] +"/"+values[2]);
        progressBarDialog.setProgress(progressCount);
    }
}
