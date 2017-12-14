package edu.wpi.cs528.lzzz.carpooling_mobile.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by QZhao on 12/6/2017.
 */

public class AppContainer {

    private static AppContainer instance = null;
    private String token = "";
    private boolean isLogIn = false;
    private User activeUser = new User();
    private List<CarPool> carPools = new ArrayList<>();
    private List<CarPool> searchResult = new ArrayList<>();

    public static AppContainer getInstance(){
        if(instance == null){
            instance = new AppContainer();
        }
        return instance;
    }



    public List<CarPool> getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(List<CarPool> searchResult) {
        this.searchResult = searchResult;
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
