package edu.wpi.cs528.lzzz.carpooling_mobile.handlers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liweihao on 12/4/17.
 */

public class ResponseMessage {
    public String[] message;
    @SerializedName(value = "register_status", alternate = {"login_status"})
    public boolean status;


    public String[] getMessage() {
        return message;
    }

    public void setMessage(String[] message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
