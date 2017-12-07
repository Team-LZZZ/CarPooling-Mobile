package edu.wpi.cs528.lzzz.carpooling_mobile.handlers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liweihao on 12/4/17.
 */

public class ResponseMessage {
    public List<String> message;
    public boolean status;
    public String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
