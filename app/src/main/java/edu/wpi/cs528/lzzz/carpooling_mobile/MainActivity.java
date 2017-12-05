package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpRequestMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.ConnectionHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.SignUpHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;

public class MainActivity extends AppCompatActivity{

    private Button testButton;
    private TextView textView;
    private TabLayout mTabLayout;

    private int[] mTabsIcons = {
            R.drawable.message,
            R.drawable.chat,
            R.drawable.notification};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

//        final ConnectionHandler connectionHandler = new ConnectionHandler(this);
//
//        testButton = (Button) findViewById(R.id.testConnectionBtn);
//
//        testButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HttpRequestMessage request = new HttpRequestMessage();
//                request.setMethod("POST");
//                request.addParam("name", "morpheus");
//                request.addParam("job", "worker");
//                request.setUrl("https://reqres.in/api/users");
////                request.setMethod("GET");
////                request.setUrl("https://reqres.in/api/users/2");
//                connectionHandler.connectForResponse(request);
//            }
//        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        if (viewPager != null)
            viewPager.setAdapter(pagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(viewPager);

            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                if (tab != null)
                    tab.setCustomView(pagerAdapter.getTabView(i));
            }

            mTabLayout.getTabAt(0).getCustomView().setSelected(true);
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public final int PAGE_COUNT = 3;

        private final String[] mTabsTitle = {"Pending", "Messages", "Notifications"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public View getTabView(int position) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.toolbar, null);
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            icon.setImageResource(mTabsIcons[position]);
            return view;
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return SearchFragment.newInstance(1);

                case 1:
                    return OfferFragment.newInstance(2);
                case 2:
                    return MeFragment.newInstance(3);
            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabsTitle[position];
        }
    }
}
