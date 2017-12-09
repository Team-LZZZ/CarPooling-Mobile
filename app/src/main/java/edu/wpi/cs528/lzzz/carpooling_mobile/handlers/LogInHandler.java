package edu.wpi.cs528.lzzz.carpooling_mobile.handlers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs528.lzzz.carpooling_mobile.connection.ConnectionAsyncTask;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpRequestMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpResponseMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.IConnectionAsyncTaskDelegate;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.User;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonExceptionMessages;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonUtils;


public class LogInHandler implements IConnectionAsyncTaskDelegate {
    private final String LoginTAG = "LogInHandler";
    private IConnectionStatus ConnectionStatus;

    public LogInHandler(IConnectionStatus connectionStatus){
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
                if(responseMessage.getMessage().size() > 0){
                    additionalInfos = responseMessage.getMessage().get(0);
                }
            }else{
                Log.i(LoginTAG + " token: ", responseMessage.getToken());
                AppContainer.getInstance().setToken(responseMessage.getToken());
            }
        }catch (Exception ex){
            isSuccessful = false;
            additionalInfos = CommonExceptionMessages.CONNECTION_FAILURE;
        }
        AppContainer.getInstance().setLogIn(isSuccessful);
        this.ConnectionStatus.onComplete(isSuccessful, additionalInfos);
    }
}
