package com.axemorgan.genconcatalogue.event_list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axemorgan.genconcatalogue.CatalogueApplication;
import com.axemorgan.genconcatalogue.R;
import com.axemorgan.genconcatalogue.event_detail.EventDetailActivity;
import com.axemorgan.genconcatalogue.events.Event;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventListFragment extends Fragment implements EventListContract.View, EventsAdapter.Listener {

    public static EventListFragment create() {
        return new EventListFragment();
    }


    @BindView(R.id.event_list_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.event_list_empty_view)
    TextView emptyView;

    @Inject
    EventListContract.Presenter presenter;

    private EventsAdapter adapter;
    private Unbinder unbinder;


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

        adapter = new EventsAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        CatalogueApplication.get(this.getContext()).getAppComponent().inject(this);

        presenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showEvents(List<Event> events) {
        adapter.setEvents(events);

        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void showNoEventsFound() {
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onEventPressed(Event event) {
        presenter.onViewDetails(event);
    }

    @Override
    public void navigateToEventDetail(Event event) {
        this.startActivity(EventDetailActivity.forEvent(this.getContext(), event.getId()));
    }
}
