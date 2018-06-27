package com.example.diego.efemerides;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;


import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CalendarFragment.OnFragmentInteractionListener, InformationFragment.OnFragmentInteractionListener{
    private CalendarFragment calendarFragment;
    private InformationFragment informationFragment;
    private String[] monthName = {"Enero", "Febrero",
            "Marzo", "Abril", "Mayo", "Junio", "Julio",
            "Agosto", "Septiembre", "Octubre", "Noviembre",
            "Diciembre"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //Locale spanish = new Locale("es", "ES");
        //Locale.setDefault(spanish);
        informationFragment = new InformationFragment();
        calendarFragment = new CalendarFragment();


        final AppDatabase db = AppDatabase.getAppDatabase(this);
        new Thread(new Runnable() {
            public void run() {
                List<Event> retrievedEvents = db.eventDao().getAll();
                if(retrievedEvents.size() == 0) {
                    ArrayList<Event> events = createEventsFromFile();
                    for (Event event : events) {
                        db.eventDao().insertEvent(event);
                    }
                }
                Bundle args = new Bundle();
                args.putParcelableArrayList(calendarFragment.ARG_EVENTS, (ArrayList<? extends Parcelable>) retrievedEvents);
                calendarFragment.setArguments(args);
            }
        }).start();





        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(findViewById(R.id.fragment_container1) != null && findViewById(R.id.fragment_container2) != null){
            fragmentTransaction.replace(R.id.fragment_container2,informationFragment ,"information");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.fragment_container1, calendarFragment, "calendar");
            fragmentTransaction.addToBackStack(null);
        }
        else{
            fragmentTransaction.replace(R.id.fragment_container, new CalendarFragment() , "calendar");
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    public ArrayList<Event> createEventsFromFile(){
        InputStream inputStream = getResources().openRawResource(R.raw.efemerides);
        ArrayList<Event> events = new ArrayList<>();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while( (line=bufferedReader.readLine())  != null){
                String [] splittedLine = line.split(" ");
                int day = Integer.parseInt(splittedLine[0]);
                int month = monthNameToInt(splittedLine[1]);
                String name = "";
                int year = 0;
                for(int i = 2; i < splittedLine.length; i++){
                    if(splittedLine[i].startsWith("(")){
                        String yearStr = splittedLine[i].substring(1,5);
                        try{
                            year = Integer.parseInt(yearStr);
                        }
                        catch (NumberFormatException e){
                            year = 0;
                        }
                        break;
                    }
                    else {
                        name += splittedLine[i] + " ";
                    }
                }
                events.add(new Event(name, year, month, day, 1));
            }
            return events;
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return events;
    }

    private int monthNameToInt(String s) throws ParseException, java.text.ParseException {
        Date date = new SimpleDateFormat("MMMM").parse(s);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (cal.get(Calendar.MONTH)) + 1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(CalendarDay date) {
        if(findViewById(R.id.fragment_container2) != null) {
            InformationFragment informationFragment = (InformationFragment) getSupportFragmentManager().findFragmentByTag("information");
            if(informationFragment != null){
                informationFragment.setText(date.getDay() + " de " + monthName[date.getMonth()] + " de " + date.getYear());
            }
        }

        else{
            InformationFragment newFragment = new InformationFragment();
            Bundle args = new Bundle();
            args.putString(InformationFragment.ARG_DATE, date.getDay() + " de " + monthName[date.getMonth()] + " de " + date.getYear());
            newFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


            transaction.replace(R.id.fragment_container, newFragment, "information");
            transaction.addToBackStack(null);


            transaction.commit();

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener, DatePicker.OnDateChangedListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            //Locale spanish = new Locale("es", "ES");
            //Locale.setDefault(spanish);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setTitle("asd");
            //dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            //c.set(0,0,1);
            //dialog.getDatePicker().setMinDate(c.getTimeInMillis());

            DatePicker datePicker = dialog.getDatePicker();
            datePicker.init(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                if (daySpinnerId != 0)
                {

                    View daySpinner = datePicker.findViewById(daySpinnerId);
                    if (daySpinner != null)
                    {
                        Log.v("Hola", "QUE WEA");
                        daySpinner.setVisibility(View.VISIBLE);
                    }
                }

                int monthSpinnerId = Resources.getSystem().getIdentifier("month", "id", "android");
                if (monthSpinnerId != 0)
                {
                    View monthSpinner = datePicker.findViewById(monthSpinnerId);
                    if (monthSpinner != null)
                    {
                        monthSpinner.setVisibility(View.VISIBLE);
                    }
                }

                int yearSpinnerId = Resources.getSystem().getIdentifier("year", "id", "android");
                if (yearSpinnerId != 0)
                {
                    View yearSpinner = datePicker.findViewById(yearSpinnerId);
                    if (yearSpinner != null)
                    {
                        yearSpinner.setVisibility(View.GONE);
                    }
                }
            }else { //Older SDK versions
                Field f[] = dialog.getClass().getDeclaredFields();
                for (Field field : f)
                {
                    if(field.getName().equals("mDayPicker") || field.getName().equals("mDaySpinner"))
                    {
                        field.setAccessible(true);
                        Object dayPicker = null;
                        try {
                            dayPicker = field.get(dialog);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        ((View) dayPicker).setVisibility(View.GONE);
                    }

                    if(field.getName().equals("mMonthPicker") || field.getName().equals("mMonthSpinner"))
                    {
                        field.setAccessible(true);
                        Object monthPicker = null;
                        try {
                            monthPicker = field.get(dialog);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        ((View) monthPicker).setVisibility(View.VISIBLE);
                    }

                    if(field.getName().equals("mYearPicker") || field.getName().equals("mYearSpinner"))
                    {
                        field.setAccessible(true);
                        Object yearPicker = null;
                        try {
                            yearPicker = field.get(dialog);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        ((View) yearPicker).setVisibility(View.GONE);
                    }
                }
            }
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

        }

        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int month_i = monthOfYear + 1;
            Log.e("selected month:", Integer.toString(month_i));
        }
    }
}
