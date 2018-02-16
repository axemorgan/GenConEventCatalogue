package com.axemorgan.genconcatalogue.filters;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.axemorgan.genconcatalogue.CatalogueApplication;
import com.axemorgan.genconcatalogue.R;
import com.axemorgan.genconcatalogue.components.SpinnerItem;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnItemSelected;
import butterknife.Unbinder;


public class FilterFragment extends Fragment implements FilterContract.View {

    @Inject
    FilterContract.Presenter presenter;

    @BindView(R.id.filters_event_type_spinner)
    Spinner eventTypeSpinner;
    @BindView(R.id.filters_age_requirement_spinner)
    Spinner ageRequirementSpinner;
    @BindView(R.id.filters_only_available_checkbox)
    CheckBox noSoldOutEventsCheckbox;

    private ArrayAdapter<SpinnerItem<String>> eventTypeAdapter;
    private ArrayAdapter<SpinnerItem<String>> ageRequirementAdapter;
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

        ageRequirementAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1);
        ageRequirementSpinner.setAdapter(ageRequirementAdapter);

        presenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showEventTypeFilters(List<SpinnerItem<String>> types) {
        eventTypeAdapter.clear();
        eventTypeAdapter.addAll(types);
    }

    @Override
    public void showAgeRequirementFilters(List<SpinnerItem<String>> items) {
        ageRequirementAdapter.clear();
        ageRequirementAdapter.addAll(items);
    }

    @OnItemSelected(R.id.filters_event_type_spinner)
    public void onEventTypeItemSelected(int position) {
        SpinnerItem<String> item = eventTypeAdapter.getItem(position);
        if (item != null) {
            presenter.onEventTypeFilterSelected(item.getValue());
        }
    }

    @OnCheckedChanged(R.id.filters_only_available_checkbox)
    public void onOnlyAvailableEventsChecked(boolean checked) {
        
    }

    @OnItemSelected(R.id.filters_age_requirement_spinner)
    public void onAgeRequirementItemSelected(int position) {
        SpinnerItem<String> item = ageRequirementAdapter.getItem(position);
        if (item != null) {
            presenter.onAgeRequirementFilterSelected(item.getValue());
        }
    }
}
