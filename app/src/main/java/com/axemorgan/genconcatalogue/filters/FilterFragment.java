package com.axemorgan.genconcatalogue.filters;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.axemorgan.genconcatalogue.CatalogueApplication;
import com.axemorgan.genconcatalogue.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FilterFragment extends Fragment implements FilterContract.View, AdapterView.OnItemSelectedListener {

    @Inject
    FilterContract.Presenter presenter;

    @BindView(R.id.filters_event_type_spinner)
    Spinner eventTypeSpinner;

    private ArrayAdapter<String> eventTypeAdapter;
    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CatalogueApplication.get(this.getContext()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventTypeAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1);
        eventTypeSpinner.setAdapter(eventTypeAdapter);
        eventTypeSpinner.setOnItemSelectedListener(this);
        presenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showEventTypes(List<String> types) {
        eventTypeAdapter.clear();
        eventTypeAdapter.addAll(types);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        presenter.onEventTypeFilterSelected((String) adapterView.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
