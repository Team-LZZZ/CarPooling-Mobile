package edu.wpi.cs528.lzzz.carpooling_mobile.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by QZhao on 11/1/2017.
 */

public class CarPool {

    private Offerer offerer;
    private List<Reserver> reserverList = new ArrayList<>();
    private Location startLocation;
    private Location targetLocation;
    private Car car;
    private Date date;
    private long time;

    public Offerer getOfferer() {
        return offerer;
    }

    public void setOfferer(Offerer offerer) {
        this.offerer = offerer;
    }

    public List<Reserver> getReserverList() {
        return reserverList;
    }

    public void setReserverList(List<Reserver> reserverList) {
        this.reserverList = reserverList;
    }

    public void addToReserverList(Reserver reserver){
        this.reserverList.add(reserver);
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(Location targetLocation) {
        this.targetLocation = targetLocation;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
