package com.example.diego.efemerides;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


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

    private ArrayList<Event> events;

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


            for (Event event : events) {
                Calendar eventDate = Calendar.getInstance();
                ArrayList<CalendarDay> calendarDays = new ArrayList<>();
                int eventYear = event.getEventYear();
                int eventMonth = event.getEventMonth();
                int eventDay = event.getEventDay();
                for (int i = calendarView.getMinimumDate().getYear(); i <= calendarView.getMaximumDate().getYear(); i++) {
                    eventDate.set(i, eventMonth - 1, eventDay);
                    Log.d("SETTING", i + "-" + eventMonth + "-" + eventDay);
                    calendarDays.add(CalendarDay.from(eventDate));

                }
                calendarView.addDecorator(new EventDecorator(Color.RED, calendarDays));
            }
        }
        return view;
    }

    private void onDateChange(CalendarDay date) {
        if (mListener != null){
            mListener.onFragmentInteraction(date);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(CalendarDay date);
    }
}
