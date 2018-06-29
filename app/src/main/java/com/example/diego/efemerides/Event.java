package com.example.diego.efemerides;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;


import javax.annotation.Nullable;

@Entity
public class Event extends EventMaster implements Parcelable{
    @PrimaryKey(autoGenerate = true)
    private int eventId;
    @ColumnInfo(name="event_name")
    private String eventName;
    @ColumnInfo(name="event_year")
    @Nullable
    private int eventYear;
    @ColumnInfo(name = "event_month")
    private int eventMonth;
    @ColumnInfo(name = "event_day")
    private int eventDay;
    @Nullable
    @ColumnInfo(name="event_birthday")
    private int eventBirthday;

    public Event(String eventName, int eventYear, int eventMonth, int eventDay, int eventBirthday){
        super(eventName);
        this.eventName = eventName;
        this.eventMonth = eventMonth;
        this.eventDay = eventDay;
        this.eventYear = eventYear;
        this.eventBirthday = eventBirthday;
        super.setIsBirthDay(eventBirthday);
    }

    protected Event(Parcel in) {
        super(in.readString());
        eventId = in.readInt();
        eventName = in.readString();
        eventYear = in.readInt();
        eventMonth = in.readInt();
        eventDay = in.readInt();
        eventBirthday = in.readInt();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public int getEventId(){
        return this.eventId;
    }

    public String getEventName(){
        return this.eventName;
    }

    public int getEventYear(){
        return this.eventYear;
    }

    public int getEventMonth(){
        return this.eventMonth;
    }

    public int getEventDay(){
        return this.eventDay;
    }


    public void setEventId(int eventId){
        this.eventId = eventId;
    }

    public void setEventName(String eventName){
        this.eventName = eventName;
    }

    public void setEventYear(int eventYear){
        this.eventYear = eventYear;
    }

    public void setEventMonth(int eventMonth){
        this.eventMonth = eventMonth;
    }

    public void setEventDay(int eventDay){
        this.eventDay = eventDay;
    }

    public boolean isEvent(){
        return true;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(eventId);
        dest.writeString(eventName);
        dest.writeInt(eventYear);
        dest.writeInt(eventMonth);
        dest.writeInt(eventDay);
        dest.writeInt(eventBirthday);
    }

    @Nullable
    public int getEventBirthday() {
        return eventBirthday;
    }

    public void setEventBirthday(@Nullable int eventBirthday) {
        this.eventBirthday = eventBirthday;
    }
}
