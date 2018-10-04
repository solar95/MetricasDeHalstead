package com.example.solar.metricas;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Lista extends AppCompatActivity {

    DB_Controller controller;
    TextView tv1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        tv1 = findViewById(R.id.lista);

        controller = new DB_Controller(this,"",null,1);
        controller.mostrar(tv1);
    }

}
