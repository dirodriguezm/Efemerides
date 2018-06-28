package com.example.diego.efemerides;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.lang.reflect.Field;
import java.util.Calendar;

public class NuevoDiaActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int DIA = 0;
    public static final int DIA_HIST = 1;
    public static final int DIA_MOVIL = 2;
    public static final int CUMPLE = 3;
    public static String[] meses = { "Enero", "Fenrero", "Marzo","Abril","Mayo", "Junio", "Julio",
            "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" };
    public static String[] dias = { "Lunes", "Martes", "Miercoles","Jueves",
            "Viernes", "Sabado", "Domingo" };
    public static String[] posciones = { "1er", "2do", "3er","4to" };

    private int item;

    private boolean setDate;
    private boolean setName;

    private Button fecha, año;
    private EditText nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_dia);
        Bundle extras = getIntent().getExtras();
        fecha = findViewById(R.id.fecha);
        año = findViewById(R.id.año);
        nombre = findViewById(R.id.nombre);
        item = extras.getInt("item");
        setDate = false;
        switch (item){
            case DIA:
                this.setTitle("Nuevo Día");
                break;
            case DIA_HIST:
                this.setTitle("Nuevo Día Historico");
                break;
            case DIA_MOVIL:
                this.setTitle("Nuevo Día Movil");
                break;
            case CUMPLE:
                this.setTitle("Nuevo Cumpleaños");
                nombre.setHint("Nombre festejado");
                año.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment;
        switch (item){
            case NuevoDiaActivity.DIA_HIST:
                newFragment = new DatePickerFragmentHist();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case NuevoDiaActivity.CUMPLE:
            case NuevoDiaActivity.DIA:
                newFragment = new DatePickerFragmentDay();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case NuevoDiaActivity.DIA_MOVIL:
                newFragment = new DatePickerMovilDialogFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
        }

    }

    public void showYearPickerDialog(View v){
        DialogFragment newFragment = new DatePickerFragmentYear();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void setHistoricDate(int year, int month, int day){
        fecha.setText(day+"/"+month+"/"+year);
        setDate = true;
    }

    public void setDate(int month, int day){
        fecha.setText(day+"/"+month);
        setDate = true;
    }

    public void setYear(int year){
        año.setText(Integer.toString(year));
    }

    public void setMovil(int pos, int day, int month){
        fecha.setText(posciones[pos]+" "+dias[day]+" "+meses[day]);
        setDate = true;
    }

    public void setMovilDay(int day){
        fecha.setText("Dia "+day);
        setDate = true;
    }


    public static class DatePickerFragmentHist extends DialogFragment
            implements DatePickerDialog.OnDateSetListener, DatePicker.OnDateChangedListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);

            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            c.set(1,1,1);
            dialog.getDatePicker().setMinDate(c.getTimeInMillis());

            DatePicker datePicker = dialog.getDatePicker();
            datePicker.init(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),this);
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            NuevoDiaActivity activity = (NuevoDiaActivity)getActivity();
            activity.setHistoricDate(year,month+1,day);
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        }
    }

    public static class DatePickerFragmentDay extends DialogFragment
            implements DatePickerDialog.OnDateSetListener, DatePicker.OnDateChangedListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog,this, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setTitle("Fecha");
            DatePicker datePicker = dialog.getDatePicker();
            datePicker.init(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                if (daySpinnerId != 0)
                {

                    View daySpinner = datePicker.findViewById(daySpinnerId);
                    if (daySpinner != null)
                    {
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
                        ((View) dayPicker).setVisibility(View.VISIBLE);
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
            NuevoDiaActivity activity = (NuevoDiaActivity)getActivity();
            activity.setDate(month+1,day);
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        }
    }

    public static class DatePickerFragmentYear extends DialogFragment
            implements DatePickerDialog.OnDateSetListener, DatePicker.OnDateChangedListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog,this, year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setTitle("Año");
            DatePicker datePicker = dialog.getDatePicker();
            datePicker.init(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                if (daySpinnerId != 0)
                {

                    View daySpinner = datePicker.findViewById(daySpinnerId);
                    if (daySpinner != null)
                    {
                        daySpinner.setVisibility(View.GONE);
                    }
                }

                int monthSpinnerId = Resources.getSystem().getIdentifier("month", "id", "android");
                if (monthSpinnerId != 0)
                {
                    View monthSpinner = datePicker.findViewById(monthSpinnerId);
                    if (monthSpinner != null)
                    {
                        monthSpinner.setVisibility(View.GONE);
                    }
                }

                int yearSpinnerId = Resources.getSystem().getIdentifier("year", "id", "android");
                if (yearSpinnerId != 0)
                {
                    View yearSpinner = datePicker.findViewById(yearSpinnerId);
                    if (yearSpinner != null)
                    {
                        yearSpinner.setVisibility(View.VISIBLE);
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
                        ((View) monthPicker).setVisibility(View.GONE);
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
                        ((View) yearPicker).setVisibility(View.VISIBLE);
                    }
                }
            }
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            NuevoDiaActivity activity = (NuevoDiaActivity)getActivity();
            activity.setYear(year);
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        }
    }

    public static class DatePickerMovilDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

        private NumberPicker picker,pickerDay,pickerMonth,pickerAbsoluteDay;
        private AlertDialog.Builder builder;
        private LinearLayout LL;
        private Boolean modo;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            modo = true;
            LL = new LinearLayout(getContext());
            LL.setOrientation(LinearLayout.HORIZONTAL);
            // Use the Builder class for convenient dialog construction
            builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Numero, Dia y Mes");

            pickerAbsoluteDay = new NumberPicker(getActivity());
            pickerAbsoluteDay.setMinValue(1);
            pickerAbsoluteDay.setMaxValue(365);

            picker = new NumberPicker(getActivity());
            picker.setMinValue(0);
            picker.setMaxValue(3);
            picker.setDisplayedValues( posciones );

            pickerDay = new NumberPicker(getActivity());
            pickerDay.setMinValue(0);
            pickerDay.setMaxValue(6);
            pickerDay.setDisplayedValues( dias );

            pickerMonth = new NumberPicker(getActivity());
            pickerMonth.setMinValue(0);
            pickerMonth.setMaxValue(11);
            pickerMonth.setDisplayedValues( meses );

            builder.setPositiveButton("Aceptar",this);
            builder.setNegativeButton("Cancelar",this);
            builder.setNeutralButton("Cambiar Modo", this);


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);
            params.gravity = Gravity.CENTER;

            LinearLayout.LayoutParams picerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            picerParams.weight = 1;

            LinearLayout.LayoutParams dayPicerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dayPicerParams.weight = 1;

            LinearLayout.LayoutParams monthPicerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            monthPicerParams.weight = 1;

            LL.setLayoutParams(params);
            LL.addView(picker,picerParams);
            LL.addView(pickerDay,dayPicerParams);
            LL.addView(pickerMonth,monthPicerParams);

            builder.setView(LL);

            // Create the AlertDialog object and return it
            return builder.create();
        }

        private void switchMode(){
            modo = !modo;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
                case DialogInterface.BUTTON_POSITIVE:
                    NuevoDiaActivity activity = (NuevoDiaActivity)getActivity();
                    if(modo){
                        activity.setMovil(picker.getValue(),pickerDay.getValue(),pickerMonth.getValue());
                    }else{
                        activity.setMovilDay(pickerAbsoluteDay.getValue());
                    }

                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    switchMode();
                    if(modo){
                        builder.setView(LL);
                        builder.create();
                        builder.show();
                    }else{
                        builder.setView(pickerAbsoluteDay);
                        builder.create();
                        builder.show();
                    }


            }
        }
    }
}
