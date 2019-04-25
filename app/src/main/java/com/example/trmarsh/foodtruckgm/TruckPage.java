package com.example.trmarsh.foodtruckgm;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.trmarsh.foodtruckgm.LoginActivity.Extra_String_UserN;

public class TruckPage extends AppCompatActivity {
    private TruckPage thisPage;

    public final static String Extra_String_TruckName = "com.example.trmarsh.foodtruckgm.truckname";

    private Button btnReviews, btnMap, btnProfile;

    private TextView bigTruckname, truckDescription, truckLocation;
    private Button btnCheckin, instagram, twitter, facebook;
    private ListView reviewsListView;

    private ArrayList<String> reviews;
    private String instagramURL = "";
    private String twitterURL = "";
    private String facebookURL = "";

    private String loggedInUser = null;
    private String truckOwner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_page);
        thisPage = this;

        startMenuButtonListeners(0);

        String truckName = getIntent().getStringExtra(Extra_String_TruckName);
        loggedInUser = getIntent().getStringExtra(Extra_String_UserN);

        bigTruckname = findViewById(R.id.lbl_bigTruckName);
        truckDescription = findViewById(R.id.tv_truckDescription);
        truckLocation = findViewById(R.id.tv_truckLocation);

        startTruckButtonListeners();
        Firebase.setAndroidContext(this);
        updateTruckInfo(truckName);
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

    private void startTruckButtonListeners() {
        btnCheckin = findViewById(R.id.btn_checkin);
        btnCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loggedInUser != null && truckOwner.equals(loggedInUser)) {
                    String truck = bigTruckname.getText().toString();
                    if (btnCheckin.getText().toString().equals("Check-In")) {
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        Criteria criteria = new Criteria();
                        Location myLoc = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                        String latitude, longitude;
                        try {
                            latitude = String.valueOf(myLoc.getLatitude());
                            longitude = String.valueOf(myLoc.getLongitude());
                        } catch (NullPointerException e) {
                            latitude = "0";
                            longitude = "0";
                        }
                        setCheckinInfo(truck, latitude, longitude);

                        // kick them out to the map
                        Intent intent2 = new Intent(getApplicationContext(), MapsActivity.class);
                        intent2.putExtra(Extra_String_UserN, loggedInUser);
                        startActivity(intent2);
                    } else {
                        setCheckinInfo(truck, "0", "0");
                        btnCheckin.setText("Check-In");

                        Intent intent1 = new Intent(getApplicationContext(), TruckPage.class);
                        intent1.putExtra(Extra_String_UserN, loggedInUser);
                        intent1.putExtra(Extra_String_TruckName, truck);
                        startActivity(intent1);
                    }
                }
            }
        });

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
    }

    private void setCheckinInfo(final String truckName, String lat, String lng) {
        Firebase ref = new Firebase("https://foodtruck-38f8f.firebaseio.com");
        Firebase refInstance;

        refInstance = ref.child("Truck").child(truckName);
        refInstance.child("Lat").setValue(lat);
        refInstance.child("Lng").setValue(lng);

        refInstance = ref.child("User").child(loggedInUser).child("Truck").child(truckName);
        refInstance.child("Lat").setValue(lat);
        refInstance.child("Lng").setValue(lng);
    }

    private void updateTruckInfo(final String truckName) {
        Firebase truckRef = new Firebase("https://foodtruck-38f8f.firebaseio.com/Truck");
        truckRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                        truckOwner = sOwner;

                        if (loggedInUser != null && truckOwner.equals(loggedInUser)) {
                            btnCheckin.setVisibility(View.VISIBLE);
                            if (!(sLat.equals("0") && sLng.equals("0"))) {
                                btnCheckin.setText("Close Truck");
                            } else {
                                btnCheckin.setText("Check-In");
                            }
                        } else {
                            btnCheckin.setVisibility(View.GONE);
                        }

                        bigTruckname.setText(sTruckName);
                        truckDescription.setText(sBio);

                        if (!(sLat.equals("0") || sLng.equals("0"))) {
                            // turn latlong into street address
                            Geocoder geocoder = new Geocoder(thisPage);
                            List<Address> addressList = null;
                            boolean success = true;
                            try {
                                addressList = geocoder.getFromLocation(Double.parseDouble(sLat), Double.parseDouble(sLng), 1);
                            } catch (IOException e) {
                                success = false;
                                e.printStackTrace();
                            }

                            Address ad = null;
                            String address = null;
                            if (success) {
                                //get address and
                                ad = addressList.get(0);
                                address = ad.getAddressLine(0);
                            }

                            if (address != null) {
                                //TODO make the address a clickable thing that intent's out to navigation??????
                                truckLocation.setText("Current Location: " + address);
                            } else {
                                truckLocation.setText("");
                            }


                        } else {
                            //TODO put something in the truck location area
                            truckLocation.setText("");
                        }

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
                    String reviewTruck = snap.child("Truck").getValue().toString();
                    String reviewUser = snap.child("User").getValue().toString();
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
