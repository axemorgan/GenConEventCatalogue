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

        // TODO this is going to get out of hand very quickly
        return if (searchModel.eventTypeFilter.isEmpty()) {
            if (!searchModel.ageRequirementFilter.isEmpty()) {
                eventDao.searchWithAgeRequirement("%" + searchModel.query + "%", searchModel.ageRequirementFilter)
            } else {
                eventDao.search("%" + searchModel.query + "%")
            }
        } else {
            if (!searchModel.ageRequirementFilter.isEmpty()) {
                eventDao.searchWithTypeAndAgeRequirement("%" + searchModel.query + "%", searchModel.eventTypeFilter, searchModel.ageRequirementFilter)
            } else {
                eventDao.searchWithEventType("%" + searchModel.query + "%", searchModel.eventTypeFilter)
            }
        }
    }
}