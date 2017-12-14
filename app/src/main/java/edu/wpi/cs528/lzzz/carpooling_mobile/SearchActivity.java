package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.ISearchStatus;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.SearchHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;

public class SearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView searchTargetAddressText;
    private TextView searchDateText;

    private double targetAddressLattitude;
    private double targetAddressLongitude;
    private String targetAddressName;

    private Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);

        searchTargetAddressText = (TextView) findViewById(R.id.to_address);
        searchDateText = (TextView) findViewById(R.id.search_day);
        searchBtn = (Button) findViewById(R.id.search_button_filter);

        View.OnClickListener selectAddressListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {

                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(SearchActivity.this);
                    startActivityForResult(intent, 0);

                } catch (Exception ex) {

                }
            }
        };
        searchTargetAddressText.setOnClickListener(selectAddressListener);


//        final String targetAddress = searchTargetAddressText.getText().toString();
//        String searchDateString = searchDateText.getText().toString();
//        long searchMilliseconds =

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("+============", "searchbutton clicked.");
                ISearchStatus searchStatus = new ISearchStatus() {
                    @Override
                    public void onSearchComplete() {
                        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                };
                final String targetAddress = searchTargetAddressText.getText().toString();
                SearchHandler.performSearch(targetAddress, searchStatus);
            }
        });
    }


    public void datePicker(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        this.searchDateText.setText(dateFormat.format(calendar.getTime()));

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(data, this);
            final CharSequence nameChar = place.getName();
            final CharSequence addressChar = place.getAddress();
            String name = nameChar.toString();
            String address = addressChar.toString();
            if (name.contains("\"")) {
                name = transferLatLngToAddress(address);
            }
            Log.i(CommonConstants.LogPrefix, address);
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }


            searchTargetAddressText.setText(name);
            this.targetAddressName = name;
            this.targetAddressLattitude = place.getLatLng().latitude;
            this.targetAddressLongitude = place.getLatLng().longitude;

        }
    }

    public String transferLatLngToAddress(String address) {
        String[] addressInfo = address.split(",");
        return addressInfo[0] + "," + addressInfo[1];
    }


}
