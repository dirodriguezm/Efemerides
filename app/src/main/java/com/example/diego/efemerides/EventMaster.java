package com.example.diego.efemerides;

public class EventMaster {
    private String eventName;

    public EventMaster (String eventName){
        this.eventName = eventName;
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
}
