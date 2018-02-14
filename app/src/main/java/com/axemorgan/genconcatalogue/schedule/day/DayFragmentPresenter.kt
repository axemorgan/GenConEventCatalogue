package com.axemorgan.genconcatalogue.schedule.day

import com.axemorgan.genconcatalogue.events.EventDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DayFragmentPresenter @Inject constructor(val eventDao: EventDao) : DayContract.Presenter() {

    override fun onViewAttached() {
        eventDao.getSavedEventsForDay(viewOrThrow.getDay().startTime, viewOrThrow.getDay().endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewOrThrow.showLoading() }
                .subscribe { events ->
                    if (events.size > 0) {
                        view?.showEvents(events)
                    } else {
                        view?.showEmpty()
                    }
                }
    }
}
