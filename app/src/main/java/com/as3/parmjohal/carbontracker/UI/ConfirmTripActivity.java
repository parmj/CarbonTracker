package com.as3.parmjohal.carbontracker.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.as3.parmjohal.carbontracker.Model.Journey;
import com.as3.parmjohal.carbontracker.Model.Car;
import com.as3.parmjohal.carbontracker.Model.CarbonTrackerModel;
import com.as3.parmjohal.carbontracker.R;
import com.as3.parmjohal.carbontracker.Model.Route;
import com.as3.parmjohal.carbontracker.SharedPreference;

public class ConfirmTripActivity extends AppCompatActivity {

    CarbonTrackerModel model;
    private Journey journey;

    public static final int REQUEST_CODE_CAR= 2017;
    public static final int REQUEST_CODE_ROUTE= 2018;
    public static final int REQUEST_CODE_DATE= 2019;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_trip);

        model = CarbonTrackerModel.getCarbonTrackerModel(this);

        if(model.isConfirmTrip()) {
            setTitle("Confirm Trip");

        }
        else {
            setTitle("Journey Data");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getJourneyData();

        populateTextViews();

        // if a menu option was selected at dashboard:
        int menu_select_id = getIntent().getIntExtra("menu_select", 0);
        if (menu_select_id != 0) { OptionSelect(menu_select_id); }

    }

    private void populateTextViews(){
        Log.i("TAG", ""+ journey.toString());
        setupTextView(R.id.display_CO2, String.format("%.2f", journey.getCo2()));
        setupTextView(R.id.display_CO2Units, "kg of CO₂");
        setupTextView(R.id.date, "On " + journey.getDateInfo());
        //setupTextView(R.id.display_CarName, journey.getCar().getName());
        setupTextView(R.id.display_MainCar, journey.getTransportation().getInfo());
        setupTextView(R.id.display_RouteName, journey.getRoute().getRouteName());
        setupTextView(R.id.display_Route, journey.getRouteInfo());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreference.saveCurrentModel(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if(model.isConfirmTrip()) {
            getMenuInflater().inflate(R.menu.menu_activity_confirm_decline, menu);
            return true;
        }
        else{
            getMenuInflater().inflate(R.menu.menu_activity_journey, menu);
            return true;
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        OptionSelect(item.getItemId());
        return true;

    }

    public void OptionSelect(int id) {
        switch (id) {
            case android.R.id.home:

                finish();
                break;
            case R.id.action_confirm:

                //Add code to add journey to collection here
                Log.i("Journey: ", "ADDED");
                addJourney();

                Intent intent = MainActivity.makeIntent(ConfirmTripActivity.this);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//reset activity stack
                model.setCurrentCar(null);
                model.setCurrentRoute(null);
                finish();
                startActivity(intent);//go to dashboard

                break;
            case R.id.editCar_id:
                editCar();
                break;
            case R.id.editRoute_id:
                editRoute();
                break;
            case R.id.edit_date_id:
                editDate();
                break;
            case R.id.delete_id:
                model.getJourneyManager().remove(journey);
                model.setConfirmTrip(true);
                finish();
                break;
            default:
                break;
        }
    }

    private void addJourney() {
        model.getJourneyManager().add(journey);
        model.getDayManager().add(journey);
    }

    private void getJourneyData()
    {
        if(model.isConfirmTrip()) {
            Log.i("Journey: ", "New Journey");
            Car currentCar = model.getCurrentCar();
            Route currentRoute = model.getCurrentRoute();

            Log.i("CO2", currentCar.toString());

            journey = new Journey(currentCar, currentRoute);
        }
        else {
            Log.i("Journey: ", "Clicked Journey");
            journey = model.getCurrentJouney();
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
        }
    }

    private void editCar()
    {
        model.setEditJourney(true);
        Intent intent = SelectCarActivity.makeIntent(ConfirmTripActivity.this);
        startActivityForResult(intent,REQUEST_CODE_CAR);
    }

    private void editRoute(){
        model.setEditJourney(true);
        Intent intent = SelectRouteActivity.makeIntent(ConfirmTripActivity.this);
        startActivityForResult(intent,REQUEST_CODE_ROUTE);
    }

    private void editDate()
    {
        startActivityForResult(new Intent(ConfirmTripActivity.this,CalenderActivity.class),REQUEST_CODE_DATE);
    }

    private void setupTextView(int id, String displayString)
    {
        TextView textView = (TextView) findViewById(id);
        textView.setText(displayString);
    }


    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, ConfirmTripActivity.class);
        return intent;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case (REQUEST_CODE_CAR):
                if (resultCode == Activity.RESULT_OK) {
                    journey.setCar(model.getCurrentCar());
                    journey.calculateCO2();
                    model.setCurrentCar(null);
                    model.setEditJourney(false);

                    restart();

                    break;


                }
            case (REQUEST_CODE_ROUTE):
                if(resultCode == Activity.RESULT_OK){
                    journey.setRoute(model.getCurrentRoute());
                    journey.calculateCO2();
                    model.setCurrentRoute(null);
                    model.setEditJourney(false);
                    restart();

                    break;

                }
            case (REQUEST_CODE_DATE):
                if (resultCode == Activity.RESULT_OK) {
                    restart();

                    break;


                }
        }


    }
    private void restart()
    {
        populateTextViews();
    }
}
