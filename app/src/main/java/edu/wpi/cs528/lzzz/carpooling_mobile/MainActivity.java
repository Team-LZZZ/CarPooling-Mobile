package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.CarpoolsHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.ConnectionHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.IConnectionStatus;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.SignUpHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.User;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Toolbar.OnMenuItemClickListener {
    private RecyclerView mRecycleView;
    private MainActivityAdapter mainActivityAdapter;
    private CarpoolsHandler carpoolsHandler;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressDialog progressDialog;
    TextView name;
    TextView phone;
    TextView email;

    public User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!AppContainer.getInstance().isLogIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        mRecycleView = (RecyclerView) findViewById(R.id.list);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(layoutManager);


//        progressDialog = CommonUtils.createProgressDialog(this, "loading...");
//        progressDialog.show();
        CarpoolsHandler carpoolsHandler = new CarpoolsHandler(new IConnectionStatus() {
            @Override
            public void onComplete(Boolean isSuccess, String additionalInfos) {
//               progressDialog.dismiss();
                if (isSuccess){
                    onGetAllCarpoolSuccess();
                }else{
                    onGetAllCarpoolFailed(additionalInfos);
                }
            }
        });
        carpoolsHandler.connectForResponse(CommonUtils.createHttpGETRequestMessageWithToken(CommonConstants.getAllCarpools));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
//        mUser = AppContainer.getInstance().getActiveUser();

        Log.i("User", "======================");
        ImageView photo = (ImageView) header.findViewById(R.id.imageView);
        name = (TextView) header.findViewById(R.id.name);
        phone = (TextView) header.findViewById(R.id.phone);
        email = (TextView) header.findViewById(R.id.email);
        //photo.setImageURI(Uri.parse(AppContainer.getInstance().getActiveUser().getPhoto()));


        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        Log.i("start", "========");
        super.onStart();
        this.updateUserDisplay();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.offer) {
            Intent intent = new Intent(MainActivity.this, OfferCarpoolActivity.class);
            startActivity(intent);

        } else if (id == R.id.reservation) {
            Intent intent = new Intent(MainActivity.this, ReservationActivity.class);
            startActivity(intent);

        } else if (id == R.id.myoffer) {
            Intent intent = new Intent(MainActivity.this, OfferListActivity.class);
            startActivity(intent);

        } else if (id == R.id.profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.about_us) {

        } else if (id == R.id.contact) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateUserDisplay() {
        mUser = AppContainer.getInstance().getActiveUser();
        name.setText(mUser.getUsername());
        phone.setText(mUser.getPhone());
        email.setText(mUser.getEmail());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_map) {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        }
        return true;
    }

    private void onGetAllCarpoolSuccess(){
        mainActivityAdapter = new MainActivityAdapter(AppContainer.getInstance().getCarPools(), this);
        mRecycleView.setAdapter(mainActivityAdapter);
        mainActivityAdapter.notifyDataSetChanged();
    }
    private void onGetAllCarpoolFailed(String error){
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}
