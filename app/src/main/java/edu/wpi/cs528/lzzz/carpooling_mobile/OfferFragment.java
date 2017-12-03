package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OfferFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private static final int REQUEST_PLACE_PICKER_FOR_FROM_ADDRESS = 1;
    private static final int REQUEST_PLACE_PICKER_FOR_TO_ADDRESS = 2;

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
        return view;
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
