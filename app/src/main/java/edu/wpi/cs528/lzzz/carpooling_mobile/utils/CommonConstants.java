package edu.wpi.cs528.lzzz.carpooling_mobile.utils;

/**
 * Created by QQZhao on 10/28/17.
 */

public class CommonConstants {

    // connections
//    public static String BASE_URL = "http://youzhou1993.pythonanywhere.com/api/";
 //   public static String BASE_URL = "http://10.0.2.2:5000/api/";
    public static String BASE_URL = "https://bd1858ff.ngrok.io/api/";
    public static String registration = "reg";
    public static String login = "login";
    public static String getAllCarpools = "carPools";
    public static String makeNewReservation = "reservations";
    public static String makeNewOffer = "offers";
    public static String userSetting ="settings";
   //    public static String BASE_URL = "http://127.0.0.1:5000/api/";
    public static String LogPrefix = "CS528Final";

    // GoogleMap
    public static int MAP_CAMERA = 15;

    //Amazon S3
    public static final String COGNITO_POOL_ID = "us-east-2:d59f7b37-b24b-4c30-bcf2-e9aef1c51965";
    public static final String COGNITO_POOL_REGION = "us-east-2";
    public static final String BUCKET_NAME = "wpics528";
    public static final String BUCKET_REGION = "us-east-2";
    public static final String S3_PREFIX = "https://s3.us-east-2.amazonaws.com/wpics528/";
}
