package com.example.diego.efemerides;

public class EventMaster {
    private String eventName;
    private int isBirthDay;

    public EventMaster (String eventName){
        this.eventName = eventName;
        this.isBirthDay = 0;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getEventYear(){
        return 0;
    }

    public int getIsBirthDay() {
        return isBirthDay;
    }

    public void setIsBirthDay(int isBirthDay) {
        this.isBirthDay = isBirthDay;
    }
}
