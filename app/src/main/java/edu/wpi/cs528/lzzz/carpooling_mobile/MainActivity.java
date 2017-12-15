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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.CarpoolsHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.ConnectionHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.IConnectionStatus;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.SignUpHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.CarPool;
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
    NavigationView navigationView;
    private TextView searchCriteriaTextView;
    public static final int MAKE_RESERVATION = 1;

    private int loginCode = 0;
//    TextView name;
//    TextView phone;
//    TextView email;

//    public User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!AppContainer.getInstance().isLogIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, loginCode);
        }
        searchCriteriaTextView = (TextView) findViewById(R.id.search_criteria);
        searchCriteriaTextView.setText(AppContainer.getInstance().getSearchCriteriaDisplayContent());
        searchCriteriaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppContainer.getInstance().setSearchCriteriaDisplayContent("");
                finish();
                startActivity(getIntent());
            }
        });
        carpoolsHandler = new CarpoolsHandler(new IConnectionStatus() {
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

        mRecycleView = (RecyclerView) findViewById(R.id.list);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(layoutManager);


//        progressDialog = CommonUtils.createProgressDialog(this, "loading...");
//        progressDialog.show();


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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        updateUserDisplay();
    }

    @Override
    protected void onStart() {
        Log.i("start", "========");
        super.onStart();

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
            Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(intent);

        } else if (id == R.id.contact) {
            Intent intent = new Intent(MainActivity.this, ContactActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateUserDisplay() {
        View header = navigationView.getHeaderView(0);
        ImageView photo = (ImageView) header.findViewById(R.id.imageView);
        TextView name = (TextView) header.findViewById(R.id.name);
        TextView phone = (TextView) header.findViewById(R.id.phone);
        TextView email = (TextView) header.findViewById(R.id.email);
        //photo.setImageURI(Uri.parse(AppContainer.getInstance().getActiveUser().getPhoto()));

        User mUser = AppContainer.getInstance().getActiveUser();
        name.setText(mUser.getUsername());
        CommonUtils.getProfile(this,mUser.getPhoto(), photo);
        phone.setText( CommonUtils.formatPhoneNumber(mUser.getPhone()));
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
        updateCarPoolsDisplay();
    }
    private void onGetAllCarpoolFailed(String error){
//        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == this.loginCode){
            updateCarPoolsDisplay();
            this.updateUserDisplay();
        }
    }

    private void updateCarPoolsDisplay(){

        if (AppContainer.getInstance().getSearchCriteriaDisplayContent().equals("")){
            mainActivityAdapter = new MainActivityAdapter(MAKE_RESERVATION, CommonUtils.getAvailabeRes(), this);
            mRecycleView.setAdapter(mainActivityAdapter);
            mainActivityAdapter.notifyDataSetChanged();
        }else{
            searchCriteriaTextView.setText(AppContainer.getInstance().getSearchCriteriaDisplayContent());
            mainActivityAdapter = new MainActivityAdapter(MAKE_RESERVATION, AppContainer.getInstance().getSearchResult(), this);
            mRecycleView.setAdapter(mainActivityAdapter);
            mainActivityAdapter.notifyDataSetChanged();
        }
    }

}
