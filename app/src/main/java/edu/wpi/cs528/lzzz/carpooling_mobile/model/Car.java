package edu.wpi.cs528.lzzz.carpooling_mobile.model;

/**
 * Created by QZhao on 11/1/2017.
 */

public class Car {

    private String make;
    private String model;
    private String plate;
    private int seatsLimit;

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public int getSeatsLimit() {
        return seatsLimit;
    }

    public void setSeatsLimit(int seatsLimit) {
        this.seatsLimit = seatsLimit;
    }
}
