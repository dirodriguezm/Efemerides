package com.example.diego.efemerides;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    //private ArrayList<Event> events;
    private ArrayList<EventMaster> events;
    private ArrayList<DayMonthEvent> dayMonthEvents;
    private ArrayList<DayNumberEvent> dayNumberEvents;
    private CalendarDay date;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventName;
        public TextView eventDescription;
        public ImageView icon;
        public View layout;
        public ViewHolder(View v) {
            super(v);
            layout = v;
            eventName = v.findViewById(R.id.firstLine);
            eventDescription = v.findViewById(R.id.secondLine);
            icon = v.findViewById(R.id.icon);
        }
    }

    public EventListAdapter(ArrayList<Event> events, ArrayList<DayMonthEvent> dayMonthEvents,ArrayList<DayNumberEvent> dayNumberEvents,  CalendarDay date) {
        this.events = new ArrayList();
        if(events != null) {
            for (Event event : events) {
                this.events.add(event);
            }
        }
        if(dayMonthEvents != null) {
            for (DayMonthEvent event : dayMonthEvents) {
                this.events.add(event);
            }
        }
        if(dayNumberEvents != null) {
            for (DayNumberEvent event : dayNumberEvents) {
                this.events.add(event);
            }
        }
        this.date = date;
    }



    public EventListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.dias_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.eventName.setText(events.get(position).getEventName());
        if(events.get(position).getIsBirthDay() == 1){
            Drawable cake = holder.layout.getContext().getResources().getDrawable(R.drawable.ic_cake_black_24dp);
            holder.icon.setImageDrawable(cake);
            holder.icon.setVisibility(View.VISIBLE);
            if(events.get(position).getEventYear() != 0){
                int thisYear = date.getYear();
                int years = thisYear - events.get(position).getEventYear();
                holder.eventDescription.setText("Cumple " + years+" años");
                holder.eventDescription.setVisibility(View.VISIBLE);
            }
        }else if(events.get(position).getEventYear() != 0){
            Drawable cake = holder.layout.getContext().getResources().getDrawable(R.drawable.ic_history_black_24dp);
            holder.icon.setImageDrawable(cake);
            holder.icon.setVisibility(View.VISIBLE);
            int thisYear = date.getYear();
            int years = thisYear - events.get(position).getEventYear();
            holder.eventDescription.setText("Aniversario N° " + years);
            holder.eventDescription.setVisibility(View.VISIBLE);
        }
    }


    public int getItemCount() {
        return events.size();
    }


}
