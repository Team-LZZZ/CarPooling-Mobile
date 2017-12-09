package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.internal.ListenerClass;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.IConnectionStatus;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.ReservationHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.Reservation;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonUtils;

/**
 * Created by liweihao on 12/5/17.
 */

public class ReservationActivity extends AppCompatActivity {

    private Button reserveBtn;
    private Button cancelReservationBtn;
    private Button goBackBtn;
    private TextView reserverNameTextView;
    private TextView reserveNumberTextView;
    private long carPoolId;
    private boolean reserveMode;
    private int reserveSeats;
    private ProgressDialog progressDialog;
    private ReservationHandler reservationHandler;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        carPoolId = getIntent().getIntExtra("carPoolId", 0);
        reserveMode = getIntent().getBooleanExtra("reserveMode", false);

        reserverNameTextView = (TextView) findViewById(R.id.ReserverName);
        reserverNameTextView.setText(AppContainer.getInstance().getActiveUser().getUsername());
        reserverNameTextView.setEnabled(false);
        reserveNumberTextView = (TextView) findViewById(R.id.ReserveSeatNumber);
        this.progressDialog = CommonUtils.createProgressDialog(this, "loading..");

        reserveBtn = (Button) findViewById(R.id.ReserveBtn);
        reserveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isInputValid()){
                    onReservationFailed("Invalid Input");
                    return;
                }

                Reservation reservation = new Reservation();
                reservation.setCarPoolId(carPoolId);
                reservation.setNewReservedSeatsNumber(reserveSeats);
                String reservationJson = gson.toJson(reservation);

                reservationHandler = new ReservationHandler(new IConnectionStatus() {
                    @Override
                    public void onComplete(Boolean success, String additionalInfos) {
                        progressDialog.dismiss();
                        if (success){
                            onMakeReservationSucceed();
                        }else{
                            onReservationFailed(additionalInfos);
                        }
                    }
                });
                reservationHandler.connectForResponse(CommonUtils.createHttpPOSTRequestMessageWithToken(reservationJson, CommonConstants.makeNewReservation));

            }
        });

        cancelReservationBtn = (Button) findViewById(R.id.cancelReserveBtn);
        cancelReservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelReservationSucceed();
            }
        });

        goBackBtn = (Button) findViewById(R.id.goBackBtn);
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservationActivity.this, MainActivity.class);
                ReservationActivity.this.startActivity(intent);
            }
        });

        if(reserveMode){
            reserveBtn.setEnabled(true);
            cancelReservationBtn.setEnabled(false);
        }else{
            reserveBtn.setEnabled(false);
            cancelReservationBtn.setEnabled(true);
        }
    }

    private boolean isInputValid(){

        String inputReserveSeats = reserveNumberTextView.getText().toString();
        try{
            this.reserveSeats = Integer.valueOf(inputReserveSeats);

            //more validations if neccessary

        }catch (Exception ex){
            return false;
        }
        return true;
    }

    private void onMakeReservationSucceed(){
        AlertDialog alertDialog = new AlertDialog.Builder(ReservationActivity.this).create();
        alertDialog.setTitle("Success");
        alertDialog.setMessage("Your reservation has been booked");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(ReservationActivity.this, MainActivity.class);
                        ReservationActivity.this.startActivity(i);
                    }
                });
        alertDialog.show();
    }

    private void onCancelReservationSucceed(){
        AlertDialog alertDialog = new AlertDialog.Builder(ReservationActivity.this).create();
        alertDialog.setTitle("Success");
        alertDialog.setMessage("Your reservation has been canceled.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void onReservationFailed(String error){
        Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();
    }

}
