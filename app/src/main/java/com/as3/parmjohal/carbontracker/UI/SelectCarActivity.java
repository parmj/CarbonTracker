package com.as3.parmjohal.carbontracker.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.as3.parmjohal.carbontracker.Model.Bike;
import com.as3.parmjohal.carbontracker.Model.Bus;
import com.as3.parmjohal.carbontracker.Model.Car;
import com.as3.parmjohal.carbontracker.Model.CarbonTrackerModel;
import com.as3.parmjohal.carbontracker.Model.Route;
import com.as3.parmjohal.carbontracker.Model.Skytrain;
import com.as3.parmjohal.carbontracker.Model.Transportation;
import com.as3.parmjohal.carbontracker.Model.Walk;
import com.as3.parmjohal.carbontracker.R;
import com.as3.parmjohal.carbontracker.SharedPreference;

import java.util.ArrayList;
/**
 * --SelectCarActivity--
 * Shows a listview of existing cars that user
 * can select to create a journey
 */
public class SelectCarActivity extends AppCompatActivity {

    CarbonTrackerModel model;

    ArrayList<Car> carList = new ArrayList<Car>();
    public static final int REQUEST_CODE_ADD= 1024;
    public static final int REQUEST_CODE_EDIT= 1025;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_car);
        model = CarbonTrackerModel.getCarbonTrackerModel(this);
        if(model.isEditJourney()){
            setTitle(getString(R.string.Edit_Journeys_Car));
        }
        else {
            setTitle(getString(R.string.select_car));
        }

        populateListView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void populateListView() {

        carList = model.getCarManager().getCarCollection();

        ArrayAdapter<Car> adapter = new SelectCarActivity.MyListAdaptder();
        ListView list = (ListView) findViewById(R.id.carListView);
        list.setAdapter(adapter);
        registerForContextMenu(list);
    }


    /**
     * Sets up the complex Listview
     */
    private class MyListAdaptder extends ArrayAdapter<Car> {
        public MyListAdaptder() {
            super(SelectCarActivity.this, R.layout.car_list_view_select, carList);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View itemView = convertView;

            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.car_list_view_select, parent, false);
            }


            Car thisCar = carList.get(position);

            TextView carName = (TextView) itemView.findViewById(R.id.carName);
            carName.setText(thisCar.getName());

            ImageView car = (ImageView) itemView.findViewById(R.id.carImage);
            car.setImageDrawable(getDrawable(thisCar.getCarImage()));

            TextView description = (TextView) itemView.findViewById(R.id.carDescription);
            description.setText(thisCar.getMake() + ", " + thisCar.getModel() + ", " + thisCar.getYear());

            TextView description2 = (TextView) itemView.findViewById(R.id.carDescription2);
            description2.setText(thisCar.getTranyType() + ", " + thisCar.getFuelType() + getString(R.string.fuel)+", "
                    +thisCar.getEngineDisplacment()+"L");//fill


            // on track/item click
            CardView track = (CardView) itemView.findViewById(R.id.track);
            track.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.setCurrentTransportation(carList.get(position));
                    if (model.isEditJourney()){
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        Transportation test = model.getCurrentJouney().getTransportation();
                        if(test instanceof Skytrain == true ||test instanceof Bus == true || test instanceof Walk== true ||
                                test instanceof Bike == true ){
                            Intent intent2 = SelectRouteActivity.makeIntent(SelectCarActivity.this);
                            startActivity(intent2);
                        }
                        finish();
                    }
                    else {
                        Intent intent = SelectRouteActivity.makeIntent(SelectCarActivity.this);
                        startActivity(intent);
                    }
                }
            });

            // on overflow click
            ImageButton overflow = (ImageButton) itemView.findViewById(R.id.overflow);
            overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(SelectCarActivity.this, v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.edit_delete_context, popup.getMenu());
                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            ListView clicklist = (ListView) findViewById(R.id.carListView);
                            Car clickedCar = (Car) clicklist.getItemAtPosition(position);

                            if(item.getItemId() == R.id.delete_id)
                            {
                                //do stuff if the delete button is clicked...
                                Toast.makeText(SelectCarActivity.this, R.string.DELECTED, Toast.LENGTH_SHORT).show();
                                model.getCarManager().remove(clickedCar);
                                restart();
                            }
                            else if(item.getItemId() == R.id.edit_id)
                            {

                                model.setCurrentCar(clickedCar);
                                Intent intent2 = EditCarActivity.makeIntent(SelectCarActivity.this);
                                startActivityForResult(intent2,REQUEST_CODE_EDIT);

                            }
                            return true;
                        }
                    });

                }
            });


            return itemView;
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case (REQUEST_CODE_ADD):
                if (resultCode == Activity.RESULT_OK) {
                    if(model.isEditJourney()){
                        populateListView();
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        if(model.getCurrentJouney().getTransportation() instanceof Car == false){
                            Intent intent2 = SelectRouteActivity.makeIntent(SelectCarActivity.this);
                            startActivity(intent2);
                        }
                        finish();
                        break;
                    }
                    else {
                        Intent intent = SelectRouteActivity.makeIntent(SelectCarActivity.this);
                        startActivity(intent);
                        populateListView();
                        break;
                    }


                }
            case (REQUEST_CODE_EDIT):
                if(resultCode == Activity.RESULT_OK){
                    populateListView();
                    break;

                }
        }

    }


    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, SelectCarActivity.class);
        return intent;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_car_select, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add:
                Intent intent = AddCarActivity.makeIntent(SelectCarActivity.this);
                startActivityForResult(intent,REQUEST_CODE_ADD);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreference.saveCurrentModel(this);
    }

    private void restart()
    {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}

