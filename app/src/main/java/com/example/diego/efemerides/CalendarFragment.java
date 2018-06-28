package com.example.diego.efemerides;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    private MaterialCalendarView calendarView;

    private OnFragmentInteractionListener mListener;

    public static String ARG_EVENTS = "events";
    public static String ARG_DAY_NUMBER_EVENTS = "dayNumberEvents";
    public static String ARG_DAY_MONTH_EVENTS = "dayMonthEvents";

    private ArrayList<Event> events;
    private ArrayList<DayMonthEvent> dayMonthEvents;
    private ArrayList<DayNumberEvent> dayNumberEvents;

    private ListMultimap< CalendarDay, Event> calendarDayEventMultimap;
    private ListMultimap< CalendarDay, DayMonthEvent> calendarDayDayMonthEventListMultimap;
    private ListMultimap< CalendarDay, DayNumberEvent> calendarDayDayNumberEventListMultimap;


    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            events = getArguments().getParcelableArrayList(ARG_EVENTS);
            dayMonthEvents = getArguments().getParcelableArrayList(ARG_DAY_MONTH_EVENTS);
            dayNumberEvents = getArguments().getParcelableArrayList(ARG_DAY_NUMBER_EVENTS);
            Log.d("DAY EVENTS",dayMonthEvents.size()+"");
        }
        else{
            Log.d("EVENTS", "NULL");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        calendarView.state().edit()
                .setMaximumDate(CalendarDay.from(2025,12,31 ))
                .setMinimumDate(CalendarDay.from(2000,1,1 ))
                .commit();
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                onDateChange(date);
            }
        });
        if(events != null) {
            calendarDayEventMultimap = MultimapBuilder.hashKeys().arrayListValues().build();
            for (Event event : events) {
                Calendar eventDate = Calendar.getInstance();
                ArrayList<CalendarDay> calendarDays = new ArrayList<>();
                int eventYear = event.getEventYear();
                int eventMonth = event.getEventMonth();
                int eventDay = event.getEventDay();
                for (int i = calendarView.getMinimumDate().getYear(); i <= calendarView.getMaximumDate().getYear(); i++) {
                    eventDate.set(i, eventMonth - 1, eventDay);
                    calendarDays.add(CalendarDay.from(eventDate));
                    calendarDayEventMultimap.put(CalendarDay.from(eventDate),event);
                }
                calendarView.addDecorator(new EventDecorator(Color.RED, calendarDays));
            }
        }
        if(dayNumberEvents != null) {
            calendarDayDayNumberEventListMultimap = MultimapBuilder.hashKeys().arrayListValues().build();
            for (DayNumberEvent event : dayNumberEvents) {
                Calendar eventDate = Calendar.getInstance();
                ArrayList<CalendarDay> calendarDays = new ArrayList<>();
                for (int i = calendarView.getMinimumDate().getYear(); i <= calendarView.getMaximumDate().getYear(); i++) {
                    eventDate.set(Calendar.YEAR, i);
                    eventDate.set(Calendar.DAY_OF_YEAR,event.getEventNumber());
                    calendarDays.add(CalendarDay.from(eventDate));
                    calendarDayDayNumberEventListMultimap.put(CalendarDay.from(eventDate),event);
                }
                calendarView.addDecorator(new EventDecorator(Color.RED, calendarDays));
            }
        }
        if(dayMonthEvents != null) {
            calendarDayDayMonthEventListMultimap = MultimapBuilder.hashKeys().arrayListValues().build();
            for (DayMonthEvent event : dayMonthEvents) {
                Calendar eventDate = Calendar.getInstance();
                ArrayList<CalendarDay> calendarDays = new ArrayList<>();

                for (int i = calendarView.getMinimumDate().getYear(); i <= calendarView.getMaximumDate().getYear(); i++) {
                    eventDate.set(Calendar.DAY_OF_WEEK, event.getEventDay());
                    eventDate.set(Calendar.DAY_OF_WEEK_IN_MONTH,event.getEventNumber());
                    eventDate.set(Calendar.MONTH, event.getEventMonth());
                    eventDate.set(Calendar.YEAR, i);
                    calendarDays.add(CalendarDay.from(eventDate));
                    calendarDayDayMonthEventListMultimap.put(CalendarDay.from(eventDate),event);
                    Log.d("PUT EVENT NAME",event.getEventName());
                    Log.d("PUT EVENT DATE", CalendarDay.from(eventDate).toString());
                }
                calendarView.addDecorator(new EventDecorator(Color.RED, calendarDays));
            }
        }
        return view;
    }


    private void onDateChange(CalendarDay date) {
        if (mListener != null){
            mListener.onFragmentInteraction( date, calendarDayEventMultimap.get(date), calendarDayDayMonthEventListMultimap.get(date), calendarDayDayNumberEventListMultimap.get(date));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(CalendarDay date,Collection<Event> events, Collection<DayMonthEvent> dayMonthEvents, Collection<DayNumberEvent> dayNumberEvents);
    }
}
