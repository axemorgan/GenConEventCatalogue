package com.axemorgan.genconcatalogue;

import com.axemorgan.genconcatalogue.components.AbstractPresenter;

public interface SearchContract {

    interface View {

        String getSearchText();
    }

    abstract class Presenter extends AbstractPresenter<View> {

        public abstract void performSearch();

        public abstract void onSearchClosed();
    }

}
