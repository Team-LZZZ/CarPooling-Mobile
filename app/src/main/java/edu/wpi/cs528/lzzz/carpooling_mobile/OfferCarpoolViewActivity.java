package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import edu.wpi.cs528.lzzz.carpooling_mobile.model.CarPool;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.Reserver;


public class OfferCarpoolViewActivity extends AppCompatActivity {

    private CarPool carPool;

    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private TextView plateTextView;
    private TextView modelMakeTextView;
    private TextView fromAddressTextView;
    private TextView toAddressTextView;
    private TextView dateTextView;
    private TextView reserverList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_carpool_view);

        Intent intent = getIntent();
        String carPoolJson = intent.getStringExtra("carPoolInfo");
        Gson gson = new Gson();
        this.carPool = gson.fromJson(carPoolJson, CarPool.class);
        nameTextView = (TextView) findViewById(R.id.carppool_detail_name);
        phoneTextView = (TextView) findViewById(R.id.carppool_detail_phone);
        emailTextView = (TextView) findViewById(R.id.carppool_detail_email);
        plateTextView = (TextView) findViewById(R.id.carppool_detail_car_plate);
        modelMakeTextView = (TextView) findViewById(R.id.carppool_detail_car_detail);
        fromAddressTextView = (TextView) findViewById(R.id.carppool_detail_from_address);
        toAddressTextView = (TextView) findViewById(R.id.carppool_detail_to_address);
        dateTextView = (TextView) findViewById(R.id.carppool_detail_departure_time);
        reserverList = (TextView) findViewById(R.id.res1) ;

        nameTextView.setText(carPool.getOfferer().getUsername());
        phoneTextView.setText(carPool.getOfferer().getPhone());
        emailTextView.setText(carPool.getOfferer().getEmail());
        plateTextView.setText(carPool.getCar().getPlate());
        modelMakeTextView.setText(carPool.getCar().getMake() + " " + carPool.getCar().getMake());
        fromAddressTextView.setText(carPool.getStartLocation().getAddress());
        toAddressTextView.setText(carPool.getTargetLocation().getAddress());

        Long millis = Long.valueOf(carPool.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("US/East"));
        calendar.setTimeInMillis(millis);
        String date = sdf.format(calendar.getTime());
        dateTextView.setText(date);

        StringBuilder stringBuilder = new StringBuilder();
        for(Reserver r : carPool.getReserverList()){
            stringBuilder.append(r.getUsername() + " " + r.getPhone());
            stringBuilder.append("\n");
        }
        reserverList.setText(stringBuilder.toString());
    }
}
