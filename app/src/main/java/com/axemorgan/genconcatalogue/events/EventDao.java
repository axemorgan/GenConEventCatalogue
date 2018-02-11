package com.axemorgan.genconcatalogue.events;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface EventDao {

    @Query("SELECT * FROM Event")
    Flowable<List<Event>> getAll();

    @Query("SELECT * FROM Event WHERE title LIKE :query")
    Flowable<List<Event>> search(String query);

    @Query("SELECT * FROM Event WHERE title like :query AND event_type = :eventType")
    Flowable<List<Event>> searchWithEventType(String query, String eventType);

    @Query("SELECT * FROM Event WHERE title LIKE :query AND age_required = :ageRequirement")
    Flowable<List<Event>> searchWithAgeRequirement(String query, String ageRequirement);

    @Query("SELECT * FROM Event WHERE title LIKE :query AND event_type = :eventType AND age_required = :ageRequirement")
    Flowable<List<Event>> searchWithTypeAndAgeRequirement(String query, String eventType, String ageRequirement);

    @Query("SELECT * FROM Event WHERE id == :oneId")
    Single<Event> byId(String oneId);

    @Query("SELECT DISTINCT event_type FROM Event ORDER BY event_type ASC")
    Flowable<List<String>> getAllEventTypes();

    @Query("SELECT DISTINCT age_required FROM Event ORDER BY age_required ASC")
    Flowable<List<String>> getAllAgeRequirements();

    @Query("SELECT * FROM Event WHERE saved = 1")
    Flowable<List<Event>> getAllSavedEvents();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void put(Event event);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void putAll(List<Event> events);

    @Update
    void update(Event event);
}
