package com.example.trmarsh.foodtruckgm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String recUserN = getIntent().getStringExtra(LoginActivity.Extra_String_UserN);
        String recEmail = getIntent().getStringExtra(LoginActivity.Extra_String_Email);
        String recFirstN = getIntent().getStringExtra(LoginActivity.Extra_String_First);
        String recLastN = getIntent().getStringExtra(LoginActivity.Extra_String_Last);

        TextView uv = (TextView) findViewById(R.id.textView2);
        uv.setText(recUserN);
        TextView ev = (TextView) findViewById(R.id.textView3);
        ev.setText(recEmail);
        TextView fv = (TextView) findViewById(R.id.textView4);
        fv.setText(recFirstN);
        TextView lv = (TextView) findViewById(R.id.textView5);
        lv.setText(recLastN);

    }



}
