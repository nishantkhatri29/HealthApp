package edu.ncuindia.healthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class HeartRateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);

        ImageView iv = findViewById(R.id.my_imageview);
        iv.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse));
    }

    @Override
    public boolean onNavigateUp(){
        finish();
        return true;
    }
}
