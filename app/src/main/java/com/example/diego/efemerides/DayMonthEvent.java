package com.example.diego.efemerides;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class DayMonthEvent extends EventMaster implements Parcelable{
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

    public DayMonthEvent(String eventName, int eventNumber, int eventDay, int eventMonth){
        super(eventName);
        this.eventName = eventName;
        this.eventNumber = eventNumber;
        this.eventDay = eventDay;
        this.eventMonth = eventMonth;
    }


    protected DayMonthEvent(Parcel in) {
        super(in.readString());
        eventId = in.readInt();
        eventName = in.readString();
        eventNumber = in.readInt();
        eventDay = in.readInt();
        eventMonth = in.readInt();
    }

    public static final Creator<DayMonthEvent> CREATOR = new Creator<DayMonthEvent>() {
        @Override
        public DayMonthEvent createFromParcel(Parcel in) {
            return new DayMonthEvent(in);
        }

        @Override
        public DayMonthEvent[] newArray(int size) {
            return new DayMonthEvent[size];
        }
    };

    public int getEventId(){
        return this.eventId;
    }

    public void setEventId(int eventId){
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getEventNumber() {
        return eventNumber;
    }

    public void setEventNumber(int eventNumber) {
        this.eventNumber = eventNumber;
    }

    public int getEventDay() {
        return eventDay;
    }

    public void setEventDay(int eventDay) {
        this.eventDay = eventDay;
    }

    public int getEventMonth() {
        return eventMonth;
    }

    public void setEventMonth(int eventMonth) {
        this.eventMonth = eventMonth;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(eventId);
        dest.writeString(eventName);
        dest.writeInt(eventNumber);
        dest.writeInt(eventDay);
        dest.writeInt(eventMonth);
    }
}
