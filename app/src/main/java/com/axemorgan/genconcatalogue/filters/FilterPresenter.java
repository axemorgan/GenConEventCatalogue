package com.axemorgan.genconcatalogue.filters;

import com.axemorgan.genconcatalogue.SearchModel;
import com.axemorgan.genconcatalogue.components.SpinnerItem;
import com.axemorgan.genconcatalogue.events.EventDao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import timber.log.Timber;

public class FilterPresenter extends FilterContract.Presenter {

    private static final String NO_EVENT_TYPE_FILTER = "";

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
                            getViewOrThrow().showEventTypeFilters(getEventTypeSpinnerItems(types));
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

    private List<SpinnerItem<String>> getEventTypeSpinnerItems(List<String> eventTypes) {
        ArrayList<SpinnerItem<String>> types = new ArrayList<>(eventTypes.size() + 1);
        for (String eventType : eventTypes) {
            types.add(new SpinnerItem<>(eventType, eventType));
        }
        types.add(new SpinnerItem<>("All Event Types", NO_EVENT_TYPE_FILTER));
        return types;
    }
}
