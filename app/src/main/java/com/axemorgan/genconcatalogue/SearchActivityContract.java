package com.axemorgan.genconcatalogue;

import com.axemorgan.genconcatalogue.components.AbstractPresenter;

public interface SearchActivityContract {

    interface View {

        String getSearchText();
    }

    abstract class Presenter extends AbstractPresenter<View> {

        abstract void performSearch();

        abstract void onSearchClosed();
    }

}
