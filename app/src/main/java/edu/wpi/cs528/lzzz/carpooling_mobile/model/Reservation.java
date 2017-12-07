package edu.wpi.cs528.lzzz.carpooling_mobile.model;

/**
 * Created by QZhao on 11/1/2017.
 */

public class Reservation {
    private long carPoolId;
    private int newReservedSeatsNumber;

    public long getCarPoolId() {
        return carPoolId;
    }

    public void setCarPoolId(long carPoolId) {
        this.carPoolId = carPoolId;
    }

    public int getNewReservedSeatsNumber() {
        return newReservedSeatsNumber;
    }

    public void setNewReservedSeatsNumber(int newReservedSeatsNumber) {
        this.newReservedSeatsNumber = newReservedSeatsNumber;
    }
}
