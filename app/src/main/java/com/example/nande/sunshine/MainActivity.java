package com.example.nande.sunshine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       if(savedInstanceState == null) {
           getSupportFragmentManager().beginTransaction().add(R.id.Main_fragmentcontainer, new ForecastFragment()).commit();
       }


    }
}
