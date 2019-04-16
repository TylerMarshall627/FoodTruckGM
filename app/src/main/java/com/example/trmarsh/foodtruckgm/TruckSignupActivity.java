package com.example.trmarsh.foodtruckgm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

import com.firebase.client.Firebase;

public class TruckSignupActivity extends AppCompatActivity {

    private EditText tName;
    private EditText tDesc;
    private EditText tFacebook;
    private EditText tInstagram;
    private EditText tTwitter;

    public final static String Extra_String_UserEmail = "";

    public Firebase tRef;
    public Firebase tRefInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_signup);

        tRef = new Firebase("https://spring2019-dfeb9.firebaseio.com");

        //tName = (EditText) findViewById(R.id.)

    }
}
