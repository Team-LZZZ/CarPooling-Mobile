package edu.wpi.cs528.lzzz.carpooling_mobile.handlers;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;

import edu.wpi.cs528.lzzz.carpooling_mobile.connection.ConnectionAsyncTask;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpRequestMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpResponseMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.IConnectionAsyncTaskDelegate;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonExceptionMessages;


public class LogInHandler implements IConnectionAsyncTaskDelegate {

    private Activity activity;


    public LogInHandler(Activity activity){
        this.activity = activity;
    }

    public void connectForResponse(HttpRequestMessage requestMessage){
        new ConnectionAsyncTask(this).execute(requestMessage);
    }

    @Override
    public void processResult(HttpResponseMessage response) {
        // handle response message code here
        // update GUI
        // update model
        Gson gson = new Gson();
        try {
            ResponseMessage responseMessage = gson.fromJson(response.getContent(),ResponseMessage.class);
            CommonConstants.isLogIn = responseMessage.isStatus();
            CommonExceptionMessages.LOGIN_FAILURE = responseMessage.getMessage()[0];
        }catch (Exception ex){
            CommonExceptionMessages.LOGIN_FAILURE = "Can not connect to server";
        }

    }
}
