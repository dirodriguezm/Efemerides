package com.example.diego.efemerides;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NuevoDiaActivity extends AppCompatActivity {

    public static int DIA = 0;
    public static int DIA_HIST = 1;
    public static int DIA_MOVIL = 2;
    public static int CUMPLE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_dia);
    }
}
