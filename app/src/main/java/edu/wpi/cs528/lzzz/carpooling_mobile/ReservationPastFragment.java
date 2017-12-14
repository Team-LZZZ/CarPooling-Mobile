package edu.wpi.cs528.lzzz.carpooling_mobile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonUtils;


public class ReservationPastFragment extends Fragment {
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager layoutManager;
    private MainActivityAdapter mainActivityAdapter;
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;
    public static final int VIEW_PAST_RESERVATION = 2;

    public static ReservationPastFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        ReservationPastFragment fragment = new ReservationPastFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPageNo = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i("Res  Past  Fragment", "================");
        View view = inflater.inflate(R.layout.fragment_reservation_past, container, false);

        mRecycleView = view.findViewById(R.id.res_past_list);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getContext());
        mRecycleView.setLayoutManager(layoutManager);

        mainActivityAdapter = new MainActivityAdapter(VIEW_PAST_RESERVATION, AppContainer.getInstance().getCarPools(), getContext());
        mRecycleView.setAdapter(mainActivityAdapter);
        mainActivityAdapter.notifyDataSetChanged();
        return view;

    }
}