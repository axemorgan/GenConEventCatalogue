package com.axemorgan.genconcatalogue;

import javax.inject.Inject;

public class SearchPresenter extends SearchActivityContract.Presenter {

    private final SearchModel searchModel;

    @Inject
    public SearchPresenter(SearchModel searchModel) {
        this.searchModel = searchModel;
    }

    @Override
    void performSearch() {
        searchModel.setQuery(this.getViewOrThrow().getSearchText());
    }
}
