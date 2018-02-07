package com.axemorgan.genconcatalogue.event_list;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axemorgan.genconcatalogue.CatalogueApplication;
import com.axemorgan.genconcatalogue.R;
import com.axemorgan.genconcatalogue.SearchContract;
import com.axemorgan.genconcatalogue.components.NumberFormats;
import com.axemorgan.genconcatalogue.event_detail.EventDetailActivity;
import com.axemorgan.genconcatalogue.events.Event;
import com.axemorgan.genconcatalogue.events.EventUpdateBroadcastReceiver;
import com.axemorgan.genconcatalogue.events.EventUpdateService;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventListFragment extends Fragment implements EventListContract.View, EventsAdapter.Listener,
        SearchView.OnCloseListener, SearchView.OnQueryTextListener, SearchContract.View,
        EventUpdateBroadcastReceiver.Listener {

    public interface DrawerHost {
        void openDrawer();
    }

    public static EventListFragment create() {
        return new EventListFragment();
    }


    @BindView(R.id.event_list_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.event_list_empty_view)
    TextView emptyView;
    @BindView(R.id.event_list_result_count)
    TextView resultCountView;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.loading_bottom_sheet)
    View bottomSheet;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    EventListContract.Presenter eventListPresenter;
    @Inject
    SearchContract.Presenter searchPresenter;

    private BottomSheetBehavior bottomSheetBehavior;
    private EventUpdateBroadcastReceiver receiver;
    private DrawerHost drawerHost;
    private MenuItem searchMenuItem;
    private EventsAdapter adapter;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        drawerHost = (DrawerHost) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CatalogueApplication.get(this.getContext()).getAppComponent().inject(this);
        this.setHasOptionsMenu(true);
        receiver = new EventUpdateBroadcastReceiver(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        adapter = new EventsAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        searchView.setOnCloseListener(this);
        searchView.setOnQueryTextListener(this);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        eventListPresenter.attachView(this);
        searchPresenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        receiver.register(this.getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        receiver.unregister(this.getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        eventListPresenter.detachView();
        searchPresenter.detachView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
        searchMenuItem = menu.findItem(R.id.menu_search);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search: {
                this.showSearchBar();
                return true;
            }
            case R.id.menu_filter: {
                drawerHost.openDrawer();
                return true;
            }
            case R.id.menu_update_events: {
                this.getContext().startService(EventUpdateService.getIntent(this.getContext()));
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void showEvents(List<Event> events) {
        adapter.setEvents(events);
        resultCountView.setText(getResources().getQuantityString(R.plurals.event_list_search_result_count, events.size(), NumberFormats.INSTANCE.format(events.size())));

        recyclerView.setVisibility(View.VISIBLE);
        resultCountView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void showNoEventsFound() {
        recyclerView.setVisibility(View.GONE);
        resultCountView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onEventPressed(Event event) {
        eventListPresenter.onViewDetails(event);
    }

    @Override
    public void navigateToEventDetail(Event event) {
        this.startActivity(EventDetailActivity.forEvent(this.getContext(), event.getId()));
    }

    /**
     * Called when the SearchView is closed
     */
    @Override
    public boolean onClose() {
        this.hideSearchBar();
        searchPresenter.onSearchClosed();
        return true;
    }

    @Override
    public String getSearchText() {
        return searchView.getQuery().toString();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchPresenter.performSearch();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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

    @Override
    public void onUpdateStarted() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onUpdateFinished() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
