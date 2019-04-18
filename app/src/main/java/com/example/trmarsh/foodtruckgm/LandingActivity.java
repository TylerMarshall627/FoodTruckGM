package com.example.trmarsh.foodtruckgm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
    }
    public void onSignup(View v){
        Intent intent = new Intent(getApplicationContext(), UserSignupActivity.class);
        startActivity(intent);
    }
    public void onLogin(View v){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
    public void onTruck(View v){
        Intent intent = new Intent(getApplicationContext(), UserSignupActivity.class);
        startActivity(intent);
    }
}
