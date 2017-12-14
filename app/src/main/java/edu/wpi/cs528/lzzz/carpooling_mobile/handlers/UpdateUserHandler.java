package edu.wpi.cs528.lzzz.carpooling_mobile.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs528.lzzz.carpooling_mobile.connection.ConnectionAsyncTask;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpRequestMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpResponseMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.IConnectionAsyncTaskDelegate;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonExceptionMessages;


public class UpdateUserHandler implements IConnectionAsyncTaskDelegate {

    private final String TAG = "UpdateUserHandler";
    private IConnectionStatus ConnectionStatus;

    public UpdateUserHandler(IConnectionStatus connectionStatus){
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
        this.ConnectionStatus.onComplete(isSuccessful, additionalInfos);
    }
}
