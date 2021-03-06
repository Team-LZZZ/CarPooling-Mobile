package edu.wpi.cs528.lzzz.carpooling_mobile.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import edu.wpi.cs528.lzzz.carpooling_mobile.connection.ConnectionAsyncTask;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpRequestMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpResponseMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.IConnectionAsyncTaskDelegate;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.CarPool;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonExceptionMessages;

/**
 * Created by tonggezhu on 12/13/17.
 */

public class CancelResHandler implements IConnectionAsyncTaskDelegate {

    private final String TAG = "CancelResHandler";
    private IConnectionStatus ConnectionStatus;

    public CancelResHandler(IConnectionStatus connectionStatus){
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
            }

        }catch (Exception ex){
            isSuccessful = false;
            additionalInfos = CommonExceptionMessages.CONNECTION_FAILURE;
        }
        this.ConnectionStatus.onComplete(isSuccessful, additionalInfos);
    }
}
