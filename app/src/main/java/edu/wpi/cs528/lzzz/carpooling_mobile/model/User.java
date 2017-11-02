package edu.wpi.cs528.lzzz.carpooling_mobile.model;

import android.graphics.Bitmap;

/**
 * Created by QQZhao on 10/28/17.
 */

public class User {

    private String username;
    private Bitmap photo;
    private int phone;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
}
