package com.example.diego.efemerides;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DayNumberEventDao {

    @Query("SELECT * FROM DayNumberEvent")
    List<DayNumberEvent> getAll();

    @Insert
    void insertDayNumberEvent(DayNumberEvent event);
}
