package random;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.EditText;

import com.randomapps.randomapp.R;
import com.randomapps.randomapp.utils.PartitionHelper;

import java.util.ArrayList;
import java.util.List;

final public class RandomDivideAndAssignGenTask extends AsyncTask<Void, Void, List<List<String>>> {

    Context context;
    List<String> seriesItemsList;
    Integer itemsPerTeam, howManyTeams;
    EditText editText;
    ProgressDialog progressBarDialog;

    public RandomDivideAndAssignGenTask(Context context, List<String> seriesItemsList,Integer howManyTeams, Integer itemsPerTeam, EditText editText) {
        this.context = context;
        this.seriesItemsList = seriesItemsList;
        this.howManyTeams = howManyTeams;
        this.itemsPerTeam = itemsPerTeam;
        this.editText = editText;
    }

    @Override
    protected void onPreExecute() {
        progressBarDialog = new ProgressDialog(context);
        progressBarDialog.setCanceledOnTouchOutside(false);
        //progressBarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBarDialog.setTitle(context.getString(R.string.app_teamsGenerator));
        progressBarDialog.setMessage(context.getString(R.string.text_generating_teams));
        //progressBarDialog.setMax(100);
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
    //protected PartitionHelper<String> doInBackground(Void... voids) {
    protected List<List<String>> doInBackground(Void... voids) {
        publishProgress();
        List<List<String>> teams = new ArrayList<>();

        //if after teams members left out assign them to teams equally
        if(howManyTeams * itemsPerTeam < seriesItemsList.size()){
            int extraItems = seriesItemsList.size() - (howManyTeams * itemsPerTeam);
            List<String> splitListOne = new ArrayList<>();
            List<String> splitListTwo = new ArrayList<>();
            int splitCount = (extraItems * itemsPerTeam) + extraItems;
            splitListOne.addAll(seriesItemsList.subList(0,splitCount));
            splitListTwo.addAll(seriesItemsList.subList(splitCount, seriesItemsList.size()));
            PartitionHelper<String> partitionedTeams1 = PartitionHelper.ofSize(splitListOne, itemsPerTeam+1);
            PartitionHelper<String> partitionedTeams2 = PartitionHelper.ofSize(splitListTwo, itemsPerTeam);

            List<PartitionHelper<String>> partitionedSets = new ArrayList<>();
            partitionedSets.add(partitionedTeams1);
            partitionedSets.add(partitionedTeams2);

            teams = GetTeamsListFromPartitionedSets(partitionedSets);
        } else {
            PartitionHelper<String> partitionedTeams = PartitionHelper.ofSize(seriesItemsList, itemsPerTeam);
            List<PartitionHelper<String>> partitionedSets = new ArrayList<>();
            partitionedSets.add(partitionedTeams);
            teams = GetTeamsListFromPartitionedSets(partitionedSets);
        }
        return teams;
    }

    private List<List<String>> GetTeamsListFromPartitionedSets(List<PartitionHelper<String>> partitionedSets) {
        List<List<String>> teams = new ArrayList<>();
        for (PartitionHelper<String> partition : partitionedSets) {
            for (List<String> team: partition) {
                teams.add(team);
            }
        }
        return teams;
    }

    @Override
    //protected void onPostExecute(PartitionHelper<String> partitionedTeams) {
    protected void onPostExecute(List<List<String>> partitionedTeams) {
        StringBuilder resultString = new StringBuilder();
        int count = 0;
        for (List<String> seriesItem: partitionedTeams) {
            count ++;
            resultString.append(context.getString(R.string.text_teams)+ " " +count + " :" + "  ");
            resultString.append(TextUtils.join(", ", seriesItem) + "\n");
        }
        editText.setText(resultString.toString());
        progressBarDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
