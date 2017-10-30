package edu.wpi.cs528.lzzz.carpooling_mobile.handlers;

import android.app.Activity;
import android.widget.TextView;

import edu.wpi.cs528.lzzz.carpooling_mobile.R;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.ConnectionAsyncTask;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.IConnectionAsyncTaskDelegate;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpRequestMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpResponseMessage;

/**
 * Created by QQZhao on 10/28/17.
 */

public class ConnectionHandler implements IConnectionAsyncTaskDelegate {

    private Activity activity;

    public ConnectionHandler(Activity activity){
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
        TextView textView = (TextView) this.activity.findViewById(R.id.textView);
        textView.setText(response.getContent());
    }
}
