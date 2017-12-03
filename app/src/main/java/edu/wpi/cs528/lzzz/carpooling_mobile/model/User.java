package edu.wpi.cs528.lzzz.carpooling_mobile.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import edu.wpi.cs528.lzzz.carpooling_mobile.R;

/**
 * Created by QQZhao on 10/28/17.
 */

public class User {

    private String name;
//    private Bitmap photo;
    private String phone;
    private String email;
    private String password;
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String username) {
        this.name = username;
    }

//    public Bitmap getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(Bitmap photo) {
//        this.photo = photo;
//    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
