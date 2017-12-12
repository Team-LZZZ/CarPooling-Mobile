package edu.wpi.cs528.lzzz.carpooling_mobile;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;


import java.text.DateFormat;
import java.util.Calendar;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.GregorianCalendar;


import butterknife.Bind;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.LogInHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.CarPool;


public class OfferCarpoolActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private final String TAG = "OfferCarpoolctivity";
    private static final int REQUEST_SIGNUP = 0;
    private LogInHandler logInHandler;
    private ProgressDialog progressDialog;
    private Gson gson = new Gson();
    private CarPool carPool;

    @Bind(R.id.make_input)
    EditText _carMakeText;
    @Bind(R.id.model_input)
    EditText _carModelText;
    @Bind(R.id.plate_input)
    Button _carPlate_input;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_carpool);



    }


    public void datePicker(View view){

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

        ((TextView) findViewById(R.id.carppool_day))
                .setText(dateFormat.format(calendar.getTime()));

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


}
