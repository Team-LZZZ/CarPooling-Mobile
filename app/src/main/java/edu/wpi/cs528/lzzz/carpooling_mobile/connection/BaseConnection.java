package edu.wpi.cs528.lzzz.carpooling_mobile.connection;

import android.util.Log;
import android.util.StringBuilderPrinter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonExceptionMessages;


/**
 * Created by QQZhao on 10/28/17.
 */

public class BaseConnection{

    public static HttpResponseMessage connect(HttpRequestMessage httpRequest){
        String TAG = "BaseConnection";
        HttpURLConnection conn = null;
        HttpResponseMessage httpResponse = new HttpResponseMessage();

        try {
            // initialize connection
            URL url = new URL(httpRequest.getUrl());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(httpRequest.getMethod());
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            if (httpRequest.getToken() != null && !httpRequest.getToken().equals("")){
                conn.setRequestProperty("Authorization", "Bearer " + httpRequest.getToken());
            }

            // add params for post method
            if (httpRequest.getMethod().equals("POST") || httpRequest.getMethod().equals("PUT") || httpRequest.getMethod().equals("DELETE")){
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//                writer.write(getPostDataParams(httpRequest.getParams()));
                writer.write(httpRequest.getBody());
                Log.i(CommonConstants.LogPrefix, httpRequest.getBody());
                writer.flush();
                writer.close();
            }

            // receive response from server
            int responseCode = conn.getResponseCode();
            Log.i(TAG, "responseCode: " + responseCode);
            httpResponse.setStatus(responseCode);
            StringBuilder responseContent = new StringBuilder();
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            httpResponse.setContent(responseContent.toString());
            Log.i(CommonConstants.LogPrefix, httpResponse.getStatus() + "    " + httpResponse.getContent());
            reader.close();
        } catch (Exception e) {
            httpResponse.setContent(CommonExceptionMessages.CONNECTION_FAILURE);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return httpResponse;
    }

    private static String getPostDataParams(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
