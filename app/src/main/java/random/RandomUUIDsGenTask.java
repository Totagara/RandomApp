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
import java.util.UUID;

final public class RandomUUIDsGenTask extends AsyncTask<Void, Integer, ArrayList<String>> {
    Context context;
    Integer quantity;
    EditText editText;
    ProgressDialog progressBarDialog;
    String resultsSeparator;

    public RandomUUIDsGenTask(Context context, Integer quantity, EditText editText, String resultsSeparator) {
        this.context = context;
        this.quantity = quantity;
        this.editText = editText;
        this.resultsSeparator = resultsSeparator;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        ArrayList<String> randomUUIDs = new ArrayList<String>();
        for (int i = 0; i < quantity; i++) {
            randomUUIDs.add(UUID.randomUUID().toString());

            //Update progress status
            Double percentageFact = (double) i/quantity;
            int progressCount1 = (int) (percentageFact * 100.0000);
            String decimalString = BigDecimal.valueOf(progressCount1).toPlainString();
            int progressCount = Integer.parseInt(decimalString);
            publishProgress(progressCount, i, quantity);
        }
        return randomUUIDs;
    }

    @Override
    protected void onPreExecute() {
        progressBarDialog = new ProgressDialog(context);
        progressBarDialog.setCanceledOnTouchOutside(false);
        progressBarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBarDialog.setTitle(context.getString(R.string.text_UUIDs));
        progressBarDialog.setMessage(context.getString(R.string.text_generating_UUIDs));
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
    protected void onPostExecute(ArrayList<String> uuids) {
        String randomNumbersResult = TextUtils.join(resultsSeparator, uuids);
        editText.setText(randomNumbersResult);
        progressBarDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progressCount = values[0];
        progressBarDialog.setMessage(context.getString(R.string.text_generating_UUIDs) + " " + values[1] +"/"+values[2]);
        progressBarDialog.setProgress(progressCount);
    }
}
