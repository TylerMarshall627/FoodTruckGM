package com.example.trmarsh.foodtruckgm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

public class ReviewCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_create);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemClickListener(this);


    }


}
