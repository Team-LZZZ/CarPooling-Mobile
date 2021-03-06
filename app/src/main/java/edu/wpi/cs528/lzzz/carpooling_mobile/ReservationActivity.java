package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.internal.ListenerClass;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.IConnectionStatus;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.MyReservationHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.ReservationHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.Reservation;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonUtils;

/**
 * Created by liweihao on 12/5/17.
 */

public class ReservationActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static int int_items = 2 ;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_reservation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);


        myAdapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myAdapter);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        MyReservationHandler myReservationHandler = new MyReservationHandler(new IConnectionStatus() {
            @Override
            public void onComplete(Boolean success, String additionalInfos) {
                if (success){
                    myAdapter.notifyDataSetChanged();
                    Log.i("ReservationActivity", "" + AppContainer.getInstance().getMyReservations().size());
                }else{
                    CommonUtils.showAlert(ReservationActivity.this, false, "can not get my reservations.");
                }
            }
        });
        myReservationHandler.connectForResponse(CommonUtils.createHttpGETRequestMessageWithToken(CommonConstants.getMyReservations));
    }

    private class MyAdapter extends FragmentPagerAdapter {

        MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 :
                    Log.i("case0", "++++++++++");
//                    ReservationPastFragment reservationPastFragment = new ReservationPastFragment();
                    return ReservationPastFragment.newInstance(1);
                case 1 :
                    Log.i("case1", "++++++++++");
                    return new ReservationUpcomingFragment();

            }

            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Past";
                case 1 :
                    return "Upcoming";

            }
            return null;
        }



    }
}