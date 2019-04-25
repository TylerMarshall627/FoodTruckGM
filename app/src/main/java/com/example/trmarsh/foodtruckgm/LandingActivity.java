package com.example.trmarsh.foodtruckgm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static com.example.trmarsh.foodtruckgm.LoginActivity.Extra_String_UserN;

public class LandingActivity extends AppCompatActivity {

    private String loggedInUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        //get username if logged in
        loggedInUser = getIntent().getStringExtra(Extra_String_UserN);
    }
    public void onSignup(View v){
        // send to signup activity
        Intent intent = new Intent(getApplicationContext(), UserSignupActivity.class);
        intent.putExtra(Extra_String_UserN, loggedInUser);
        startActivity(intent);
    }
    public void onLogin(View v){
        // send to login activity
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra(Extra_String_UserN, loggedInUser);
        startActivity(intent);
    }
    public void onTruck(View v){
        // go to map
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra(Extra_String_UserN, loggedInUser);
        startActivity(intent);
    }
}
