package com.randomapps.randomapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.randomapps.randomapp.R;

import java.util.ArrayList;

import random.RandomLibrary;

/**
 * A simple {@link Fragment} subclass.
 */
//public class DiceFragment extends Fragment implements SensorEventListener {
public class DiceFragment extends Fragment {

    ////////////Shake handler/////////////////
    // variables for shake detection
    /*private static final float SHAKE_THRESHOLD = 3.25f; // m/S**2
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 500;
    private long mLastShakeTime;
    private SensorManager mSensorMgr;*/
    ////////////Shake handler - end/////////////////

    //Random Library
    RandomLibrary randomLib;
    View root;

    //Random Colors UI elements
    LinearLayout diceLayout;
    Button butRollTt;
    Spinner howManyDiceSpinner;
    TextView diceResultsTextView, diceResultsTotalTextView;

    public DiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Random Library instantiation
        randomLib = new RandomLibrary();
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_dice, container, false);

        //Random Dice UI handling
        InitializeDiceGeneratorUIElements(root);

        //InitializeShakeSensorEventListner();

        return root;
    }

    ////////////Shake handler/////////////////
    /*private void InitializeShakeSensorEventListner() {
        // Get a sensor manager to listen for shakes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mSensorMgr = (SensorManager) this.getActivity().getSystemService(SENSOR_SERVICE);

            // Listen for shakes
            Sensor accelerometer = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                mSensorMgr.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }*/
    ////////////Shake handler-end/////////////////

    private void InitializeDiceGeneratorUIElements(View root) {
        howManyDiceSpinner = root.findViewById(R.id.howManyDiceSpinner);
        howManyDiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UpdateDiceLayout(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        diceResultsTextView = root.findViewById(R.id.diceResultsTextView);
        diceResultsTotalTextView = root.findViewById(R.id.diceResultsTotalTextView);
        diceLayout = root.findViewById(R.id.diceLayout);
        butRollTt = root.findViewById(R.id.butRollit);
        butRollTt.setOnClickListener(v -> DiceRollIt(v));
    }

    private void DiceRollIt(View v) {
        diceLayout.removeAllViews();
        int diceCount = howManyDiceSpinner.getSelectedItemPosition() + 1;
        UpdateDiceLayout(diceCount);
    }

    private void UpdateDiceLayout(int diceCount) {
        ArrayList<ImageView> imageViews = new ArrayList<>();

        diceLayout.removeAllViews();
        int diceWidth = (diceLayout.getWidth() - 300)/3;
        int rowCount = diceCount % 3 == 0? diceCount/3 : diceCount/3 + 1;
        ArrayList<Integer> diceResults = new ArrayList<>();
        int diceCreationCount = diceCount;
        for (int i = 0; i < rowCount; i++){
            int count = 0;
            LinearLayout diceRow = new LinearLayout(this.getActivity());
            diceRow.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1.0f;
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
            layoutParams.setMargins(20, 20, 20, 20);
            diceRow.setLayoutParams(layoutParams);

            while(count < 3 && diceCreationCount > 0){
                int randomDicer = randomLib.randomObject.nextInt(1,7);
                diceResults.add(randomDicer);
                String drawableName = "dice_" + randomDicer;
                int drawableID = getResources().getIdentifier(drawableName, "drawable", this.getActivity().getPackageName());
                ImageView imageView = new ImageView(this.getActivity());
                imageView.setImageResource(drawableID);
                LinearLayout.LayoutParams ImagelayoutParams = new LinearLayout.LayoutParams(diceWidth, diceWidth);
                ImagelayoutParams.setMargins(10, 10, 10, 10);
                imageView.setLayoutParams(ImagelayoutParams);
                diceRow.addView(imageView);

                //Check Animation
                imageViews.add(imageView);

                count++;
                diceCreationCount--;
            }
            diceLayout.addView(diceRow);
        }
        String resultText = TextUtils.join(" + ", diceResults);
        diceResultsTextView.setText(resultText);
        diceResultsTotalTextView.setText(getString(R.string.result_total_msg, getSum(diceResults)));
        StartImageViewsAnimations(imageViews);
    }

    private void StartImageViewsAnimations(ArrayList<ImageView> imageViews) {
        Animation rotateAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotation);
        for (ImageView i : imageViews) {
            rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            i.startAnimation(rotateAnimation);
        }
    }

    public int getSum(ArrayList<Integer> list){
        int sum = 0;
        for (Integer e : list) sum += e;
        return sum;
    }

    ////////////Shake handler/////////////////
    /*@Override
    public void onSensorChanged(SensorEvent event) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        //Boolean enableShakePref = sharedPreferences.getBoolean(AppStrings.SETTINGS_ENABLE_SHAKE_KEY, false);
        String enableShakePref = sharedPreferences.getString(AppStrings.SETTINGS_ENABLE_SHAKE_KEY, "false");
        if(enableShakePref.equals("true")){
            //SetApplocaleSettings(null);
        } else {
            //SetApplocaleSettings("kn");
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                double acceleration = Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;
                //Log.d("Dice", "Acceleration is " + acceleration + "m/s^2");

                if (acceleration > SHAKE_THRESHOLD) {
                    mLastShakeTime = curTime;
                    int diceCount = howManyDiceSpinner.getSelectedItemPosition() + 1;
                    UpdateDiceLayout(diceCount);
                    //Log.d("Dice", "Shake, Rattle, and Roll");
                }
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }*/
    ////////////Shake handler - end/////////////////
}
