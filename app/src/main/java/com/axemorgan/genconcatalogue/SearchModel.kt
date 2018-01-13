package com.axemorgan.genconcatalogue

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.Delegates

@Singleton
class SearchModel @Inject constructor() {

    private var observers: MutableList<(String) -> Unit> = mutableListOf()

    fun addObserver(observer: (String) -> Unit) {
        observers.add(observer)
    }

    fun removeObserver(observer: (String) -> Unit) {
        observers.remove(observer)
    }

    var query: String by Delegates.observable("") { _, oldValue, newValue ->
        observers?.forEach { it(newValue) }
    }

}