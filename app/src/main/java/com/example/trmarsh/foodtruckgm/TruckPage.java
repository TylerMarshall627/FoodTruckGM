package com.example.trmarsh.foodtruckgm;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TruckPage extends AppCompatActivity {
    private TruckPage thisPage;

    public final static String Extra_String_TruckName = "com.example.trmarsh.foodtruckgm.truckname";

    private TextView bigTruckname, truckDescription, truckLocation;
    private Button instagram, twitter, facebook;
    private ListView reviewsListView;

    private ArrayList<String> reviews;
    private String instagramURL = "";
    private String twitterURL = "";
    private String facebookURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_page);
        thisPage = this;

        bigTruckname = findViewById(R.id.lbl_bigTruckName);
        truckDescription = findViewById(R.id.tv_truckDescription);
        truckLocation = findViewById(R.id.tv_truckLocation);
        instagram = findViewById(R.id.btn_instagram);
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(instagramURL));
                startActivity(intent);
            }
        });
        twitter = findViewById(R.id.btn_twitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(twitterURL));
                startActivity(intent);
            }
        });
        facebook = findViewById(R.id.btn_facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(facebookURL));
                startActivity(intent);
            }
        });
        reviewsListView = findViewById(R.id.lst_reviews);

        String truckName = getIntent().getStringExtra(Extra_String_TruckName);
        Firebase.setAndroidContext(this);
        updateTruckInfo(truckName);
    }

    private void updateTruckInfo(final String truckName) {
        Firebase truckRef = new Firebase("https://foodtruck-38f8f.firebaseio.com/Truck");
        truckRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reviews = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String sTruckName = snap.child("Name").getValue().toString();
                    String sBio = snap.child("Bio").getValue().toString();
                    String sFacebook = snap.child("Facebook").getValue().toString();
                    String sInstagram = snap.child("Instagram").getValue().toString();
                    String sTwitter = snap.child("Twitter").getValue().toString();
                    String sOwner = snap.child("Owner").getValue().toString();
                    String sLat = snap.child("Lat").getValue().toString();
                    String sLng = snap.child("Lng").getValue().toString();
                    if (sTruckName.equalsIgnoreCase(truckName)) {
                        bigTruckname.setText(sTruckName);
                        truckDescription.setText(sBio);

                        //TODO get address from latlng
                        truckLocation.setText("");

                        instagramURL = sInstagram;
                        twitterURL = sTwitter;
                        facebookURL = sFacebook;
                        break;
                    }
                }
                updateReviews(truckName);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    private void updateReviews(final String truckName) {
        reviews = new ArrayList<>();
        Firebase reviewRef = new Firebase("https://foodtruck-38f8f.firebaseio.com/Review");
        reviewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reviews = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String reviewTruck = snap.child("TruckName").getValue().toString();
                    String reviewUser = snap.child("UserName").getValue().toString();
                    String reviewText = snap.child("Text").getValue().toString();
                    String reviewRating = snap.child("Rating").getValue().toString();
                    if (reviewTruck.equalsIgnoreCase(truckName)) {
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
