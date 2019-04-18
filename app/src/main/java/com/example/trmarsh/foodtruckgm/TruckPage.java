package com.example.trmarsh.foodtruckgm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TruckPage extends AppCompatActivity {

    public final static String Extra_String_TruckName = "com.example.trmarsh.foodtruckgm.truckname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_page);
    }
}
