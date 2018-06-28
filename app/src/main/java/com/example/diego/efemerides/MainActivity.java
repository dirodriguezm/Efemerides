package com.example.diego.efemerides;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
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
import android.support.v7.widget.PopupMenu;
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
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CalendarFragment.OnFragmentInteractionListener, InformationFragment.OnFragmentInteractionListener, PopupMenu.OnMenuItemClickListener {
    private CalendarFragment calendarFragment;
    private InformationFragment informationFragment;
    private String[] monthName = {"Enero", "Febrero",
            "Marzo", "Abril", "Mayo", "Junio", "Julio",
            "Agosto", "Septiembre", "Octubre", "Noviembre",
            "Diciembre"};
    private AppDatabase db;
    private List<Event> retrievedEvents;
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


        db = AppDatabase.getAppDatabase(this);
        new Thread(new Runnable() {
            public void run() {
                retrievedEvents = db.eventDao().getAll();
                if(retrievedEvents.size() == 0) {
                    ArrayList<Event> events = createEventsFromFile();
                    for (Event event : events) {
                        db.eventDao().insertEvent(event);
                    }
                }
                retrievedEvents = db.eventDao().getAll();
                ArrayList<? extends Parcelable> test = (ArrayList<? extends Parcelable>) retrievedEvents;
                Bundle args = new Bundle();
                args.putParcelableArrayList(calendarFragment.ARG_EVENTS, (ArrayList<? extends Parcelable>) retrievedEvents);
                calendarFragment.setArguments(args);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                if(findViewById(R.id.fragment_container1) != null && findViewById(R.id.fragment_container2) != null){
                    fragmentTransaction.replace(R.id.fragment_container2,informationFragment ,"information");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.fragment_container1, calendarFragment, "calendar");
                    fragmentTransaction.addToBackStack(null);
                }
                else{
                    fragmentTransaction.replace(R.id.fragment_container, calendarFragment , "calendar");
                    fragmentTransaction.addToBackStack(null);
                }

                fragmentTransaction.commit();
            }
        }).start();


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
                events.add(new Event(name, year, month, day));
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
    public void onFragmentInteraction(CalendarDay date, Collection<Event> events) {


        if(findViewById(R.id.fragment_container2) != null) {
            InformationFragment informationFragment = (InformationFragment) getSupportFragmentManager().findFragmentByTag("information");
            if(informationFragment != null){
                informationFragment.update(date.getDay() + " de " + monthName[date.getMonth() -1] + " de " + date.getYear(), events);
            }
        }

        else{
            InformationFragment newFragment = new InformationFragment();
            Bundle args = new Bundle();
            args.putParcelable(InformationFragment.ARG_DATE, date);
            ArrayList<Event> events1 = new ArrayList<>();
            for (Event event : events){
                events1.add(event);
            }
            args.putParcelableArrayList(InformationFragment.ARG_EVENT, events1);
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



    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.actions);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent nuevoDia = new Intent(this, NuevoDiaActivity.class);
        switch (item.getItemId()) {
            case R.id.dia:
                nuevoDia.putExtra("item",NuevoDiaActivity.DIA);
                startActivity(nuevoDia);
                return true;
            case R.id.dia_historico:
                nuevoDia.putExtra("item",NuevoDiaActivity.DIA_HIST);
                startActivity(nuevoDia);
                return true;
            case R.id.dia_movil:
                nuevoDia.putExtra("item",NuevoDiaActivity.DIA_MOVIL);
                startActivity(nuevoDia);
                return true;
            case R.id.cumpleaños:
                nuevoDia.putExtra("item",NuevoDiaActivity.CUMPLE);
                startActivity(nuevoDia);
                return true;
            default:
                return false;
        }

    }



}
