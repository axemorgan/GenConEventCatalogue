package com.axemorgan.genconcatalogue.events

import com.axemorgan.genconcatalogue.SearchModel
import dagger.Reusable
import io.reactivex.Flowable
import timber.log.Timber
import javax.inject.Inject

@Reusable
class Search @Inject constructor(val eventDao: EventDao) {

    fun using(searchModel: SearchModel): Flowable<List<Event>> {
        Timber.i("Searching for \"%s\" with event filter \"%s\"", searchModel.query, searchModel.eventTypeFilter)

        return if (searchModel.eventTypeFilter.isEmpty()) {
            eventDao.search("%" + searchModel.query + "%")
        } else {
            eventDao.searchWithEventType("%" + searchModel.query + "%", searchModel.eventTypeFilter)
        }
    }
}