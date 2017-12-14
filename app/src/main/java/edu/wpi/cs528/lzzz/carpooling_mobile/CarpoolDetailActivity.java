package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.CancelResHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.IConnectionStatus;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.ReservationHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.CarPool;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonUtils;

public class CarpoolDetailActivity extends AppCompatActivity {

    private CarPool carPool;

    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private TextView plateTextView;
    private TextView modelMakeTextView;
    private TextView fromAddressTextView;
    private TextView toAddressTextView;
    private TextView dateTextView;
    private int numberPeople;
//    private NumberPicker reservationNumberPicker;
    private Button makeReservationBtn;
    private Button makeCancelBtn;
    private TextView mChooseNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool_detail);

        Intent intent = getIntent();
        String carPoolJson = intent.getStringExtra("carPoolInfo");
        int action  = intent.getIntExtra("action", 0);

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
        mChooseNumberTextView = (TextView) findViewById(R.id.NumberPeople);
        makeReservationBtn = (Button) findViewById(R.id.reservation);
        makeCancelBtn = (Button) findViewById(R.id.cancel);
        mChooseNumberTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showNumberPicker(1, carPool.getAvailable());
            }
        });

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

        if (action == CommonConstants.MAKE_RESERVATION){
            makeCancelBtn.setBackgroundColor(Color.DKGRAY);
            makeCancelBtn.setEnabled(false);
        } else if (action == CommonConstants.VIEW_INCOME_RESERVATION){
            makeReservationBtn.setBackgroundColor(Color.DKGRAY);
            makeReservationBtn.setEnabled(false);
        }

        makeReservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int offer_id = CarpoolDetailActivity.this.carPool.getOid();
                int num = Integer.valueOf(numberPeople);
                ReservationInput input = new ReservationInput(offer_id, num);
                Gson gson = new Gson();
                String inputJson = gson.toJson(input);

                ReservationHandler handler = new ReservationHandler(new IConnectionStatus() {
                    @Override
                    public void onComplete(Boolean success, String additionalInfos) {
                        if (success) {
//                            makeReservationSucceed();
                            showReservationAlert(true, "Reservation is Successful.");


                        } else {
//                            makeReservationFailed(additionalInfos);
                            showReservationAlert(false, "Reservation failed.");
                        }
                    }
                });

                handler.connectForResponse(CommonUtils.createHttpPOSTRequestMessageWithToken(inputJson, CommonConstants.makeNewReservation));
            }
        });


        makeCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int offer_id = CarpoolDetailActivity.this.carPool.getOid();

                CancelInput input = new CancelInput(offer_id);
                Gson gson = new Gson();
                String inputJson = gson.toJson(input);

                CancelResHandler handler = new CancelResHandler(new IConnectionStatus() {
                    @Override
                    public void onComplete(Boolean success, String additionalInfos) {
                        if (success) {
//                            makeReservationSucceed();
                            showCancelReservationAlert(true, "Cancel reservation is Successful.");


                        } else {
//                            makeReservationFailed(additionalInfos);
                            showCancelReservationAlert(false, "Cancel reservation failed.");
                        }
                    }
                });

                handler.connectForResponse(CommonUtils.createHttpDeleteRequestMessageWithToken(inputJson, CommonConstants.makeNewReservation));
            }
        });
    }

//    private void makeReservationSucceed(){
//        Toast.makeText(this, "reservation successful", Toast.LENGTH_SHORT).show();
//    }
//    private void makeReservationFailed(String error){
//        Toast.makeText(this, "reservation failed", Toast.LENGTH_SHORT).show();
//    }

    private void showCancelReservationAlert(boolean success, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

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
                        Intent intent = new Intent(CarpoolDetailActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        alertDialog.show();
    }



    private void showReservationAlert(boolean success, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

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
                        Intent intent = new Intent(CarpoolDetailActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        alertDialog.show();
    }

    private void showNumberPicker(int min, int max){
        final cn.qqtheme.framework.picker.NumberPicker picker = new cn.qqtheme.framework.picker.NumberPicker(this);
        picker.setItemWidth(200);
        View headerView = View.inflate(this, R.layout.picker_header, null);
        final TextView titleView = (TextView) headerView.findViewById(R.id.picker_title);
        headerView.findViewById(R.id.picker_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.dismiss();
            }
        });
        picker.setHeaderView(headerView);
        picker.setCycleDisable(true);
        picker.setOffset(4);
        picker.setRange(min, max, 1);
        picker.setSelectedItem(min);
        picker.setOnWheelListener(new cn.qqtheme.framework.picker.NumberPicker.OnWheelListener() {
            @Override
            public void onWheeled(int index, Number item) {
                titleView.setText("Number of people : " + item.intValue());
                mChooseNumberTextView.setText("Number of people : " + item.intValue());
                numberPeople = item.intValue();
            }
        });
        picker.show();
    }
}

class ReservationInput{
    private int offer_id;
    private int num;

    public ReservationInput(int offer_id, int num) {
        this.offer_id = offer_id;
        this.num = num;
    }

    public int getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(int offer_id) {
        this.offer_id = offer_id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}

class CancelInput{
    private int offer_id;

    public CancelInput(int offer_id) {
        this.offer_id = offer_id;

    }

    public int getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(int offer_id) {
        this.offer_id = offer_id;
    }


}