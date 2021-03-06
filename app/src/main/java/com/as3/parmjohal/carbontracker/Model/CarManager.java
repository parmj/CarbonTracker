package com.as3.parmjohal.carbontracker.Model;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 *  Keeps track of all car objects in an arraylist
 *  provides the ability to search for and edit specific cars in one command
 */

public class CarManager {

    //make	model	year	trany	cylinders	displ	fuelType	city08	highway08

    private ArrayList<Car> carCollection = new ArrayList<>();
    private ArrayList<Car> allCars;
    private ArrayList<Journey> journeys;
    private JourneyManager journeyManager;
    private Context context;

    public CarManager()
    {

    }

//    public CarManager(Context context) {
//        VehicleData vehicleData = null;
//        this.context = context;
//
//        try {
//            vehicleData = new VehicleData(context);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        allCars = vehicleData.getAllCars();
//    }

    public Car add(Car car)
    {
        carCollection.add(car);
        return car;
    }

    public Car edit(Car car) //same as add in looking for car from CSV file, but don't add to collection
    {
       return car;
    }

    private Car getCarFromCSVFile(Car car)
    {
        for(int i = 0; i < allCars.size(); i++)
        {
            if(allCars.get(i).equals(car))
            {
                Log.i("ADDED ", allCars.get(i).toString());
                return allCars.get(i);
            }
        }
        return null;
    }

    public void remove(Car car)
    {
        carCollection.remove(car);
    }

    public void update(Car updateCar, Car newCar)
    {
        Car car = getCarFromArray(updateCar);
        Log.i("Before ", car.toString());

        car.setMake(newCar.getMake());
        car.setModel(newCar.getModel());
        car.setYear(newCar.getYear());
        car.setTranyType(newCar.getTranyType());
        car.setEngineDisplacment(newCar.getEngineDisplacment());
        Log.i("After ", car.toString());
    }

    private int getIndex(Car car)
    {
        int index = carCollection.indexOf(car);
        return index;
    }

    private Car getCarFromArray(Car getCar)
    {
        int index = getIndex(getCar);
        Car car = carCollection.get(index);

        return car;
    }

    public ArrayList<Car> getCarCollection() {
        return ( ArrayList<Car>) carCollection.clone();
    }

}