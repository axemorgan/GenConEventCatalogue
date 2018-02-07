package com.axemorgan.genconcatalogue;

import javax.inject.Inject;

public class SearchPresenter extends SearchContract.Presenter {

    private final SearchModel searchModel;

    @Inject
    public SearchPresenter(SearchModel searchModel) {
        this.searchModel = searchModel;
    }

    @Override
    public void performSearch() {
        searchModel.setQuery(this.getViewOrThrow().getSearchText());
    }

    @Override
    public void onSearchClosed() {
        searchModel.setQuery("");
    }
}
