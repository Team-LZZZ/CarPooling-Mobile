package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs528.lzzz.carpooling_mobile.model.Car;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.CarPool;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.Location;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.Offerer;

public class SearchFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;

    private RecyclerView mRecyclerView;

    private MyAdapter myAdapter;

    private static List<CarPool> carPools = new ArrayList<>();

    static {
        Car car = new Car("Acura", "TSX", "4XZ319",  5);
        Offerer o1 = new Offerer();
        o1.setUsername("weihao");
        Offerer o2 = new Offerer();
        o2.setUsername("Tom");
        Location l1 = new Location("28 N Ashland");
        Location l2 = new Location("WPI Library");
        CarPool carPool1 = new CarPool();
        carPool1.setStartLocation(l1);
        carPool1.setTargetLocation(l2);
        carPool1.setCar(car);
        carPool1.setOfferer(o1);
        CarPool carPool2 = new CarPool();
        carPool2.setStartLocation(l2);
        carPool2.setTargetLocation(l1);
        carPool2.setCar(car);
        carPool2.setOfferer(o2);
        carPools.add(carPool1);
        carPools.add(carPool2);
    }
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
        // 拿到RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        // 设置LinearLayoutManager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 设置ItemAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // 设置固定大小
        mRecyclerView.setHasFixedSize(true);
        // 初始化自定义的适配器
        myAdapter = new MyAdapter(getActivity(), carPools);
        // 为mRecyclerView设置适配器
        mRecyclerView.setAdapter(myAdapter);
//        for (String name : names){
//            mRecyclerView.scrollToPosition(myAdapter.getItemCount() - 1);
//            myAdapter.notifyDataSetChanged();
//        }

        return view;
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
