package com.axemorgan.genconcatalogue

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.Delegates

@Singleton
class SearchModel @Inject constructor() {

    private var observers: MutableList<(SearchModel) -> Unit> = mutableListOf()

    var query: String by Delegates.observable("") { _, oldValue, newValue ->
        if (oldValue != newValue) {
            this.notifyObservers()
        }
    }

    var eventTypeFilter: String by Delegates.observable("") { _, oldValue, newValue ->
        if (oldValue != newValue) {
            this.notifyObservers()
        }
    }


    fun addObserver(observer: (SearchModel) -> Unit) {
        observers.add(observer)
    }

    fun removeObserver(observer: (SearchModel) -> Unit) {
        observers.remove(observer)
    }

    private fun notifyObservers() {
        observers.forEach { it(this) }
    }

}