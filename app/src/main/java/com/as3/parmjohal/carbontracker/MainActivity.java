package com.as3.parmjohal.carbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        try {
            VehicleData v = new VehicleData(this);
        } catch (IOException e) {
            e.printStackTrace();
            

            
        }

        startNewJourney();
    }

    /**
     Call This Method to Start New Journey
     **/
    private void startNewJourney() {
        Intent intent = SelectCarActivity.makeIntent(MainActivity.this);
        startActivity(intent);
    }
}
