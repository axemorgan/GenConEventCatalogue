package com.axemorgan.genconcatalogue.filters;

import com.axemorgan.genconcatalogue.components.AbstractPresenter;

import java.util.List;

public interface FilterContract {
    interface View {

        void showEventTypes(List<String> types);
    }

    public abstract class Presenter extends AbstractPresenter<View> {

        public abstract void onEventTypeFilterSelected(String type);
    }
}
