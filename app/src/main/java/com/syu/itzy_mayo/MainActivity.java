package com.syu.itzy_mayo;

import android.annotation.SuppressLint;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.syu.itzy_mayo.Goal.GoalPagerFragment;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.naver.maps.map.NaverMapSdk;

public class MainActivity extends AppCompatActivity
        implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after
     * returning in {@link
     * #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean permissionDenied = false;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private final MapFragment mapFragment = new MapFragment();
    private final FeedFragment feedFragment = new FeedFragment();
    private final CalendarFragment calendarFragment = new CalendarFragment();
    private GoalPagerFragment goalPagerFragment = new GoalPagerFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.menu_frame_layout, mapFragment).commitAllowingStateLoss();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        // 네이버 지도 SDK 초기화
        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NcpKeyClient(BuildConfig.NCP_CLIENT_ID));
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            if (menuItem.getItemId() == R.id.nav_home) {
                transaction.replace(R.id.menu_frame_layout, mapFragment).commitAllowingStateLoss();
            } else if (menuItem.getItemId() == R.id.nav_feed) {
                transaction.replace(R.id.menu_frame_layout, feedFragment).commitAllowingStateLoss();
            } else if (menuItem.getItemId() == R.id.nav_calander) {
                transaction.replace(R.id.menu_frame_layout, calendarFragment).commitAllowingStateLoss();
            }else if (menuItem.getItemId() == R.id.nav_goal) {
                transaction.replace(R.id.menu_frame_layout, goalPagerFragment).commitAllowingStateLoss();
            }

            return true;
        }
    }
}