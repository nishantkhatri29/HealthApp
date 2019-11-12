package edu.ncuindia.healthapp.ui.home;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;

import edu.ncuindia.healthapp.Database.MyDatabase;
import edu.ncuindia.healthapp.Database.UserModel;
import edu.ncuindia.healthapp.HeartRateActivity;
import edu.ncuindia.healthapp.MainActivity;
import edu.ncuindia.healthapp.R;
import edu.ncuindia.healthapp.ui.dashboard.DashboardFragment;

import static android.content.Context.SENSOR_SERVICE;

public class HomeFragment extends Fragment implements SensorEventListener, StepListener{

    private HomeViewModel homeViewModel;
    private Context context;
    public TextView textView;
    private SimpleStepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps = 0;
    int stepsGoal = 0;
    private ProgressBar pieChart;
    private TextView info;
    private MyDatabase database;
    private static final String DATABASE_NAME = "MyDatabase";
    private MaterialButton btn;
    private TextView bmiResult;
    private ImageView imageView;
    private View root;
    private GoogleSignInAccount acct;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.context = view.getContext();
        acct = GoogleSignIn.getLastSignedInAccount(this.context);
        if (acct != null) {
            String personName = acct.getDisplayName();
        } else {
            Toast.makeText(this.context, "You are not authorized!", Toast.LENGTH_LONG).show();
        }
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        database = Room.databaseBuilder(getActivity().getApplication(), MyDatabase.class, DATABASE_NAME).build();
        btn = root.findViewById(R.id.addDetails);
        bmiResult = root.findViewById(R.id.bmiResult);
        imageView = root.findViewById(R.id.imageView2);
        new myTask().execute();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomNavigationView bottomNavigationView = root.findViewById(R.id.nav_view);
                bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
            }
        });

        MaterialButton heartRate = root.findViewById(R.id.heartRate);
        heartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), HeartRateActivity.class);
                startActivity(intent);
            }
        });

        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new SimpleStepDetector();
        simpleStepDetector.registerListener(this);
        textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(TEXT_NUM_STEPS + numSteps);
            }
        });
        info = root.findViewById(R.id.number_of_steps);
        info.setText(String.valueOf(numSteps)+"/"+String.valueOf(stepsGoal));
        pieChart = root.findViewById(R.id.stats_progressbar);
        double d = (double) numSteps / (double) stepsGoal;
        int progress = (int) (d * 100);
        pieChart.setProgress(progress);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        numSteps = 0;
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(TEXT_NUM_STEPS + numSteps);
            }
        });
        info.setText(String.valueOf(numSteps)+"/"+String.valueOf(stepsGoal));
        double d = (double) numSteps / (double) stepsGoal;
        int progress = (int) (d * 100);
        pieChart.setProgress(progress);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(TEXT_NUM_STEPS + numSteps);
            }
        });
        info.setText(String.valueOf(numSteps)+"/"+String.valueOf(stepsGoal));
        double d = (double) numSteps / (double) stepsGoal;
        int progress = (int) (d * 100);
        pieChart.setProgress(progress);
    }

    private class myTask extends AsyncTask<Void, Void, Void> {
        UserModel result;
        protected Void doInBackground(Void... params) {
            result = database.userDao().findByEmail(acct.getEmail());
            return null;
        }

        @Override
        protected void onPostExecute(Void x) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(result != null) {
                        stepsGoal = result.getStepGoal();
                        btn.setVisibility(View.GONE);
                        bmiResult.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        info.setText(String.valueOf(numSteps)+"/"+String.valueOf(stepsGoal));
                        TextView bmiResult = root.findViewById(R.id.bmiResult);
                        double bmi = result.getWeight() / (Math.pow((result.getHeight() /100),2));
                        DecimalFormat df = new DecimalFormat("#.#");
                        bmiResult.setText(df.format(bmi));
                    }
                    else{
                        btn.setVisibility(View.VISIBLE);
                        bmiResult.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

}