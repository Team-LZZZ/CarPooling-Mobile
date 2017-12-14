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
    private NumberPicker reservationNumberPicker;
    private Button makeReservationBtn;
    private Button makeCancelBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool_detail);

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
        reservationNumberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        reservationNumberPicker.setMinValue(1);
        reservationNumberPicker.setMaxValue(7);
        reservationNumberPicker.setWrapSelectorWheel(true);
        makeReservationBtn = (Button) findViewById(R.id.reservation);
        makeCancelBtn = (Button) findViewById(R.id.cancel);

        reservationNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker
                numberPeople = newVal;
            }
        });


        nameTextView.setText(carPool.getOfferer().getUsername());
        phoneTextView.setText(carPool.getOfferer().getPhone());
        emailTextView.setText(carPool.getOfferer().getEmail());
        plateTextView.setText(carPool.getCar().getPlate());
        modelMakeTextView.setText(carPool.getCar().getMake() + " " + carPool.getCar().getMake());
        fromAddressTextView.setText(carPool.getStartLocation().getAddress());
        toAddressTextView.setText(carPool.getTargetLocation().getAddress());
        makeCancelBtn.setEnabled(false);
        makeCancelBtn.setBackgroundColor(Color.DKGRAY);

        Long millis = Long.valueOf(carPool.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("US/East"));
        calendar.setTimeInMillis(millis);
        String date = sdf.format(calendar.getTime());
        dateTextView.setText(date);

        makeReservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int offer_id = CarpoolDetailActivity.this.carPool.getOid();
                int num = reservationNumberPicker.getValue();
                //ReservationInput input = new ReservationInput(offer_id, num);
                ReservationInput input = new ReservationInput(offer_id, numberPeople);
                Gson gson = new Gson();
                String inputJson = gson.toJson(input);

                ReservationHandler handler = new ReservationHandler(new IConnectionStatus() {
                    @Override
                    public void onComplete(Boolean success, String additionalInfos) {
                        if (success) {
//                            makeReservationSucceed();
                            showReservationAlert(true, "Reservation is Successful.");
                            makeReservationBtn.setEnabled(false);
                            makeReservationBtn.setBackgroundColor(Color.DKGRAY);
                            makeCancelBtn.setEnabled(true);

                        } else {
//                            makeReservationFailed(additionalInfos);
                            showReservationAlert(false, "Reservation failed.");
                        }
                    }
                });

                handler.connectForResponse(CommonUtils.createHttpPOSTRequestMessageWithToken(inputJson, CommonConstants.makeNewReservation));
            }
        });
    }

//    private void makeReservationSucceed(){
//        Toast.makeText(this, "reservation successful", Toast.LENGTH_SHORT).show();
//    }
//    private void makeReservationFailed(String error){
//        Toast.makeText(this, "reservation failed", Toast.LENGTH_SHORT).show();
//    }

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