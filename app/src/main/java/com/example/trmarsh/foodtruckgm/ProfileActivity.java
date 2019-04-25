package com.example.trmarsh.foodtruckgm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import static com.example.trmarsh.foodtruckgm.LoginActivity.Extra_String_UserN;
import static com.example.trmarsh.foodtruckgm.TruckPage.Extra_String_TruckName;

public class ProfileActivity extends AppCompatActivity {

    private Button btnReviews, btnMap, btnProfile;
    private TextView tvUser, tvFirst, tvLast, tvEmail;
    private Button btnTruckPage;

    private String loggedInUser = null;
    private String usersTruck = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        loggedInUser = getIntent().getStringExtra(Extra_String_UserN);

        // if no username is stored, send to homepage
        if (loggedInUser == null) {
            Intent intent = new Intent(getApplicationContext(), LandingActivity.class);
            intent.putExtra(Extra_String_UserN, loggedInUser);
            startActivity(intent);
        }


        startMenuButtonListeners(3);
        tvUser = findViewById(R.id.tv_userName);
        tvFirst = findViewById(R.id.tv_first);
        tvLast = findViewById(R.id.tv_last);
        tvEmail = findViewById(R.id.tv_email);
        btnTruckPage = findViewById(R.id.btn_truckPage);
        btnTruckPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TruckPage.class);
                intent.putExtra(Extra_String_TruckName, usersTruck);
                intent.putExtra(Extra_String_UserN, loggedInUser);
                startActivity(intent);
            }
        });

        showUserInfo(loggedInUser);
    }

    private void showUserInfo(final String userName) {
        Firebase reviewRef = new Firebase("https://foodtruck-38f8f.firebaseio.com/User");
        reviewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // update the info inside the snapshot
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String user = snap.child("UserName").getValue().toString();
                    String first = snap.child("FirstName").getValue().toString();
                    String last = snap.child("LastName").getValue().toString();
                    String isOwner = snap.child("TruckOwner").getValue().toString();
                    if (user.equals(userName)) {
                        tvUser.setText(user);
                        tvFirst.setText(first);
                        tvLast.setText(last);

                        // when user is an owner in database, allow them to navigate to their truckpage
                        if (isOwner.equals("1")) {
                            for (DataSnapshot truckSnap : snap.child("Truck").getChildren()) {
                                usersTruck = truckSnap.child("Name").getValue().toString();
                                btnTruckPage.setVisibility(View.VISIBLE);
                            }
                        } else {
                            btnTruckPage.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    private void startMenuButtonListeners(final int thing) {
        // define listeners for all navigation buttons at top
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
        // coloring the navigation buttons
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

}
