package com.as3.parmjohal.carbontracker.Model;

import com.as3.parmjohal.carbontracker.R;

/**
 * A class designed to make a Bike object that holds the distance and name
 * Auto generates a route based on the inputted data
 */

public class Bike extends  Transportation{

    private String name = " ";
    private int distance = 0;
    private Route bikeRoute;
    private int bikeImage = R.drawable.cycle;

    public Bike(String name, int distance) {
        super(0, 0, " ","bike",0) ;

        this.name = name;
        this.distance = distance;
        setupSuperClass();
        bikeRoute = new Route(distance,0,"Bike Trip: "+name);
    }

    private void setupSuperClass() {
        super.setFuelType("Bike");
        super.setCityFuel(0);
        super.setHighwayFuel(0);
        super.setImage(bikeImage);
    }
    public Route getRoute()
    {
        return bikeRoute;
    }
}
