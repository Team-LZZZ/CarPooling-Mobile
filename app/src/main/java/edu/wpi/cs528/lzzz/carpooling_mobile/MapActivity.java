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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.CarPool;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;

public class MapActivity extends AppCompatActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnInfoWindowClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback,
        com.google.android.gms.location.LocationListener,
        GoogleMap.OnMarkerClickListener,
        Toolbar.OnMenuItemClickListener{

    private GoogleApiClient mApiClient;
    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;
    private List<CarPool> carPools;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.map_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
    }

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
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        Log.i(CommonConstants.LogPrefix, id + "");
        if (id == R.id.action_list) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            moveMapCamera(mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            for (CarPool carPool : carPools){
                createCarPoolMarker(carPool);
            }
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setOnInfoWindowClickListener(this);
            mGoogleMap.setOnMarkerClickListener(this);
            mGoogleMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
        }
    }

    public void moveMapCamera(Location location){

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
        Intent i = new Intent(this, ReservationActivity.class);
        i.putExtra("carPoolId", marker.getSnippet());
        i.putExtra("reserveMode", true);
        startActivity(i);
    }

    private Marker createCarPoolMarker(CarPool carPool) {
        String carpoolInfo = carPool.getAvailable() + "," + carPool.getStartLocation().getName() + "," + carPool.getTargetLocation().getName()
                + "," + carPool.getDate();
        Marker marker = mGoogleMap.addMarker(
                new MarkerOptions()
                        .position(carPool.getStartLocation().getLatLng())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.tires_accessories))
                        .title(carpoolInfo)
                        .snippet(String.valueOf(carPool.getOid()))
        );
        return marker;
    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        @Bind(R.id.available_seat_window_textView)
        TextView mAvailableSeat;
        @Bind(R.id.from_address_window_textView)
        TextView mFromAddress;
        @Bind(R.id.to_address_window_textView)
        TextView mToAddress;
        @Bind(R.id.date_window_textview)
        TextView mDate;
        private View v;
        public MarkerInfoWindowAdapter()
        {
            v  = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            ButterKnife.bind(this,v);
        }

        @Override
        public View getInfoWindow(Marker marker)
        {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker)
        {
            String[] carpoolInfoArr = marker.getTitle().split(",");
            mAvailableSeat.setText(carpoolInfoArr[0]);
            mFromAddress.setText(carpoolInfoArr[1]);
            mToAddress.setText(carpoolInfoArr[2]);
            mDate.setText(carpoolInfoArr[3]);
            return v;
        }
    }

}