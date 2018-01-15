package com.axemorgan.genconcatalogue.filters;

import com.axemorgan.genconcatalogue.SearchModel;
import com.axemorgan.genconcatalogue.events.EventDao;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import timber.log.Timber;

public class FilterPresenter extends FilterContract.Presenter {

    private final EventDao eventDao;
    private final SearchModel searchModel;

    @Inject
    public FilterPresenter(EventDao eventDao, SearchModel searchModel) {
        this.eventDao = eventDao;
        this.searchModel = searchModel;
    }

    @Override
    protected void onViewAttached() {
        eventDao.getAllEventTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<String>>() {
                    @Override
                    public void onNext(List<String> types) {
                        if (getView() != null) {
                            getViewOrThrow().showEventTypes(types);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Timber.e(t);
                    }

                    @Override
                    public void onComplete() {
                        Timber.i("Subscription completed.");
                    }
                });
    }

    @Override
    public void onEventTypeFilterSelected(String type) {
        Timber.i("Filtering on Event Type: %s", type);
        searchModel.setEventTypeFilter(type);
    }
}
