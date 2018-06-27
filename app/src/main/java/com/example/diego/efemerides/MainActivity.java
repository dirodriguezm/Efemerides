package com.example.diego.efemerides;

import android.arch.persistence.room.Room;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.common.io.Resources;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CalendarFragment.OnFragmentInteractionListener, InformationFragment.OnFragmentInteractionListener{
    private CalendarFragment calendarFragment;
    private InformationFragment informationFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
                retrievedEvents = db.eventDao().getAll();
                ArrayList<? extends Parcelable> test = (ArrayList<? extends Parcelable>) retrievedEvents;
                Log.d("EVENTS BEFORE", test.size()+"");
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
                events.add(new Event(name, year, month, day, 1));
            }
            return events;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return events;
    }

    private int monthNameToInt(String s) throws ParseException {
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
                informationFragment.setText("Fecha: " + date.getDay() + "-" + (date.getMonth()+ 1) + "-" + date.getYear());
            }
        }

        else{
            InformationFragment newFragment = new InformationFragment();
            Bundle args = new Bundle();
            args.putString(InformationFragment.ARG_DATE, "Fecha: " + date.getDay() + "-" + (date.getMonth() + 1) + "-" + date.getYear());
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
}
