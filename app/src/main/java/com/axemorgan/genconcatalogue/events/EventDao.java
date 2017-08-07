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

    @Query("SELECT * FROM event")
    Flowable<List<Event>> getAll();

    @Query("SELECT * FROM event WHERE id == :oneId")
    Single<Event> byId(String oneId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void put(Event event);
}
