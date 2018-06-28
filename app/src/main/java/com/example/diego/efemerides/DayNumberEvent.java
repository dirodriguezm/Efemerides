package com.example.diego.efemerides;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class DayNumberEvent extends EventMaster implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int eventId;
    @ColumnInfo(name="event_name")
    private String eventName;
    @ColumnInfo(name = "event_number")
    private int eventNumber;

    public DayNumberEvent(String eventName, int eventNumber) {
        super(eventName);
        this.eventName = eventName;
        this.eventNumber = eventNumber;
    }

    protected DayNumberEvent(Parcel in) {
        super(in.readString());
        eventId = in.readInt();
        eventName = in.readString();
        eventNumber = in.readInt();
    }

    public static final Creator<DayNumberEvent> CREATOR = new Creator<DayNumberEvent>() {
        @Override
        public DayNumberEvent createFromParcel(Parcel in) {
            return new DayNumberEvent(in);
        }

        @Override
        public DayNumberEvent[] newArray(int size) {
            return new DayNumberEvent[size];
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(eventId);
        dest.writeString(eventName);
        dest.writeInt(eventNumber);
    }
}
