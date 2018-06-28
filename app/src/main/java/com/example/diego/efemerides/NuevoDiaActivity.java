package com.example.diego.efemerides;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.lang.reflect.Field;
import java.util.Calendar;

public class NuevoDiaActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    public static final int DIA = 0;
    public static final int DIA_HIST = 1;
    public static final int DIA_MOVIL = 2;
    public static final int CUMPLE = 3;
    public static final int dimension = 2;
    public static String[] meses = { "Enero", "Febrero", "Marzo","Abril","Mayo", "Junio", "Julio",
            "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" };
    public static String[] dias = { "Lunes", "Martes", "Miercoles","Jueves",
            "Viernes", "Sabado", "Domingo" };
    public static String[] posciones = { "1er", "2do", "3er","4to" };
    public static int[] diasMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public static int[] diasMesBisiesto = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public static final String TIPO = "tipo";
    //public static final String DATE = "date";
    public static final String DAY = "day";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String POS = "pos";
    public static final String SET_YEAR = "setYear";
    public static final String SET_MOVIL = "setMovil";

    private int item;

    private boolean setDate;
    private boolean setName;
    private boolean setYear;
    private boolean setMovil;

    private Button fecha, año;
    private EditText nombre;

    private FloatingActionButton botonAceptar;
    private FloatingActionButton botonCancelar;

    private CalendarDay date;
    private String nombreEvento;
    private int year;

    private int pos,day,month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_dia);
        Bundle extras = getIntent().getExtras();
        fecha = findViewById(R.id.fecha);
        año = findViewById(R.id.año);

        nombre = findViewById(R.id.nombre);
        nombre.clearFocus();
        nombre.setOnEditorActionListener(this);

        item = extras.getInt("item");
        setDate = false;
        setName = false;
        setYear = false;
        setMovil = false;

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
        botonAceptar = findViewById(R.id.done);
        botonCancelar = findViewById(R.id.cancel);
    }

    public void aceptar(View v){

        if(setDate && setName){
            Intent main = new Intent(this, MainActivity.class);
            switch (item){
                case DIA:
                    main.putExtra(TIPO,DIA);
                    main.putExtra(DAY,day);
                    main.putExtra(MONTH,month);
                    break;
                case DIA_HIST:
                    main.putExtra(TIPO,DIA_HIST);
                    main.putExtra(DAY,day);
                    main.putExtra(MONTH,month);
                    main.putExtra(YEAR,month);
                    break;
                case DIA_MOVIL:
                    main.putExtra(TIPO,DIA_MOVIL);
                    main.putExtra(DAY,day);
                    main.putExtra(SET_MOVIL,setMovil);
                    if(setMovil){
                        main.putExtra(POS,pos);
                        main.putExtra(MONTH,month);
                    }
                    break;
                case CUMPLE:
                    main.putExtra(TIPO,CUMPLE);
                    main.putExtra(SET_YEAR,setYear);
                    main.putExtra(DAY,day);
                    main.putExtra(MONTH,month);
                    if(setYear){
                        main.putExtra(YEAR,year);
                    }
                    break;
            }
            startActivity(main);
        }else{
            Context context = getApplicationContext();
            CharSequence text = "Falta ";
            if(!setDate && !setName){
                text = text + "nombre y fecha";
            }else if(!setDate){
                text = text + "fecha";
            }else{
                text = text + "nombre";
            }
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }


    }

    public void cancelar(View v){
        DialogFragment newFragment;
        switch (item){
            case DIA:
                newFragment = new CerrarDialogFragment();
                Bundle args = new Bundle();
                args.putString("message","Descartar nuevo Dia?");
                newFragment.setArguments(args);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case DIA_HIST:
                newFragment = new CerrarDialogFragment();
                Bundle argsHist = new Bundle();
                argsHist.putString("message","Descartar nuevo Dia Historico?");
                newFragment.setArguments(argsHist);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case DIA_MOVIL:
                newFragment = new CerrarDialogFragment();
                Bundle argsMovil = new Bundle();
                argsMovil.putString("message","Descartar nuevo Dia Movil?");
                newFragment.setArguments(argsMovil);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case CUMPLE:
                newFragment = new CerrarDialogFragment();
                Bundle argsCumple = new Bundle();
                argsCumple.putString("message","Descartar nuevo Cumpleaños?");
                newFragment.setArguments(argsCumple);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
        }
    }

    public void showDatePickerDialog(View v) {
        nombre.clearFocus();
        DialogFragment newFragment;
        switch (item){
            case NuevoDiaActivity.DIA_HIST:
                newFragment = new DatePickerMonthDialogFragment();
                Bundle args = new Bundle();
                args.putBoolean("year",true);
                newFragment.setArguments(args);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case NuevoDiaActivity.CUMPLE:
            case NuevoDiaActivity.DIA:
                newFragment = new DatePickerMonthDialogFragment();
                Bundle arg = new Bundle();
                arg.putBoolean("year",false);
                newFragment.setArguments(arg);
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case NuevoDiaActivity.DIA_MOVIL:
                newFragment = new DatePickerMovilDialogFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                break;
        }

    }

    public void showYearPickerDialog(View v){
        nombre.clearFocus();
        DialogFragment newFragment = new DatePickerYearDialogFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void setHistoricDate(int year, int month, int day){
        fecha.setText(day+"/"+month+"/"+year);

        this.day = day;
        this.month = month;
        this.year = year;

        setDate = true;
    }

    public void setDate(int month, int day){
        fecha.setText(day+"/"+month);

        this.day = day;
        this.month = month;

        setDate = true;
    }

    public void setYear(int year){
        año.setText(Integer.toString(year));
        this.year = year;

        setYear = true;
    }

    public void setMovil(int pos, int day, int month){
        fecha.setText(posciones[pos]+" "+dias[day]+" "+meses[month]);

        this.pos = pos;
        this.day = day;
        this.month = month;

        setMovil = true;
        setDate = true;
    }

    public void setMovilDay(int day){
        fecha.setText("Dia "+day);

        this.day = day;

        setMovil = false;
        setDate = true;
    }

    public void openMain() {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId== EditorInfo.IME_ACTION_DONE){
            //Clear focus here from edittext
            String text = nombre.getText().toString();
            nombreEvento = text;
            setName = true;

            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            nombre.clearFocus();



            return true;
        }
        return false;
    }

    public static class CerrarDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{

        private NuevoDiaActivity activity;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            String message = getArguments().getString("message");
            activity = (NuevoDiaActivity)getActivity();

            AlertDialog.Builder builder = new AlertDialog.Builder((NuevoDiaActivity)getActivity());

            builder.setMessage(message);
            builder.setPositiveButton("Aceptar",this);
            builder.setNegativeButton("Cancelar",this);

            return builder.create();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
                case DialogInterface.BUTTON_POSITIVE:
                    activity.openMain();
                    break;
            }
        }
    }

    public static class DatePickerYearDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{

        private AlertDialog alertDialog;
        private NuevoDiaActivity activity;
        private NumberPicker pickerYear;

        public AlertDialog createDayMonthView(){
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Año");

            pickerYear = new NumberPicker(activity);
            pickerYear.setMinValue(1900);
            pickerYear.setMaxValue(year);
            pickerYear.setValue(year);

            builder.setPositiveButton("Aceptar",this);
            builder.setNegativeButton("Cancelar",this);

            builder.setView(pickerYear);

            alertDialog = builder.create();
            return alertDialog;
        }

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            activity = (NuevoDiaActivity)getActivity();
            alertDialog = createDayMonthView();
            return alertDialog;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
                case DialogInterface.BUTTON_POSITIVE:
                    activity.setYear(pickerYear.getValue());
                    break;
            }
        }
    }

    public static class DatePickerMovilDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

        private Boolean modo;
        private AlertDialog alertDialog;
        private NuevoDiaActivity activity;
        private Context context;
        private NumberPicker picker, pickerDay, pickerMonth, pickerAbsoluteDay;

        public AlertDialog createDayView(){
            LinearLayout LL = new LinearLayout(context);
            LL.setOrientation(LinearLayout.HORIZONTAL);

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Numero, Dia y Mes");

            picker = new NumberPicker(activity);
            picker.setMinValue(0);
            picker.setMaxValue(3);
            picker.setDisplayedValues( posciones );

            pickerDay = new NumberPicker(activity);
            pickerDay.setMinValue(0);
            pickerDay.setMaxValue(6);
            pickerDay.setDisplayedValues( dias );

            pickerMonth = new NumberPicker(activity);
            pickerMonth.setMinValue(0);
            pickerMonth.setMaxValue(11);
            pickerMonth.setDisplayedValues( meses );

            builder.setPositiveButton("Aceptar",this);
            builder.setNegativeButton("Cancelar",this);
            builder.setNeutralButton("Cambiar Modo", this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dimension, dimension);
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

            alertDialog = builder.create();
            return alertDialog;

        }

        public AlertDialog createAbsoluteDayView(){

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Dia del año");

            pickerAbsoluteDay = new NumberPicker(activity);
            pickerAbsoluteDay.setMinValue(1);
            pickerAbsoluteDay.setMaxValue(365);

            builder.setPositiveButton("Aceptar",this);
            builder.setNegativeButton("Cancelar",this);
            builder.setNeutralButton("Cambiar Modo", this);

            builder.setView(pickerAbsoluteDay);

            alertDialog = builder.create();
            return alertDialog;

        }


        public Dialog onCreateDialog(Bundle savedInstanceState) {
            activity = (NuevoDiaActivity)getActivity();
            context = getContext();
            modo = true;

            alertDialog = createDayView();

            return alertDialog;
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
                    if(modo){
                        activity.setMovil(picker.getValue(),pickerDay.getValue(),pickerMonth.getValue());
                    }else{
                        activity.setMovilDay(pickerAbsoluteDay.getValue());
                    }

                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    switchMode();
                    if(modo){
                        alertDialog.dismiss();
                        alertDialog = createDayView();
                        alertDialog.show();
                    }else{
                        alertDialog.dismiss();
                        alertDialog = createAbsoluteDayView();
                        alertDialog.show();
                    }
                    break;
            }
        }
    }

    public static class DatePickerMonthDialogFragment extends DialogFragment implements DialogInterface.OnClickListener, NumberPicker.OnValueChangeListener {

        private Boolean withYear;
        private AlertDialog alertDialog;
        private NuevoDiaActivity activity;
        private Context context;
        private NumberPicker pickerDay, pickerMonth;
        private NumberPicker pickerYear;
        private int[] diasMesfragment;


        public AlertDialog createDayMonthView(){

            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);
            LinearLayout LL = new LinearLayout(context);
            LL.setOrientation(LinearLayout.HORIZONTAL);

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            withYear = getArguments().getBoolean("year");
            if(withYear){

                if(esBisisesto(year)){
                    diasMesfragment = diasMesBisiesto;
                }else{
                    diasMesfragment = diasMes;
                }

            }else{

                diasMesfragment = diasMesBisiesto;
            }

            pickerMonth = new NumberPicker(activity);
            pickerMonth.setMinValue(0);
            pickerMonth.setMaxValue(11);
            pickerMonth.setDisplayedValues( meses );
            pickerMonth.setValue(month);
            pickerMonth.setOnValueChangedListener(this);

            pickerDay = new NumberPicker(activity);
            pickerDay.setMinValue(1);
            pickerDay.setMaxValue(diasMesfragment[month]);
            pickerDay.setValue(day);
            pickerDay.setOnValueChangedListener(this);

            builder.setPositiveButton("Aceptar",this);
            builder.setNegativeButton("Cancelar",this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dimension, dimension);
            params.gravity = Gravity.CENTER;

            LinearLayout.LayoutParams dayPicerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dayPicerParams.weight = 1;

            LinearLayout.LayoutParams monthPicerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            monthPicerParams.weight = 1;

            LL.setLayoutParams(params);
            LL.addView(pickerDay,dayPicerParams);
            LL.addView(pickerMonth,monthPicerParams);

            if(withYear){


                pickerYear = new NumberPicker(activity);
                pickerYear.setMinValue(1);
                pickerYear.setMaxValue(year);
                pickerYear.setValue(year);
                pickerYear.setOnValueChangedListener(this);

                builder.setTitle("Dia, Mes y Año");

                LinearLayout.LayoutParams yearPicerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                yearPicerParams.weight = 1;

                LL.addView(pickerYear, yearPicerParams);
            }else{
                builder.setTitle("Dia y Mes");
            }

            builder.setView(LL);

            alertDialog = builder.create();
            return alertDialog;

        }

        public boolean esBisisesto(int year){
            if(year % 400 == 0){
                return true;
            }else if(year % 4 == 0 && year % 100 != 0){
                return true;
            }else{
                return false;
            }
        }


        public Dialog onCreateDialog(Bundle savedInstanceState) {
            activity = (NuevoDiaActivity)getActivity();
            context = getContext();
            alertDialog = createDayMonthView();
            return alertDialog;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
                case DialogInterface.BUTTON_POSITIVE:
                    if(withYear){
                        activity.setHistoricDate(pickerYear.getValue(),pickerMonth.getValue() + 1, pickerDay.getValue());
                    }else{
                        activity.setDate(pickerMonth.getValue() + 1,pickerDay.getValue());
                    }

                    break;
            }
        }

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            if(picker == pickerDay){
                if(oldVal == pickerDay.getMaxValue() && newVal == pickerDay.getMinValue()){
                    //Aumentar mes
                    int value = pickerMonth.getValue();
                    if(value == pickerMonth.getMaxValue()){
                        pickerMonth.setValue(pickerMonth.getMinValue());
                    }else{
                        pickerMonth.setValue(value + 1);
                    }
                }else if(oldVal == pickerDay.getMinValue() && newVal == pickerDay.getMaxValue()){
                    //Disminuir mes
                    int value = pickerMonth.getValue();
                    if(value == pickerMonth.getMinValue()){
                        pickerMonth.setValue(pickerMonth.getMaxValue());
                    }else{
                        pickerMonth.setValue(value - 1);
                    }
                }else if(oldVal == pickerDay.getMinValue() + 1 && newVal == pickerDay.getMinValue() + 2){
                    int value = pickerMonth.getValue();
                    pickerDay.setMaxValue(diasMesfragment[value]);
                }else if(oldVal == pickerDay.getMinValue() + 2 && newVal == pickerDay.getMinValue() + 1){
                    int value = pickerMonth.getValue();
                    pickerDay.setMaxValue(diasMesfragment[value - 1]);
                }
            }else if(picker == pickerMonth ){
                pickerDay.setMaxValue(diasMesfragment[newVal]);
            }else if(picker == pickerYear){
                Log.e("asd",""+newVal);
                if(esBisisesto(newVal)){
                    diasMesfragment = diasMesBisiesto;
                }else{
                    diasMesfragment = diasMes;
                }
                pickerDay.setMaxValue(diasMesfragment[pickerMonth.getValue()]);
            }

        }
    }
}
