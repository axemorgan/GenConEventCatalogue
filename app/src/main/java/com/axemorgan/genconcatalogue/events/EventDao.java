package com.axemorgan.genconcatalogue.events;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

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

    @Query("SELECT * FROM Event WHERE id == :oneId")
    Single<Event> byId(String oneId);

    @Query("SELECT DISTINCT event_type FROM Event")
    Flowable<List<String>> getAllEventTypes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void put(Event event);
}
