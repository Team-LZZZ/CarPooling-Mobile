package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.CarPool;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;

public class MapActivity extends FragmentActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnInfoWindowClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback,
        com.google.android.gms.location.LocationListener,
        GoogleMap.OnMarkerClickListener {




    public static final String ARG_PAGE = "ARG_PAGE";
    public int mPageNo;
    private SupportMapFragment mapFragment;
    private GoogleApiClient mApiClient;
    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;
    private List<CarPool> carPools;
    private Map<Marker, CarPool> markers;

    public MapActivity() {
    }

//    public static MapActivity newInstance(int pageNo) {
//        Bundle args = new Bundle();
//        args.putInt(ARG_PAGE, pageNo);
//        MapActivity fragment = new MapActivity();
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mApiClient.connect();
        carPools = AppContainer.getInstance().getCarPools();
        markers = new HashMap<>();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_map, null, false);
//
//        mApiClient = new GoogleApiClient.Builder(getContext())
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//        mApiClient.connect();
//        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        carPools = AppContainer.getInstance().getCarPools();
//        markers = new HashMap<>();
//        return view;
//    }

    //
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//
//    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            for (CarPool carPool : carPools){
                createCarPoolMarker(carPool);
            }
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setOnInfoWindowClickListener(this);
            mGoogleMap.setOnMarkerClickListener(this);
            mGoogleMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
        }
    }

    public void getCurrentMap(Location location){

        if (mGoogleMap != null){
            mGoogleMap.clear();
            LatLng currentGPS = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentGPS, CommonConstants.MAP_CAMERA));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        CarPool carPool = markers.get(marker);
        Intent i = new Intent(this, ReservationActivity.class);
        i.putExtra("carPoolId", carPool.getOid());
        i.putExtra("reserveMode", true);
        startActivity(i);
    }

    private Marker createCarPoolMarker(CarPool carPool) {
        Log.i(CommonConstants.LogPrefix, carPool.getOid() + "");

        Marker marker = mGoogleMap.addMarker(
                new MarkerOptions()
                        .position(carPool.getStartLocation().getLatLng())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.tires_accessories))
                        .title(carPool.getStartLocation().getName())
        );
        markers.put(marker, carPool);
        return marker;
    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private TextView mTitle;
        private TextView mDescription;
        private View v;
        public MarkerInfoWindowAdapter()
        {
            v  = getLayoutInflater().inflate(R.layout.custom_info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker)
        {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker)
        {
            mTitle = v.findViewById(R.id.bubble_title);
            mTitle.setText(marker.getTitle());
            mDescription = v.findViewById(R.id.bubble_description);
            mDescription.setText("safasfsfsadf");

            return v;
        }
    }

}