package com.example.trmarsh.foodtruckgm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TruckPage extends AppCompatActivity {
    private TruckPage thisPage;

    public final static String Extra_String_TruckName = "com.example.trmarsh.foodtruckgm.truckname";

    private Firebase mRef;

    private ListView reviewsListView;

    private ArrayList<String> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_page);
        thisPage = this;

        reviewsListView = findViewById(R.id.lst_reviews);

        String truckName = getIntent().getStringExtra(Extra_String_TruckName);
        updateReviews(truckName);
    }

    private void updateReviews(final String truckName) {
        reviews = new ArrayList<>();
        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://foodtruck-38f8f.firebaseio.com/Review");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reviews = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String reviewTruck = snap.child("TruckName").getValue().toString();
                    String reviewUser = snap.child("UserName").getValue().toString();
                    String reviewText = snap.child("Text").getValue().toString();
                    String reviewRating = snap.child("Rating").getValue().toString();
                    if (reviewTruck.equalsIgnoreCase(truckName)) {
                        System.out.println("INSIDE");
                        StringBuilder strBld = new StringBuilder();
                        strBld.append(String.format("[%s] %s/10", reviewTruck, reviewRating));
                        strBld.append("\n");
                        strBld.append(reviewText);
                        strBld.append("\n");
                        strBld.append(String.format("By: %s", reviewUser));
                        reviews.add(strBld.toString());
                    }
                }
                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(thisPage, R.layout.review_item_layout, reviews);
                reviewsListView.setAdapter(itemsAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

}
