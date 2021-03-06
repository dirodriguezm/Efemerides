package com.example.diego.efemerides;

import android.net.Uri;
import android.os.Bundle;
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

import com.prolificinteractive.materialcalendarview.CalendarDay;

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
