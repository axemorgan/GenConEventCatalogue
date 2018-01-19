package com.axemorgan.genconcatalogue.filters;

import com.axemorgan.genconcatalogue.components.AbstractPresenter;
import com.axemorgan.genconcatalogue.components.SpinnerItem;

import java.util.List;

public interface FilterContract {
    interface View {

        void showEventTypeFilters(List<SpinnerItem<String>> types);
    }

    public abstract class Presenter extends AbstractPresenter<View> {

        public abstract void onEventTypeFilterSelected(String type);
    }
}
