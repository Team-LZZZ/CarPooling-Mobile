package edu.wpi.cs528.lzzz.carpooling_mobile.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QZhao on 12/6/2017.
 */

public class AppContainer {

    private static AppContainer instance = null;
    private String token = "";
    private boolean isLogIn = false;
    private User activeUser = new User();
    private static List<CarPool> carPools = new ArrayList<>();

    public static AppContainer getInstance(){
        if(instance == null){
            instance = new AppContainer();
        }
        return instance;
    }

    static {
        CarPool carPool = new CarPool();
        Location location = new Location("WPI");
        location.setLatLng(new LatLng(42.274409, -71.808752));
        carPool.setStartLocation(location);
        carPool.setOid(111);
        carPool.setTargetLocation(location);
        carPool.setAvailable(4);
        carPools.add(carPool);
    }

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<CarPool> getCarPools() {
        return carPools;
    }

    public void setCarPools(List<CarPool> carPools) {
        this.carPools = carPools;
    }

    public boolean isLogIn() {
        return isLogIn;
    }

    public void setLogIn(boolean logIn) {
        isLogIn = logIn;
    }
}
