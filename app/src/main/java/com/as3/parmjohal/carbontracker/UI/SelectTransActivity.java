package com.as3.parmjohal.carbontracker.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.as3.parmjohal.carbontracker.Model.CarbonTrackerModel;
import com.as3.parmjohal.carbontracker.R;

public class SelectTransActivity extends AppCompatActivity {
    CarbonTrackerModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_trans);

        model = CarbonTrackerModel.getCarbonTrackerModel(this);

        setTitle("Select Transportation");

        setupButtons();
    }

    private void setupButtons() {
        Button car = (Button) findViewById(R.id.carButton);
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SelectCarActivity.makeIntent(SelectTransActivity.this);
                startActivity(intent);
                if(model.isEditJourney()){
                    Intent intent2 = new Intent();
                    setResult(Activity.RESULT_OK, intent); //incase user backs instead of finishing
                    model.setCurrentTransportation(model.getCurrentJouney().getTransportation());
                    model.setCurrentRoute(model.getCurrentJouney().getRoute());

                    finish();
                }
            }
        });
        Button bus = (Button) findViewById(R.id.busButton);
        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = BusActivity.makeIntent(SelectTransActivity.this);
                startActivity(intent);
                if(model.isEditJourney()){
                    Intent intent2 = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    //incase user backs instead of finishing
                    model.setCurrentTransportation(model.getCurrentJouney().getTransportation());
                    model.setCurrentRoute(model.getCurrentJouney().getRoute());
                    finish();
                }

            }
        });
        Button skytrain = (Button) findViewById(R.id.trainButton);
        skytrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TrainActivity.makeIntent(SelectTransActivity.this);
                startActivity(intent);
                if(model.isEditJourney()) {
                    Intent intent2 = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    //incase user backs instead of finishing
                    model.setCurrentTransportation(model.getCurrentJouney().getTransportation());
                    model.setCurrentRoute(model.getCurrentJouney().getRoute());
                    finish();

                }


            }
        });
        Button walk= (Button) findViewById(R.id.walkButton);
        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = WalkActivity.makeIntent(SelectTransActivity.this);
                startActivity(intent);
                if(model.isEditJourney()){
                    Intent intent2 = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    //incase user backs instead of finishing
                    model.setCurrentTransportation(model.getCurrentJouney().getTransportation());
                    model.setCurrentRoute(model.getCurrentJouney().getRoute());
                    finish();
                }

            }
        });
    }
    public static Intent makeIntent(Context context) {
        Intent intent= new Intent(context, SelectTransActivity.class);
        return intent;
    }
}
