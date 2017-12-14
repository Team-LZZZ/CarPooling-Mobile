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

    public static void performSearch(String targetLocationString, long time, ISearchStatus searchStatus) {
        AppContainer.getInstance().getSearchResult().clear();
        List<CarPool> intermediateRes = searchByTime(AppContainer.getInstance().getCarPools(), time);
        List<CarPool> finalRes = searchByLocation(intermediateRes, targetLocationString);
        AppContainer.getInstance().setSearchResult(finalRes);
        searchStatus.onSearchComplete();
    }

    public static void performSearch(String targetLocationString, ISearchStatus searchStatus) {
        AppContainer.getInstance().getSearchResult().clear();
        AppContainer.getInstance().setSearchResult(searchByLocation(AppContainer.getInstance().getCarPools(), targetLocationString));
        searchStatus.onSearchComplete();
    }

    public static void performSearch(long time, ISearchStatus searchStatus) {
        AppContainer.getInstance().getSearchResult().clear();
        AppContainer.getInstance().setSearchResult(searchByTime(AppContainer.getInstance().getCarPools(), time));
        searchStatus.onSearchComplete();
    }

    public static void performPastRes() {
        AppContainer.getInstance().getSearchResult().clear();
        List<CarPool> PastCarpool = new ArrayList<>();
        for (CarPool cp : AppContainer.getInstance().getCarPools()) {
            String currentDate = String.valueOf(System.currentTimeMillis());
            if (cp.getReserverList().contains(AppContainer.getInstance().getActiveUser()) && (cp.getTime().compareTo(currentDate) <= 0)) {
                AppContainer.getInstance().getSearchResult().add(cp);
            }
        }
    }

    public static void performUpcomingRes() {
        AppContainer.getInstance().getSearchResult().clear();
        List<CarPool> PastCarpool = new ArrayList<>();
        for (CarPool cp : AppContainer.getInstance().getCarPools()) {
            String currentDate = String.valueOf(System.currentTimeMillis());
            if (cp.getReserverList().contains(AppContainer.getInstance().getActiveUser()) && (cp.getTime().compareTo(currentDate) > 0)) {
                AppContainer.getInstance().getSearchResult().add(cp);
            }
        }
    }

    public static void performPastOffer() {
        AppContainer.getInstance().getSearchResult().clear();
        List<CarPool> PastCarpool = new ArrayList<>();
        for (CarPool cp : AppContainer.getInstance().getCarPools()) {
            String currentDate = String.valueOf(System.currentTimeMillis());
            if (cp.getOfferer().equals(AppContainer.getInstance().getActiveUser()) && (cp.getTime().compareTo(currentDate) <= 0)) {
                AppContainer.getInstance().getSearchResult().add(cp);
            }
        }
    }

    public static void performUpcomingOffer() {
        AppContainer.getInstance().getSearchResult().clear();
        List<CarPool> PastCarpool = new ArrayList<>();
        for (CarPool cp : AppContainer.getInstance().getCarPools()) {
            String currentDate = String.valueOf(System.currentTimeMillis());
            if (cp.getOfferer().equals(AppContainer.getInstance().getActiveUser()) && (cp.getTime().compareTo(currentDate) > 0)) {
                AppContainer.getInstance().getSearchResult().add(cp);
            }
        }
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
        return carPools;
    }

    private static List<CarPool> searchByTime(List<CarPool> carpoolsSearchPool, long searchTime) {
        List<CarPool> res = new ArrayList<>();
        for (CarPool carPool : carpoolsSearchPool) {
            long eachCarPoolTime = Long.valueOf(carPool.getTime());
            if (isTheSameDate(eachCarPoolTime, searchTime)) {
                res.add(carPool);
            }
        }
        return res;
    }

    private static String convertMillisecondsToDateString(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy", Locale.US);
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("US/East"));
        calendar.setTimeInMillis(time);
        return sdf.format(calendar.getTime());
    }

    private static boolean isTheSameDate(long time1, long time2) {
        String date_1 = convertMillisecondsToDateString(time1).split(" ")[1];
        String date_2 = convertMillisecondsToDateString(time2).split(" ")[1];
        String month_1 = convertMillisecondsToDateString(time1).split(" ")[0];
        String month_2 = convertMillisecondsToDateString(time2).split(" ")[0];
        String year_1 = convertMillisecondsToDateString(time1).split(" ")[2];
        String year_2 = convertMillisecondsToDateString(time2).split(" ")[2];
        return date_1.equals(date_2) && month_1.equals(month_2) && year_1.equals(year_2);
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
                if (infoInOne.equals(infoInTwo)) {
                    score++;
                }
            }
        }
        return score;
    }

}
