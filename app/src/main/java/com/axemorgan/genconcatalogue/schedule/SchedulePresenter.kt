package com.axemorgan.genconcatalogue.schedule

import com.axemorgan.genconcatalogue.events.EventDao
import javax.inject.Inject

class SchedulePresenter @Inject constructor(private val eventDao: EventDao) : ScheduleContract.Presenter() {

    override fun onViewAttached() {
        // todo do stuff
    }
}