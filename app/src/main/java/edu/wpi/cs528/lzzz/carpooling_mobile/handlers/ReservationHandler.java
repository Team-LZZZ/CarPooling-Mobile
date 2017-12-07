package edu.wpi.cs528.lzzz.carpooling_mobile.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs528.lzzz.carpooling_mobile.connection.ConnectionAsyncTask;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpRequestMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpResponseMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.IConnectionAsyncTaskDelegate;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonExceptionMessages;

/**
 * Created by QZhao on 12/6/2017.
 */

public class ReservationHandler implements IConnectionAsyncTaskDelegate {
    private final String TAG = "ReservationHandler";
    private IConnectionStatus ConnectionStatus;

    public ReservationHandler(IConnectionStatus connectionStatus){
        this.ConnectionStatus = connectionStatus;
    }

    public void connectForResponse(HttpRequestMessage requestMessage){
        new ConnectionAsyncTask(this).execute(requestMessage);
    }

    @Override
    public void processResult(HttpResponseMessage response) {
        boolean isSuccessful = false;
        String additionalInfos = "";
        Gson gson = new GsonBuilder().create();
        try {
            ResponseMessage responseMessage = gson.fromJson(response.getContent(), ResponseMessage.class);
            isSuccessful = responseMessage.isStatus();
            if(!isSuccessful){
                if(responseMessage.getMessage().size() > 0){
                    additionalInfos = responseMessage.getMessage().get(0);
                }
            }
        }catch (Exception ex){
            isSuccessful = false;
            additionalInfos = CommonExceptionMessages.CONNECTION_FAILURE;
        }

        this.ConnectionStatus.onComplete(isSuccessful, additionalInfos);
    }
}
