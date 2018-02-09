package com.axemorgan.genconcatalogue.schedule

import com.axemorgan.genconcatalogue.events.EventDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DayFragmentPresenter @Inject constructor(val eventDao: EventDao) : DayContract.Presenter() {

    override fun onViewAttached() {
        eventDao.allSavedEvents
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { events -> view?.showEvents(events) }
    }
}
