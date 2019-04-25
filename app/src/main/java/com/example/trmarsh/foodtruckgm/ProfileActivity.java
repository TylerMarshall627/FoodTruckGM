package com.example.trmarsh.foodtruckgm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.trmarsh.foodtruckgm.LoginActivity.Extra_String_UserN;

public class ProfileActivity extends AppCompatActivity {

    private Button btnReviews, btnMap, btnProfile;

    private String loggedInUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        loggedInUser = getIntent().getStringExtra(Extra_String_UserN);

        if (loggedInUser == null) {
            //TODO go to a page with login and signup buttons
        }

        startMenuButtonListeners(3);


        //TODO replace this with database calls looking for the user's info
        String recEmail = getIntent().getStringExtra(LoginActivity.Extra_String_Email);
        String recFirstN = getIntent().getStringExtra(LoginActivity.Extra_String_First);
        String recLastN = getIntent().getStringExtra(LoginActivity.Extra_String_Last);

        TextView uv = (TextView) findViewById(R.id.textView2);
        uv.setText(loggedInUser);
        TextView ev = (TextView) findViewById(R.id.textView3);
        ev.setText(recEmail);
        TextView fv = (TextView) findViewById(R.id.textView4);
        fv.setText(recFirstN);
        TextView lv = (TextView) findViewById(R.id.textView5);
        lv.setText(recLastN);
    }

    private void startMenuButtonListeners(final int thing) {
        btnReviews = findViewById(R.id.btn_reviews);
        btnReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thing != 1) {
                    Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                    intent.putExtra(Extra_String_UserN, loggedInUser);
                    startActivity(intent);
                }
            }
        });

        btnMap = findViewById(R.id.btn_map);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thing != 2) {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra(Extra_String_UserN, loggedInUser);
                    startActivity(intent);
                }
            }
        });

        btnProfile = findViewById(R.id.btn_profile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thing != 3) {
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra(Extra_String_UserN, loggedInUser);
                    startActivity(intent);
                }
            }
        });
        if (thing == 1) {
            btnReviews.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            btnReviews.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        if (thing == 2) {
            btnMap.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            btnMap.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        if (thing == 3) {
            btnProfile.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            btnProfile.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
    }

    public void onReview(View v){
        Intent intent = new Intent(getApplicationContext(), ReviewCreateActivity.class);
        intent.putExtra(Extra_String_UserN, loggedInUser);
        startActivity(intent);
    }


}
