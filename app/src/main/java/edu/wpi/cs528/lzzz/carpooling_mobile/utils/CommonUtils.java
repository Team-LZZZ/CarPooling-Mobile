package edu.wpi.cs528.lzzz.carpooling_mobile.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ImageView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import edu.wpi.cs528.lzzz.carpooling_mobile.LoginActivity;
import edu.wpi.cs528.lzzz.carpooling_mobile.R;
import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpRequestMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.CarPool;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.User;

/**
 * Created by QZhao on 12/6/2017.
 */

public class CommonUtils {
    private static AmazonS3Client sS3Client;
    private static CognitoCachingCredentialsProvider sCredProvider;
    private static TransferUtility sTransferUtility;

    private static CognitoCachingCredentialsProvider getCredProvider(Context context) {
        if (sCredProvider == null) {
            sCredProvider = new CognitoCachingCredentialsProvider(
                    context.getApplicationContext(),
                    CommonConstants.COGNITO_POOL_ID,
                    Regions.fromName(CommonConstants.COGNITO_POOL_REGION));
        }
        return sCredProvider;
    }

    public static AmazonS3Client getS3Client(Context context) {
        if (sS3Client == null) {
            sS3Client = new AmazonS3Client(getCredProvider(context.getApplicationContext()));
            sS3Client.setRegion(Region.getRegion(Regions.fromName(CommonConstants.BUCKET_REGION)));
        }
        return sS3Client;
    }

    public static TransferUtility getTransferUtility(Context context) {
        if (sTransferUtility == null) {
            sTransferUtility = new TransferUtility(getS3Client(context.getApplicationContext()),
                    context.getApplicationContext());
        }

        return sTransferUtility;
    }

    public static void deletePhoto(String keyName){
        sS3Client.deleteObject(new DeleteObjectRequest(CommonConstants.BUCKET_NAME, keyName));
    }

    static String putAnObject(String keyName) {
        String content = "This is the content body!";
        String key = "ObjectToDelete-" + new Random().nextInt();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHeader("Subject", "Content-As-Object");
        metadata.setHeader("Content-Length", content.length());
        PutObjectRequest request = new PutObjectRequest(CommonConstants.BUCKET_NAME, key,
                new ByteArrayInputStream(content.getBytes()), metadata)
                .withCannedAcl(CannedAccessControlList.AuthenticatedRead);
        PutObjectResult response = sS3Client.putObject(request);
        return response.getVersionId();
    }

    public static HttpRequestMessage createHttpPOSTRequestMessage(String jsonString, String apiName){
        HttpRequestMessage request = new HttpRequestMessage();
        request.setMethod("POST");
        request.setBody(jsonString);
        request.setUrl(CommonConstants.BASE_URL + apiName);
        return request;
    }

    public static HttpRequestMessage createHttpPOSTRequestMessageWithToken(String jsonString, String apiName){
        HttpRequestMessage request = new HttpRequestMessage();
        request.setMethod("POST");
        request.setBody(jsonString);
        request.setUrl(CommonConstants.BASE_URL + apiName);
        request.setToken(AppContainer.getInstance().getToken());
        return request;
    }

    public static HttpRequestMessage createHttpRequestMessageWithToken(String jsonString, String apiName, String method){
        HttpRequestMessage request = new HttpRequestMessage();
        request.setMethod(method);
        request.setBody(jsonString);
        request.setUrl(CommonConstants.BASE_URL + apiName);
        request.setToken(AppContainer.getInstance().getToken());
        return request;
    }

    public static HttpRequestMessage createHttpGETRequestMessage(String apiName){
        HttpRequestMessage request = new HttpRequestMessage();
        request.setMethod("GET");
        request.setUrl(CommonConstants.BASE_URL + apiName);
        return request;
    }

    public static HttpRequestMessage createHttpGETRequestMessageWithToken(String apiName){
        HttpRequestMessage request = new HttpRequestMessage();
        request.setMethod("GET");
        request.setToken(AppContainer.getInstance().getToken());
        request.setUrl(CommonConstants.BASE_URL + apiName);
        return request;
    }

    public static ProgressDialog createProgressDialog(Context context, String message){
        ProgressDialog progressDialog = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public static void showAlert(Context context, boolean success, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        if (!success){
            alertDialog.setTitle("Failure");
        }else{
            alertDialog.setTitle("Results");
        }

        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public static void getProfile(Context context, String photoPath, ImageView mImageView){
        Glide.with(context).load(photoPath).diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener() {
            @Override public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                android.util.Log.d(CommonConstants.LogPrefix, String.format(Locale.ROOT,
                        "onException(%s, %s, %s, %s)", e, model, target, isFirstResource), e);
                return false;
            }
            @Override public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                android.util.Log.d(CommonConstants.LogPrefix, String.format(Locale.ROOT,
                        "onResourceReady(%s, %s, %s, %s, %s)", resource, model, target, isFromMemoryCache, isFirstResource));
                return false;
            }
        }).error(R.drawable.wpi).into(mImageView);
    }

    public static List<CarPool> getAvailabeRes(){
        List<CarPool> AvailCarpool = new ArrayList<>();
        for (CarPool cp : AppContainer.getInstance().getCarPools()) {
            if (cp.getAvailable() > 0) {
                AvailCarpool.add(cp);
            }
        }
        return AvailCarpool;
    }

    public static List<CarPool> performPastRes() {
        List<CarPool> pastRes = new ArrayList<>();
        for (CarPool cp : AppContainer.getInstance().getCarPools()) {
            String currentDate = String.valueOf(System.currentTimeMillis());
            if (cp.getReserverList().contains(AppContainer.getInstance().getActiveUser()) && (cp.getTime().compareTo(currentDate) <= 0)) {
                pastRes.add(cp);
            }
        }
        return pastRes;
    }

    public static List<CarPool> performUpcomingRes() {
        List<CarPool> upcomingRes = new ArrayList<>();
        for (CarPool cp : AppContainer.getInstance().getCarPools()) {
            String currentDate = String.valueOf(System.currentTimeMillis());
            if (cp.getReserverList().contains(AppContainer.getInstance().getActiveUser()) && (cp.getTime().compareTo(currentDate) > 0)) {
                upcomingRes.add(cp);
            }
        }
        return upcomingRes;
    }

    public static List<CarPool> performPastOffer() {
        List<CarPool> pastOffer = new ArrayList<>();
        for (CarPool cp : AppContainer.getInstance().getCarPools()) {
            String currentDate = String.valueOf(System.currentTimeMillis());
            if (cp.getOfferer().getUsername().equals(AppContainer.getInstance().getActiveUser().getUsername()) && (cp.getTime().compareTo(currentDate) <= 0)) {
                pastOffer.add(cp);
            }
        }
        return pastOffer;
    }

    public static List<CarPool> performUpcomingOffer() {
        List<CarPool> upcomingOffer = new ArrayList<>();
        for (CarPool cp : AppContainer.getInstance().getCarPools()) {
            String currentDate = String.valueOf(System.currentTimeMillis());
            if (cp.getOfferer().getUsername().equals(AppContainer.getInstance().getActiveUser().getUsername()) && (cp.getTime().compareTo(currentDate) > 0)) {
                upcomingOffer.add(cp);
            }
        }
        return upcomingOffer;
    }






}
