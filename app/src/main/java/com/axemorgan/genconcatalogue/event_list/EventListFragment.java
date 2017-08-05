package com.axemorgan.genconcatalogue.event_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axemorgan.genconcatalogue.CatalogueApplication;
import com.axemorgan.genconcatalogue.R;
import com.axemorgan.genconcatalogue.dagger.DaggerEventListComponent;
import com.axemorgan.genconcatalogue.dagger.EventListModule;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventListFragment extends Fragment implements EventListContract.View {

    public static EventListFragment create() {
        return new EventListFragment();
    }

    @BindView(R.id.event_list_recycler_view)
    RecyclerView recyclerView;

    @Inject
    EventListContract.Presenter presenter;

    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(null);

        DaggerEventListComponent.builder()
                .appComponent(CatalogueApplication.get(this.getContext()).getComponent())
                .eventListModule(new EventListModule())
                .build();
        presenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showEvents() {
        //TODO
    }
}
