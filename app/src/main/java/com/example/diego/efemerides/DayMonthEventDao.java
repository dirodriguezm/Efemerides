package com.example.diego.efemerides;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DayMonthEventDao {
    @Query("SELECT * FROM DayMonthEvent")
    List<DayMonthEvent> getAll();

    @Insert
    void insertDayMonthEvent(DayMonthEvent event);
}
