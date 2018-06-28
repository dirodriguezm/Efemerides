package com.example.diego.efemerides;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class DayMonthEvent {
    @PrimaryKey(autoGenerate = true)
    private int eventId;
    @ColumnInfo(name="event_name")
    private String eventName;
    @ColumnInfo(name = "event_number")
    private int eventNumber;
    @ColumnInfo(name="event_day")
    private int eventDay;
    @ColumnInfo(name="event_month")
    private int eventMonth;
}
