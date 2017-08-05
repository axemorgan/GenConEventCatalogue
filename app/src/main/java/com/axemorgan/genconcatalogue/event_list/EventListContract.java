package com.axemorgan.genconcatalogue.event_list;

import com.axemorgan.genconcatalogue.components.AbstractPresenter;

public interface EventListContract {
    interface View {
        void showEvents();
    }

    abstract class Presenter extends AbstractPresenter<View> {

    }
}
