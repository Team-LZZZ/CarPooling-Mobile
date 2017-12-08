package edu.wpi.cs528.lzzz.carpooling_mobile;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;

import edu.wpi.cs528.lzzz.carpooling_mobile.connection.HttpRequestMessage;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.ConnectionHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.handlers.SignUpHandler;
import edu.wpi.cs528.lzzz.carpooling_mobile.model.AppContainer;
import edu.wpi.cs528.lzzz.carpooling_mobile.utils.CommonConstants;

public class MainActivity extends AppCompatActivity{

    private Button testButton;
    private TextView textView;
    private TabLayout mTabLayout;

    private int[] mTabsIcons = {
            R.drawable.message,
            R.drawable.address,
            R.drawable.chat,
            R.drawable.notification};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!AppContainer.getInstance().isLogIn()){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

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
        private final String[] mTabsTitle = {"CarpoolList", "CarpoolMap", "CarpoolOffer", "Myself"};
        public final int PAGE_COUNT = mTabsTitle.length;

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
            Bundle args = new Bundle();
            switch (pos) {
                case 0:
                    return SearchFragment.newInstance(1);
                case 1:
                    return CarpoolMapFragment.newInstance(2);
                case 2:
                    return OfferFragment.newInstance(3);
                case 3:
                    return MeFragment.newInstance(4);
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
