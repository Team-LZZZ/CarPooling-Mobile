package edu.wpi.cs528.lzzz.carpooling_mobile.handlers;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.FileReader;
import java.util.HashMap;

import butterknife.Bind;
import edu.wpi.cs528.lzzz.carpooling_mobile.MainActivity;
import edu.wpi.cs528.lzzz.carpooling_mobile.R;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.ConnectionAsyncTask;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpRequestMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpResponseMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.IConnectionAsyncTaskDelegate;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.User;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonExceptionMessages;

/**
 * Created by QQZhao on 10/28/17.
 */

public class SignUpHandler implements IConnectionAsyncTaskDelegate {

    private Activity activity;


    public SignUpHandler(Activity activity){
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
        try {
            Gson gson = new Gson();
            ResponseMessage responseMessage = gson.fromJson(response.getContent(),ResponseMessage.class);
            CommonConstants.isSignUp = responseMessage.isStatus();
            StringBuilder sb = new StringBuilder();
            for (String s : responseMessage.getMessage()){
                sb.append(" " + s);
            }
            CommonExceptionMessages.REGISTER_FAILURE = sb.toString();
        }catch (Exception ex){
            CommonExceptionMessages.REGISTER_FAILURE = "Can not connect to server";
        }
    }
}
