package edu.wpi.cs528.lzzz.carpooling_mobile.handlers;

import android.util.Log;

import com.amazonaws.transform.MapEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.*;
import java.util.PriorityQueue;
import java.util.TimeZone;
import java.util.TreeMap;

import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.CarPool;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.User;

/**
 * Created by QQZhao on 12/13/17.
 */

public class SearchHandler {

    public static void performSearch(String targetAddressString, String targetDateString, ISearchStatus searchStatus) {
        AppContainer.getInstance().getSearchResult().clear();
        List<CarPool> intermediateRes = searchByTime(AppContainer.getInstance().getCarPools(), targetDateString);
        List<CarPool> finalRes = searchByLocation(intermediateRes, targetAddressString);
        AppContainer.getInstance().setSearchResult(finalRes);
        searchStatus.onSearchComplete();
    }

    public static void performSearch(boolean inputIsAddress, String inputString, ISearchStatus searchStatus){
        AppContainer.getInstance().getSearchResult().clear();
        if (inputIsAddress){
            AppContainer.getInstance().setSearchResult(searchByLocation(AppContainer.getInstance().getCarPools(), inputString));
        }else{
            AppContainer.getInstance().setSearchResult(searchByTime(AppContainer.getInstance().getCarPools(), inputString));
        }
        searchStatus.onSearchComplete();
    }

    private static List<CarPool> searchByLocation(List<CarPool> carpoolsSearchPool, String targetLocationString) {
        Map<CarPool, Integer> scoreMap = new HashMap<>();

        for (CarPool carPool : carpoolsSearchPool) {
            String carPoolAddress = carPool.getTargetLocation().getAddress();
            Integer similarityScore = getSimilarityScore(carPoolAddress, targetLocationString);
            scoreMap.put(carPool, similarityScore);
        }
        List<Map.Entry<CarPool, Integer>> list = new ArrayList<Map.Entry<CarPool, Integer>>(scoreMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<CarPool, Integer>>() {
            public int compare(Map.Entry<CarPool, Integer> o1,
                               Map.Entry<CarPool, Integer> o2) {
                return -o1.getValue().compareTo(o2.getValue());
            }
        });
        List<CarPool> carPools = new ArrayList<>();
        for (Map.Entry<CarPool, Integer> map : list) {
            carPools.add(map.getKey());
        }
        StringBuilder logSB = new StringBuilder();
        for (CarPool c : carPools) {
            logSB.append(c.getTargetLocation().getAddress());
            logSB.append("\n");
        }
        Log.i("SearchHandler====: ", logSB.toString());
        return carPools;
    }

    private static List<CarPool> searchByTime(List<CarPool> carpoolsSearchPool, String targetformatedDateTimeString) {
        List<CarPool> res = new ArrayList<>();
        for (CarPool carPool : carpoolsSearchPool) {
            long eachCarPoolTime = Long.valueOf(carPool.getTime());
            String eachCarPoolFormattedDateTimeString = convertMillisecondsToDateString(eachCarPoolTime);
            if (isTheSameDate(eachCarPoolFormattedDateTimeString, targetformatedDateTimeString)) {
                res.add(carPool);
            }
        }
        return res;
    }

    private static String convertMillisecondsToDateString(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("US/East"));
        calendar.setTimeInMillis(time);
        return sdf.format(calendar.getTime());
    }

    private static boolean isTheSameDate(String time1, String time2) {
        String[] time1Array = time1.replace(" ", ",").split(",");
        String[] time2Array = time2.replace(" ", ",").split(",");
        for (int i = 0; i < time1Array.length; i++){
            if (!time1Array[i].equals(time2Array[i])){
                return false;
            }
        }
        return true;
    }

    private static Integer getSimilarityScore(String address_1, String address_2) {
        Integer score = 0;
        String address_1_modified = address_1.replace(" ", ",");
        address_1_modified = address_1_modified.replace(".", ",");
        String[] addressOneArray = address_1_modified.split(",");

        String address_2_modified = address_2.replace(" ", ",");
        address_2_modified = address_2_modified.replace(".", ",");
        String[] addressTwoArray = address_2_modified.split(",");

        for (String infoInOne : addressOneArray) {
            for (String infoInTwo : addressTwoArray) {
                Log.i("SearchHandlerCity==: ", infoInOne + "    " + infoInTwo);
                if (infoInOne.equals(infoInTwo)) {
                    score++;
                }
            }
        }
        return score;
    }

}
