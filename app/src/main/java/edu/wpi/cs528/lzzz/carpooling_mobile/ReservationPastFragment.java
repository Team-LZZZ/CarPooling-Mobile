package edu.wpi.cs528.lzzz.carpooling_mobile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ReservationPastFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i("Res  Past  Fragment", "================");
        return inflater.inflate(R.layout.fragment_reservation_past, container, false);
    }
}