package edu.wpi.cs528.lzzz.carpooling_mobile.handlers;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.User;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonExceptionMessages;

/**
 * Created by QQZhao on 10/28/17.
 */

public class SignUpHandler implements IConnectionAsyncTaskDelegate {

    private final String TAG = "SignUpHandler";
    private IConnectionStatus ConnectionStatus;

    public SignUpHandler(IConnectionStatus connectionStatus){
        this.ConnectionStatus = connectionStatus;
    }

    public void connectForResponse(HttpRequestMessage requestMessage){
        new ConnectionAsyncTask(this).execute(requestMessage);
    }

    @Override
    public void processResult(HttpResponseMessage response) {
        Gson gson = new GsonBuilder().create();
        boolean isSuccessful = false;
        String additionalInfos = "";

        try {
            ResponseMessage responseMessage = gson.fromJson(response.getContent(), ResponseMessage.class);
            isSuccessful = responseMessage.isStatus();
            if(!isSuccessful){
                StringBuilder sb = new StringBuilder();
                for (String s : responseMessage.getMessage()){
                    sb.append(" " + s);
                }
                additionalInfos = sb.toString();
            }
        }catch (Exception ex){
            isSuccessful = false;
            additionalInfos = CommonExceptionMessages.CONNECTION_FAILURE;
        }
        AppContainer.getInstance().setLogIn(isSuccessful);
        this.ConnectionStatus.onComplete(isSuccessful, additionalInfos);
    }
}
