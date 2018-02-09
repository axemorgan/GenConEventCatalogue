package com.axemorgan.genconcatalogue.schedule

import com.axemorgan.genconcatalogue.components.AbstractPresenter
import com.axemorgan.genconcatalogue.events.Event

interface DayContract {

    interface View {
        fun showEvents(events: List<Event>)
    }

    abstract class Presenter() : AbstractPresenter<View>() {

    }
}