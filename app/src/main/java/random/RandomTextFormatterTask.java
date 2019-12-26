package random;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.randomapps.randomapp.R;

final public class RandomTextFormatterTask extends AsyncTask<Void, Void, String> {

    Context context;
    String fromSeparator, toSeparator;
    String content;
    EditText editText;
    ProgressDialog progressBarDialog;
    ImageView resSeparatorImageView;
    Boolean isToBeVertical;

    public RandomTextFormatterTask(Context context, String fromSeparator, String toSeparator, String content, EditText editText, ImageView resSeparatorImageView, Boolean isToBeVertical) {
        this.context = context;
        this.fromSeparator = fromSeparator;
        this.toSeparator = toSeparator;
        this.content = content;
        this.editText = editText;
        this.resSeparatorImageView = resSeparatorImageView;
        this.isToBeVertical = isToBeVertical;
    }

    @Override
    protected void onPreExecute() {
        progressBarDialog = new ProgressDialog(context);
        progressBarDialog.setCanceledOnTouchOutside(false);
        progressBarDialog.setCancelable(true);
        progressBarDialog.setTitle(context.getString(R.string.text_formatting));
        progressBarDialog.setMessage(context.getString(R.string.text_formatting_results));

        //Cancel on backbutton click
        progressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.cancel();
                cancel(true);
            }
        });
        progressBarDialog.show();
    }

    @Override
    protected void onCancelled() {
        editText.setText(content);
        progressBarDialog.dismiss();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String[] resultValues = content.split(fromSeparator);
        String updatedText = TextUtils.join(toSeparator, resultValues);
        return updatedText;
    }

    @Override
    protected void onPostExecute(String updatedText) {
        editText.setText(updatedText);
        int drawableId = isToBeVertical? R.drawable.ic_format_horizontal : R.drawable.ic_format_vertical;
        resSeparatorImageView.setImageResource(drawableId);
        progressBarDialog.dismiss();
    }
}
