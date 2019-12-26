package random;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.EditText;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

final public class RandomColorsGenTask extends AsyncTask<Void, Integer, ArrayList<String>> {
    Integer quantity;
    ProgressDialog progressBarDialog;

    public RandomColorsGenTask(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        ThreadLocalRandom randomObject = ThreadLocalRandom.current();
        ArrayList<String> randomColors = null;
        if (quantity > 0) {
            randomColors = new ArrayList<>();
            for (int i = 0; i < quantity; i++) {
                String colorId = String.format("#%06x", randomObject.nextInt(256 * 256 * 256));

                //avoid repeated colors in the list
                while (randomColors.contains(colorId)){
                    colorId = String.format("#%06x", randomObject.nextInt(256 * 256 * 256));
                }
                randomColors.add(colorId);
            }
        }
        return randomColors;
    }
}
