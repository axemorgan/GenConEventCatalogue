package com.axemorgan.genconcatalogue;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.axemorgan.genconcatalogue.event_list.EventListFragment;
import com.axemorgan.genconcatalogue.events.EventUpdateBroadcastReceiver;
import com.axemorgan.genconcatalogue.events.EventUpdateService;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SearchActivityContract.View,
        EventUpdateBroadcastReceiver.Listener, SearchView.OnCloseListener, SearchView.OnQueryTextListener {

    private static final String TAG_EVENT_LIST = "EVENT_LIST";

    @Inject
    SearchActivityContract.Presenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.loading_bottom_sheet)
    View bottomSheet;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.search_view)
    SearchView searchView;

    MenuItem searchMenuItem;
    MenuItem filterMenuItem;
    MenuItem updateEventsMenuItem;

    private BottomSheetBehavior bottomSheetBehavior;
    private EventUpdateBroadcastReceiver receiver;
    private EventListFragment eventListFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showListFragment();
                    return true;
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().popBackStack();
                    return true;
                case R.id.navigation_notifications:
                    getSupportFragmentManager().popBackStack();
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            this.showListFragment();
        }

        searchView.setOnCloseListener(this);
        searchView.setOnQueryTextListener(this);

        receiver = new EventUpdateBroadcastReceiver(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        CatalogueApplication.get(this).getAppComponent().inject(this);
        presenter.attachView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        receiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        receiver.unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.main, menu);
        filterMenuItem = menu.findItem(R.id.menu_filter);
        searchMenuItem = menu.findItem(R.id.menu_search);
        updateEventsMenuItem = menu.findItem(R.id.menu_update_events);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search: {
                this.showSearchBar();
                return true;
            }
            case R.id.menu_filter: {
                return true;
            }
            case R.id.menu_update_events: {
                this.startService(EventUpdateService.getIntent(this));
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onUpdateStarted() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onUpdateFinished() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void showSearchBar() {
        searchView.setVisibility(View.VISIBLE);
        searchView.setIconified(false);

        searchMenuItem.setVisible(false);
    }

    private void hideSearchBar() {
        searchView.setVisibility(View.GONE);

        searchMenuItem.setVisible(true);
    }

    private void showListFragment() {
        if (eventListFragment == null) {
            eventListFragment = EventListFragment.create();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, eventListFragment, TAG_EVENT_LIST)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Called when the SearchView is closed
     *
     * @return
     */
    @Override
    public boolean onClose() {
        this.hideSearchBar();
        presenter.onSearchClosed();
        return true;
    }

    @Override
    public String getSearchText() {
        return searchView.getQuery().toString();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        presenter.performSearch();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
