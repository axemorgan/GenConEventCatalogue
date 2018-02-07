package com.axemorgan.genconcatalogue;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;

import com.axemorgan.genconcatalogue.event_list.EventListFragment;
import com.axemorgan.genconcatalogue.schedule.ScheduleFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements EventListFragment.DrawerHost {

    private static final String TAG_EVENT_LIST = "EVENT_LIST";
    private static final String TAG_SCHEDULE = "SCHEDULE";

    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                showListFragment();
                return true;
            case R.id.navigation_schedule:
                showScheduleFragment();
                return true;
        }
        return false;
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            this.showListFragment();
        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        CatalogueApplication.get(this).getAppComponent().inject(this);
    }

    private void showListFragment() {
        EventListFragment eventListFragment = (EventListFragment) getSupportFragmentManager().findFragmentByTag(TAG_EVENT_LIST);
        if (eventListFragment == null) {
            eventListFragment = EventListFragment.create();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, eventListFragment, TAG_EVENT_LIST)
                .addToBackStack(null)
                .commit();
    }

    private void showScheduleFragment() {
        ScheduleFragment scheduleFragment = (ScheduleFragment) getSupportFragmentManager().findFragmentByTag(TAG_SCHEDULE);
        if (scheduleFragment == null) {
            scheduleFragment = ScheduleFragment.Factory.create();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, scheduleFragment, TAG_SCHEDULE)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openDrawer() {
        drawerLayout.openDrawer(Gravity.END);
    }
}
