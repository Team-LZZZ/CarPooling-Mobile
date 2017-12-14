package edu.wpi.cs528.lzzz.carpooling_mobile;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;

import java.util.Date;
import java.util.GregorianCalendar;


import butterknife.Bind;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.IConnectionStatus;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.LogInHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.OfferHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.Car;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.CarPool;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.Location;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.User;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonUtils;


public class OfferCarpoolActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private final String TAG = "OfferCarpoolctivity";

    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView emailTextView;

    private EditText makeInputEditText;
    private EditText modelInputEditText;
    private EditText plateInputEditText;
    private TextView dateInputEditText;
    private TextView fromAddressEditText;
    private TextView toAddressEditText;
    private Button makeOfferBtn;

    private double startAddressLattitude;
    private double startAddressLongitude;
    private double targetAddressLattitude;
    private double targetAddressLongitude;

    private String startAddressName;
    private String targetAddressName;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_carpool);

        nameTextView = (TextView) findViewById(R.id.profile_name);
        phoneTextView = (TextView) findViewById(R.id.profile_phone);
        emailTextView = (TextView) findViewById(R.id.profile_email);
        makeInputEditText = (EditText) findViewById(R.id.make_input);
        modelInputEditText = (EditText) findViewById(R.id.model_input);
        plateInputEditText = (EditText) findViewById(R.id.plate_input);
        dateInputEditText = (TextView) findViewById(R.id.carppool_day);

        fromAddressEditText = (TextView) findViewById(R.id.carppool_departure);
        toAddressEditText = (TextView) findViewById(R.id.carppool_arrival);
        User user = AppContainer.getInstance().getActiveUser();
        nameTextView.setText(user.getUsername());
        phoneTextView.setText(user.getPhone());
        emailTextView.setText(user.getEmail());
        
        View.OnClickListener selectAddressListener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try{

                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(OfferCarpoolActivity.this);
                    switch (v.getId()){
                        case(R.id.carppool_departure):
                            startActivityForResult(intent, 0);
                            break;
                        case(R.id.carppool_arrival):
                            startActivityForResult(intent, 1);
                            break;
                    }

                }catch (Exception ex){

                }
            }
        };
        fromAddressEditText.setOnClickListener(selectAddressListener);
        toAddressEditText.setOnClickListener(selectAddressListener);

//        this.startAddressName = "WPI";
//        this.startAddressLattitude = 42.274637;
//        this.startAddressLongitude = -71.806339;
//        this.targetAddressName = "Boston";
//        this.targetAddressLattitude = 42.360082;
//        this.targetAddressLongitude = -71.058880;

        makeOfferBtn = (Button) findViewById(R.id.Offer_carpool);
        makeOfferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OfferHandler offerHandler = new OfferHandler(new IConnectionStatus() {
                    @Override
                    public void onComplete(Boolean success, String additionalInfos) {
                        OfferCarpoolActivity.this.progressDialog.dismiss();
                        if(success){
                            onOfferCarpoolSucceed();
                        }else{
                            onOfferCarpoolFailed(additionalInfos);
                        }
                    }
                });

                String offerJson = obtainInputsAsJson();
                offerHandler.connectForResponse(CommonUtils.createHttpPOSTRequestMessageWithToken(offerJson, CommonConstants.makeNewOffer));
                progressDialog = CommonUtils.createProgressDialog(OfferCarpoolActivity.this, "Sending to server...");
            }
        });
    }

    public void datePicker(View view){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        this.dateInputEditText.setText(dateFormat.format(calendar.getTime()));

    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener)
                            getActivity(), year, month, day);
        }

    }

    private void onOfferCarpoolSucceed() {
        showOfferAlert(this, true, "Offer has been made successfully");
    }

    private void onOfferCarpoolFailed(String error){
        showOfferAlert(this, false, error);
    }

    private String obtainInputsAsJson(){

        String make = makeInputEditText.getText().toString();
        String model = modelInputEditText.getText().toString();
        String plate = plateInputEditText.getText().toString();

        String rawDate = dateInputEditText.getText().toString();
        Log.i(TAG, rawDate);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        Date date = null;
        try{
            date = sdf.parse(rawDate);
        }catch (Exception ex){
            Toast.makeText(this,"Invalid Date", Toast.LENGTH_SHORT).show();
            return null;
        }
        long dateInMilliseconds = date.getTime() * (long)1.2;

        if (this.startAddressLattitude == 0 ||
                this.startAddressLongitude == 0 ||
                this.targetAddressLattitude == 0 ||
                this.targetAddressLongitude == 0 ||
                this.startAddressName == null ||
                this.targetAddressName == null){
            Toast.makeText(this,"You need to input address", Toast.LENGTH_SHORT).show();
            return null;
        }

        Gson gson = new Gson();
        OfferInput offerInput = new OfferInput();
        Car car = new Car();
        car.setMake(make);
        car.setModel(model);
        car.setPlate(plate);
        offerInput.setCar(car);
        offerInput.setSeats_available(4);
        Location startLocation = new Location();
        startLocation.setLatitude(this.startAddressLattitude);
        startLocation.setLongitude(this.startAddressLongitude);
        startLocation.setAddress(this.startAddressName);

        Location targetLocation = new Location();
        targetLocation.setLatitude(this.targetAddressLattitude);
        targetLocation.setLongitude(this.targetAddressLongitude);
        targetLocation.setAddress(this.targetAddressName);
        offerInput.setStartLocation(startLocation);
        offerInput.setTargetLocation(targetLocation);
        offerInput.setTime(dateInMilliseconds);
        String offerJson = gson.toJson(offerInput);
        return offerJson;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 0 || requestCode == 1) && resultCode == Activity.RESULT_OK){

            final Place place = PlacePicker.getPlace(data, this);
            final CharSequence nameChar = place.getName();
            final CharSequence addressChar = place.getAddress();
            String name = nameChar.toString();
            String address = addressChar.toString();
            if (name.contains("\"")){
                name = transferLatLngToAddress(address);
            }
                Log.i(CommonConstants.LogPrefix,address);
            String attributions = PlacePicker.getAttributions(data);
            if(attributions == null){
                attributions = "";
            }

            switch (requestCode){
                case(0):
                    fromAddressEditText.setText(name);
                    this.startAddressName = name;
                    this.startAddressLattitude = place.getLatLng().latitude;
                    this.startAddressLongitude = place.getLatLng().longitude;
                    break;
                case(1):
                    this.targetAddressName = name;
                    toAddressEditText.setText(name);
                    this.targetAddressLattitude = place.getLatLng().latitude;
                    this.targetAddressLongitude = place.getLatLng().longitude;
                    break;
            }
        }
    }

    private void showOfferAlert(Context context, boolean success, String message){
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
                        Intent intent = new Intent(OfferCarpoolActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        alertDialog.show();
    }

    public String transferLatLngToAddress(String address){
        String[] addressInfo = address.split(",");
        return addressInfo[0] + "," + addressInfo[1];
    }
}

class OfferInput{
    private Location startLocation;
    private Location targetLocation;
    private Car car;

    private int seats_available;
    private long time;

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(Location targetLocation) {
        this.targetLocation = targetLocation;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public int getSeats_available() {
        return seats_available;
    }

    public void setSeats_available(int seats_available) {
        this.seats_available = seats_available;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


}