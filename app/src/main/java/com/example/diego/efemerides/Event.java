package com.example.diego.efemerides;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;

import javax.annotation.Nullable;

@Entity
public class Event {
    @PrimaryKey(autoGenerate = true)
    private int eventId;
    @ColumnInfo(name="event_name")
    private String eventName;
    @ColumnInfo(name = "event_month")
    private int eventMonth;
    @ColumnInfo(name = "event_day")
    private int eventDay;
    @ColumnInfo(name = "peridiocity")
    private int peridiocity; // 1 = every day, 2 = every month, 3 = every year

    public Event(String eventName, int eventMonth, int eventDay, int peridiocity){
        this.eventName = eventName;
        this.eventMonth = eventMonth;
        this.eventDay = eventDay;
        this.peridiocity = peridiocity;
    }

    public int getEventId(){
        return this.eventId;
    }

    public String getEventName(){
        return this.eventName;
    }

    public int getEventMonth(){
        return this.eventMonth;
    }

    public int getEventDay(){
        return this.eventDay;
    }

    public int getPeridiocity(){
        return this.peridiocity;
    }

    public void setEventId(int eventId){
        this.eventId = eventId;
    }

    public void setEventName(String eventName){
        this.eventName = eventName;
    }

    public void setEventMonth(int eventMonth){
        this.eventMonth = eventMonth;
    }

    public void setEventDay(int eventDay){
        this.eventDay = eventDay;
    }

    public void setPeridiocity(int peridiocity){
        this.peridiocity = peridiocity;
    }



}
