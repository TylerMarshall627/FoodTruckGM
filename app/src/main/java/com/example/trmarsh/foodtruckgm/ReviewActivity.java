package com.example.trmarsh.foodtruckgm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.trmarsh.foodtruckgm.LoginActivity.Extra_String_UserN;

public class ReviewActivity extends AppCompatActivity {
    private ReviewActivity thisPage;

    private Button btnReviews, btnMap, btnProfile;
    private ListView reviewsList;
    private Button btnMakeReview;

    private List<String> reviews;
    private String loggedInUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        this.thisPage = this;
        Firebase.setAndroidContext(this);

        loggedInUser = getIntent().getStringExtra(Extra_String_UserN);

        // set create a review button visible only if logged in
        btnMakeReview = findViewById(R.id.btn_makereview);
        if (loggedInUser != null ){
            btnMakeReview.setVisibility(View.VISIBLE);
        } else {
            btnMakeReview.setVisibility(View.GONE);
        }
        reviewsList = findViewById(R.id.lst_reviews);

        startMenuButtonListeners(1);
        updateReviews();
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

    private void updateReviews() {
        reviews = new ArrayList<>();
        Firebase reviewRef = new Firebase("https://foodtruck-38f8f.firebaseio.com/Review");
        reviewRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reviews = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String reviewTruck = snap.child("Truck").getValue().toString();
                    String reviewUser = snap.child("User").getValue().toString();
                    String reviewText = snap.child("Text").getValue().toString();
                    String reviewRating = snap.child("Rating").getValue().toString();
                        StringBuilder strBld = new StringBuilder();
                        strBld.append(String.format("[%s] %s/5", reviewTruck, reviewRating));
                        strBld.append("\n");
                        strBld.append(reviewText);
                        strBld.append("\n");
                        strBld.append(String.format("By: %s", reviewUser));
                        reviews.add(strBld.toString());
                }
                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(thisPage, R.layout.review_item_layout, reviews);
                reviewsList.setAdapter(itemsAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public void onReview(View v){
        // send to review creation activity while storing user
        Intent intent = new Intent(getApplicationContext(), ReviewCreateActivity.class);
        intent.putExtra(Extra_String_UserN, loggedInUser);
        startActivity(intent);
    }

    
}
