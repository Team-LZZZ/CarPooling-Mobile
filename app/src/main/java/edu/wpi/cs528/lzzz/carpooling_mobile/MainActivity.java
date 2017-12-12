package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;

import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpRequestMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.CarpoolsHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.ConnectionHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.SignUpHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView mRecycleView;
    private MainActivityAdapter mainActivityAdapter;
    private CarpoolsHandler carpoolsHandler;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!AppContainer.getInstance().isLogIn()){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        mRecycleView = (RecyclerView) findViewById(R.id.list);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(layoutManager);
        mainActivityAdapter = new MainActivityAdapter(AppContainer.getInstance().getCarPools());
        mRecycleView.setAdapter(mainActivityAdapter);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        navigationView.setNavigationItemSelectedListener(this);
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
}
