package edu.wpi.cs528.lzzz.carpooling_mobile.handlers;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import butterknife.Bind;
import edu.wpi.cs528.lzzz.carpooling_mobile.MainActivity;
import edu.wpi.cs528.lzzz.carpooling_mobile.R;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.ConnectionAsyncTask;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpRequestMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpResponseMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.IConnectionAsyncTaskDelegate;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.User;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;

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
        Log.i(CommonConstants.LogPrefix, response.getStatus() + "    " + response.getContent());
    }
}
