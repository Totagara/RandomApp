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
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

//final public class RandomSeriesGenTask extends AsyncTask<Void, Integer, ArrayList<ArrayList<String>>> {
final public class RandomSeriesGenTask extends AsyncTask<Void, Integer, List<List<String>>> {

    Context context;
    List<String> seriesItemsList = new ArrayList<>();
    Integer seriesSize, quantity;
    Boolean excludeGeneratedItems;
    EditText editText;
    ProgressDialog progressBarDialog;
    boolean isTaskCancelled = false;

    public RandomSeriesGenTask(Context context, Integer quantity, Integer seriesSize, List<String> seriesItemsList,  Boolean excludeGeneratedItems, EditText editText) {
        this.context = context;
        //this.seriesItemsList = seriesItemsList;
        this.seriesItemsList.addAll(seriesItemsList);
        this.seriesSize = seriesSize;
        this.quantity = quantity;
        this.excludeGeneratedItems = excludeGeneratedItems;
        this.editText = editText;
    }

    @Override
    protected void onPreExecute() {
        progressBarDialog = new ProgressDialog(context);
        progressBarDialog.setCanceledOnTouchOutside(false);
        progressBarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBarDialog.setTitle(context.getString(R.string.app_seriesGenerator));
        progressBarDialog.setMessage(context.getString(R.string.text_generating_series));
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
    //protected ArrayList<ArrayList<String>> doInBackground(Void... voids) {
    protected List<List<String>> doInBackground(Void... voids) {
        //ArrayList<ArrayList<String>> generatedSeries = new ArrayList<>();
        List<List<String>> generatedSeries = new ArrayList<>();
        List<String> itemSet = new ArrayList<>();

        if(excludeGeneratedItems){
            itemSet = RemoveDuplicatedItemsFromList(seriesItemsList);
        } else {
            itemSet = seriesItemsList;
        }

        for (int i = 0; i < quantity; i++) {
            if(isTaskCancelled) break;

            List<String> series = GetShuffledSeries(seriesSize, itemSet);
            while (generatedSeries.contains(series)){
                series = GetShuffledSeries(seriesSize, itemSet);
            }
            generatedSeries.add(series);
            if(excludeGeneratedItems){
                itemSet.removeAll(series);
            }

            //Progress update
            Double percentageFact = (double) i/quantity;
            int progressCount1 = (int) (percentageFact * 100.0000);
            String decimalString = BigDecimal.valueOf(progressCount1).toPlainString();
            int progressCount = Integer.parseInt(decimalString);
            publishProgress(progressCount, i, quantity);
        }
        return generatedSeries;
    }

    @Override
    protected void onPostExecute(List<List<String>> randomSeries) {
        StringBuilder resultString = new StringBuilder();

        int count = 0;
        for (List<String> seriesItem: randomSeries) {
            count ++;
            resultString.append(context.getString(R.string.label_series) + " "+ count + " :" + "  ");
            resultString.append(TextUtils.join(", ", seriesItem));
            resultString.append("\n");
        }
        editText.setText(resultString.toString());
        progressBarDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progressCount = values[0];
        progressBarDialog.setMessage(context.getString(R.string.text_generating_series) + " " + values[1] +"/"+values[2]);
        progressBarDialog.setProgress(progressCount);
    }

    private List<String> GetShuffledSeries(int seriesSize, List<String> itemSet) {
        //if item set size is less than seriesSize just return shuffled itemset
        if (itemSet.size() < seriesSize) {
            Collections.shuffle(itemSet);
            return itemSet;
        }
        ThreadLocalRandom randomObject = ThreadLocalRandom.current();
        List<String> itemSetForShuffle = new ArrayList<>();
        itemSetForShuffle.addAll(itemSet);
        List<String> series = new ArrayList<>();
        if(itemSet.size() > 0) {
            StringBuilder item = new StringBuilder();
            for (int i = 0; i < seriesSize; i++) {
                item.setLength(0);
                Collections.shuffle(itemSetForShuffle);
                item.append(itemSetForShuffle.get(randomObject.nextInt(itemSetForShuffle.size())));
                series.add(item.toString());
                itemSetForShuffle.remove(item.toString());
            }
        }
        return series;
    }

    @Override
    protected void onCancelled() {
        isTaskCancelled = true;
        super.onCancelled();
    }

    @Override
    protected void onCancelled(List<List<String>> arrayLists) {
        super.onCancelled(arrayLists);
    }

    public BigInteger GetPossibleSeriesCount(List<String> seriesItemsList, int seriesSize, int howMany, Boolean excludeSetsWithSameItems, Boolean excludeGeneratedItems, int itemsCount) {
        BigInteger nFact, rFact, n_rFact;
        if(excludeGeneratedItems){ // itemsCount/seriesSize
            //Check if list has duplicates
            List<String> listWithoutDuplicates = RemoveDuplicatedItemsFromList(seriesItemsList);
            return BigInteger.valueOf(listWithoutDuplicates.size()/seriesSize);
            //return BigInteger.valueOf(itemsCount/seriesSize);
        } else if(excludeSetsWithSameItems){ // nCr = n!/((n-r)! * r!)
            nFact = factorial(itemsCount);
            n_rFact = factorial(itemsCount - seriesSize);
            rFact = factorial(seriesSize);
            BigInteger n_rFact_Mul_rFact = n_rFact.multiply(rFact);
            BigInteger nCr = nFact.divide(n_rFact_Mul_rFact);
            return nCr;
        } else { // nPr = = n!/(n-r)!
            nFact = factorial(itemsCount);
            n_rFact = factorial(itemsCount - seriesSize);
            BigInteger nPr = nFact.divide(n_rFact);
            return nPr;
        }
    }

    private List<String> RemoveDuplicatedItemsFromList(List<String> seriesItemsList) {
        List<String> listWithoutDuplicates = new ArrayList<>();
        for (String item: seriesItemsList) {
            if(!listWithoutDuplicates.contains(item)){
                listWithoutDuplicates.add(item);
            }
        }
        return listWithoutDuplicates;
    }

    BigInteger factorial(int N) {
        // Initialize result
        BigInteger f = new BigInteger("1"); // Or BigInteger.ONE

        // Multiply f with 2, 3, ...N
        for (int i = 2; i <= N; i++)
            f = f.multiply(BigInteger.valueOf(i));

        return f;
    }
}
