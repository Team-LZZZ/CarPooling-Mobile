package edu.wpi.cs528.lzzz.carpooling_mobile.connection;

import android.os.AsyncTask;

/**
 * Created by QQZhao on 10/28/17.
 */

public class ConnectionAsyncTask extends AsyncTask<HttpRequestMessage, Void, HttpResponseMessage> {

    private IConnectionAsyncTaskDelegate delegate = null;

    public ConnectionAsyncTask(IConnectionAsyncTaskDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    protected HttpResponseMessage doInBackground(HttpRequestMessage... params) {
        HttpRequestMessage httpRequest = params[0];
        return BaseConnection.connect(httpRequest);
    }

    @Override
    protected void onPostExecute(HttpResponseMessage httpResponseMessage) {
        super.onPostExecute(httpResponseMessage);
        delegate.processResult(httpResponseMessage);
    }
}
