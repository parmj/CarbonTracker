package com.as3.parmjohal.carbontracker.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.as3.parmjohal.carbontracker.Model.Car;
import com.as3.parmjohal.carbontracker.Model.CarbonTrackerModel;
import com.as3.parmjohal.carbontracker.R;

import java.util.ArrayList;

/**
 * --EditCarActivity--
 * Edits existing car in carlist and sends it back
 * with the correct info.
 * Proceeds back to SelectCarActivity
 */

public class EditCarActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_IMAGE = 1328;
    private CarbonTrackerModel model;
    private String make;
    private String carModel;
    private Integer year;
    private ArrayList<Car> carList = new ArrayList<>();
    private String carNameString;
    private Car carClicked;
    private boolean carIsClicked = false;
    private EditText editName;
    private Button imageButton;
    private int pos;
    private int image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car);

        model = CarbonTrackerModel.getCarbonTrackerModel(this);
        setTitle(getString(R.string.Edit_Transportation));

        //adjust layout position when keyboard is out
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupMakeSpinner();
        image= model.getCurrentCar().getCarImage();
        setCarImageButton();
        editName = (EditText) findViewById(R.id.editName);
        editName.setText(model.getCurrentCar().getName());
    }
    private void setupMakeSpinner() {

        final Spinner makeSelection = (Spinner) findViewById(R.id.makeSpinner);
        // Spinner Drop down elements
        final ArrayList<String> makeCategories = model.getVehicleData().getCarMakers();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, makeCategories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        makeSelection.setAdapter(dataAdapter);
        //set default selection
        for(int i=0; i<makeCategories.size();i++) {
            if (model.getCurrentCar().getMake().equals(makeCategories.get(i)))
                makeSelection.setSelection(i);
                make = makeCategories.get(i);
        }
        makeSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                make =  makeSelection.getSelectedItem().toString();
                setupModelSpinner();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setupModelSpinner();
            }

        });



    }
    private void setCarImageButton() {
        Button car = (Button) findViewById(R.id.carButton);
        car.setBackground((getDrawable(image)));
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SelectCarImageActivity.makeIntent(EditCarActivity.this);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);

            }
        });
    }


    private void setupModelSpinner() {
        final Spinner modelSelection = (Spinner) findViewById(R.id.modelSpinner);
        // Spinner Drop down elements
        final ArrayList<String> modelCategories = model.getVehicleData().getModels(make);
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, modelCategories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        modelSelection.setAdapter(dataAdapter);
        //set default selection
        for(int i=0; i<modelCategories.size();i++) {
            if (model.getCurrentCar().getModel().equals(modelCategories.get(i)))
                modelSelection.setSelection(i);
                carModel = modelCategories.get(i);
        }
        modelSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carModel =  modelSelection.getSelectedItem().toString();
                setupYearSpinner();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setupYearSpinner();
            }

        });
    }

    private void setupYearSpinner() {
        final Spinner yearSelection = (Spinner) findViewById(R.id.yearSpinner);
        // Spinner Drop down elements
        final ArrayList<Integer> yearCategories = model.getVehicleData().getCarYears(make,carModel);
        // Creating adapter for spinner
        ArrayAdapter<Integer> dataAdapter2 = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, yearCategories);
        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        yearSelection.setAdapter(dataAdapter2);
        for(int i=0; i<yearCategories.size();i++) {
            if (model.getCurrentCar().getYear() == (yearCategories.get(i)))
                yearSelection.setSelection(i);
                year = yearCategories.get(i);
                year = yearCategories.get(i);
        }
        yearSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = (Integer) yearSelection.getSelectedItem();
                setupListView();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setupListView();
            }

        });
    }

    private void setupListView() {

        carList = model.getVehicleData().getPossibleCars(make,carModel,year);
        ArrayAdapter<Car> adapter = new EditCarActivity.MyListAdaptder();
        ListView list = (ListView) findViewById(R.id.carList);
        list.setAdapter(adapter);
    }

    /**
     * Sets up the complex Listview
     */
    private class MyListAdaptder extends ArrayAdapter<Car> {
        public MyListAdaptder() {
            super(EditCarActivity.this, R.layout.car_list_view, carList);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.car_list_view, parent, false);
            }


            Car thisCar = carList.get(position);


            TextView carName = (TextView) itemView.findViewById(R.id.carName);
            carName.setText(thisCar.getMake() + ", " + thisCar.getModel() + ", " + thisCar.getYear());
            TextView description = (TextView) itemView.findViewById(R.id.carDescription);
            TextView description2= (TextView) itemView.findViewById(R.id.carDescription2);
            description2.setText(getString(R.string.Transmission)+": " +thisCar.getTranyType() + ", " + thisCar.getFuelType()
                    + getString(R.string.fuel)+", " + getString(R.string.engindisplacement)+thisCar.getEngineDisplacment()+ "L");
            description.setVisibility(View.GONE);
            registerClickCallBack();


            return itemView;

        }
    }
        private void registerClickCallBack() {
            ListView clicklist = (ListView) findViewById(R.id.carList);
            clicklist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            clicklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                    carClicked = carList.get(position);
                    for(int a = 0; a < parent.getChildCount(); a++)
                    {
                        parent.getChildAt(a).setBackgroundColor(Color.WHITE);
                    }
                    viewClicked.setBackgroundColor(Color.GRAY);

                    carIsClicked = true;




                }
            });
        }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_confirm_decline, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_confirm:
                carNameString = editName.getText().toString();

                if (carNameString.isEmpty()){
                    Toast.makeText(EditCarActivity.this, getString(R.string.please_name_your_car), Toast.LENGTH_SHORT).show();
                    return false;
                }
                else if(!carIsClicked) {
                    Toast.makeText(EditCarActivity.this, getString(R.string.please_select_your_car), Toast.LENGTH_SHORT).show();
                    return false;
                }
               else if(checkDuplicate(carClicked)){
                    Toast.makeText(EditCarActivity.this,  getString(R.string.this_car_already_exist), Toast.LENGTH_SHORT).show();
                    return false;
                }

                else{

                    model.getCurrentCar().setName(carNameString);
                    Car editCar = model.getCarManager().edit(carClicked);
                    model.getCurrentCar().setCarImage(image);
                    model.getCurrentCar().setCarData(editCar);
                    model.getJourneyManager().recalculateCarbon();
                    for (int i = 0; i < model.getCarManager().getCarCollection().size(); i++) {

                        Log.i( getString(R.string.car_collection), model.getCarManager().getCarCollection().get(i).toString());

                    }

                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    return true;
                }


            default:
                return super.onOptionsItemSelected(item);
        }

    }
    private boolean checkDuplicate(Car carClicked) {

        for (int i = 0; i < model.getCarManager().getCarCollection().size(); i++) {
            if (carClicked.equals(model.getCarManager().getCarCollection().get(i)) && model.getCurrentPos() != i
                    && carNameString.equals(model.getCarManager().getCarCollection().get(i).getName())) {
                return true;
            }
        }


        return false;

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case (REQUEST_CODE_IMAGE):
                if (resultCode == Activity.RESULT_OK) {
                    image = data.getIntExtra("image",R.drawable.car1);
                    setCarImageButton();
                    break;
                }


            default:
                break;

        }

    }


    public static Intent makeIntent(Context context) {
            Intent intent = new Intent(context, EditCarActivity.class);
            return intent;

    }
}
