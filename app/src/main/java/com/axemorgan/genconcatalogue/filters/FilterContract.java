package com.axemorgan.genconcatalogue.filters;

import com.axemorgan.genconcatalogue.components.AbstractPresenter;
import com.axemorgan.genconcatalogue.components.SpinnerItem;

import java.util.List;

public interface FilterContract {
    interface View {

        void showEventTypeFilters(List<SpinnerItem<String>> types);

        void showAgeRequirementFilters(List<SpinnerItem<String>> items);
    }

    abstract class Presenter extends AbstractPresenter<View> {

        public abstract void onEventTypeFilterSelected(String type);

        public abstract void onAgeRequirementFilterSelected(String item);
    }
}
