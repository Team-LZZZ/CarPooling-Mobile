package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;

import java.time.LocalDateTime;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.IConnectionStatus;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.OfferHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonUtils;

public class OfferFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private static final int REQUEST_PLACE_PICKER_FOR_FROM_ADDRESS = 1;
    private static final int REQUEST_PLACE_PICKER_FOR_TO_ADDRESS = 2;

    private ProgressDialog progressDialog;
    private OfferHandler offerHandler;

    public int mPageNo;
    @Bind(R.id.select_button)
    Button mButton;
    @Bind(R.id.from_address)
    TextView mFromAddressText;
    @Bind(R.id.to_address)
    TextView mToAddressText;
    public OfferFragment() {
    }

    public static OfferFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        OfferFragment fragment = new OfferFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        ButterKnife.bind(this,view);
        View.OnClickListener selectAddressListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(getActivity());
                    // Start the intent by requesting a result,
                    // identified by a request code.
                    switch (v.getId()){
                        case (R.id.from_address):
                            startActivityForResult(intent, REQUEST_PLACE_PICKER_FOR_FROM_ADDRESS);
                            break;
                        case (R.id.to_address):
                            startActivityForResult(intent, REQUEST_PLACE_PICKER_FOR_TO_ADDRESS);
                            break;
                    }


                } catch (GooglePlayServicesRepairableException e) {
                    // ...
                } catch (GooglePlayServicesNotAvailableException e) {
                    // ...
                }
            }
        };

        mFromAddressText.setOnClickListener(selectAddressListener);
        mToAddressText.setOnClickListener(selectAddressListener);




        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Gson gson = new Gson();
                OfferInput offer = new OfferInput();
                offer.setStart_latitude(42.2625);
                offer.setStart_longitude(-71.8027778);
                offer.setTarget_latitude(42.3583333);
                offer.setTarget_longitude(-71.0602778);
                offer.setCar_plate("TestPlate");
                offer.setMilliseconds(System.currentTimeMillis());
                offer.setSeats_available(4);
                String offerJson = gson.toJson(offer);

                offerHandler = new OfferHandler(new IConnectionStatus() {

                    @Override
                    public void onComplete(Boolean isSuccess, String additionalInfos) {
                        progressDialog.dismiss();
                        if (isSuccess){
                            onMakeOfferSuccess();
                        }else{
                            onFailed(additionalInfos);
                        }
                    }
                });
                if(AppContainer.getInstance().isLogIn()){
                    offerHandler.connectForResponse(CommonUtils.createHttpPOSTRequestMessageWithToken(offerJson, CommonConstants.makeNewOffer));
                    progressDialog = CommonUtils.createProgressDialog(getContext(), "Loading...");
                    progressDialog.show();
                }
            }
        });

        return view;
    }


    public void onMakeOfferSuccess(){
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Success");
        alertDialog.setMessage("Your reservation has been booked");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(getContext(), MainActivity.class);
                        getActivity().startActivity(i);
                    }
                });
        alertDialog.show();
    }

    public void onFailed(String error){
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if ((requestCode == REQUEST_PLACE_PICKER_FOR_FROM_ADDRESS || requestCode == REQUEST_PLACE_PICKER_FOR_TO_ADDRESS)
                && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, getActivity());

            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }

            switch (requestCode){
                case (REQUEST_PLACE_PICKER_FOR_FROM_ADDRESS):
                    mFromAddressText.setText(name);
                    break;
                case (REQUEST_PLACE_PICKER_FOR_TO_ADDRESS):
                    mToAddressText.setText(name);
                    break;
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}

class OfferInput{
    private double start_longitude;
    private double start_latitude;
    private double target_longitude;
    private double target_latitude;
    private int seats_available;
    private String car_plate;
    private long milliseconds;

    public double getStart_longitude() {
        return start_longitude;
    }

    public void setStart_longitude(double start_longitude) {
        this.start_longitude = start_longitude;
    }

    public double getStart_latitude() {
        return start_latitude;
    }

    public void setStart_latitude(double start_latitude) {
        this.start_latitude = start_latitude;
    }

    public double getTarget_longitude() {
        return target_longitude;
    }

    public void setTarget_longitude(double target_longitude) {
        this.target_longitude = target_longitude;
    }

    public double getTarget_latitude() {
        return target_latitude;
    }

    public void setTarget_latitude(double target_latitude) {
        this.target_latitude = target_latitude;
    }

    public int getSeats_available() {
        return seats_available;
    }

    public void setSeats_available(int seats_available) {
        this.seats_available = seats_available;
    }

    public String getCar_plate() {
        return car_plate;
    }

    public void setCar_plate(String car_plate) {
        this.car_plate = car_plate;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }
}
