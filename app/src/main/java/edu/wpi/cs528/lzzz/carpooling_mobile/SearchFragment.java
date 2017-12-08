package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.CarpoolsHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.IConnectionStatus;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.Car;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.CarPool;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.Location;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.Offerer;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonUtils;

public class SearchFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;
    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;
    private ProgressDialog progressDialog;
    private CarpoolsHandler carpoolsHandler;

    public SearchFragment() {
    }

    public static SearchFragment newInstance(int pageNo) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        myAdapter = new MyAdapter(getActivity(), AppContainer.getInstance().getCarPools());
        mRecyclerView.setAdapter(myAdapter);

        carpoolsHandler = new CarpoolsHandler(new IConnectionStatus() {
            @Override
            public void onComplete(Boolean isSuccess, String additionalInfos) {
                progressDialog.dismiss();
                if (isSuccess){
                    onGetAllCarpoolsSuccess();
                }else{
                    onFailed(additionalInfos);
                }
            }
        });
        if(AppContainer.getInstance().isLogIn()){
            carpoolsHandler.connectForResponse(CommonUtils.createHttpGETRequestMessageWithToken(CommonConstants.getAllCarpools));
            progressDialog = CommonUtils.createProgressDialog(getContext(), "Loading...");
            progressDialog.show();
        }
        return view;
    }


    private void onGetAllCarpoolsSuccess(){
        myAdapter.notifyDataSetChanged();
    }

    private void onFailed(String error){
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
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
