package edu.wpi.cs528.lzzz.carpooling_mobile.model;

import java.util.Date;

/**
 * Created by QZhao on 11/1/2017.
 */

public class Query {

    private Date date;
    private long time;
    private Location startLocation;
    private Location targetLocation;

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
}
