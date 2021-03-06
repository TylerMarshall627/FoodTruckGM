package com.example.trmarsh.foodtruckgm;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.trmarsh.foodtruckgm.LoginActivity.Extra_String_UserN;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private Button btnReviews, btnMap, btnProfile;

    private GoogleMap mMap;
    private Firebase mRef;

    private String loggedInUser = null;
    private HashMap<String, ArrayList<String>> truckLocations;

    private double[] latlng = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        loggedInUser = getIntent().getStringExtra(LoginActivity.Extra_String_UserN);
        latlng = getIntent().getDoubleArrayExtra(TruckPage.Extra_String_LatLng);
        startMenuButtonListeners(2);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        if (latlng != null) {
            LatLng pos = new LatLng(latlng[0], latlng[1]);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 13));
        } else {
            LatLng startLoc = new LatLng(36.1313586, -97.073077);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLoc, 13));
        }

        ReadData();

        mMap.setOnMarkerClickListener(this);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                switchToTruckPage(marker);
            }
        });
    }

    private void ReadData() {
        truckLocations = new HashMap<>();
        Firebase.setAndroidContext(this);
        mRef = new Firebase("https://foodtruck-38f8f.firebaseio.com/Truck");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                truckLocations = new HashMap<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    ArrayList<String> item = new ArrayList<>();

                    String truckName = snap.child("Name").getValue().toString();
                    Object lat = snap.child("Lat").getValue();
                    Object lng = snap.child("Lng").getValue();

                    if (lat == null || lng == null || lat.toString().equals("0") || lng.toString().equals("0")) {
                        continue;
                    }

                    item.add(truckName);
                    item.add(lat.toString());
                    item.add(lng.toString());

                    truckLocations.put(truckName, item);
                }

                UpdatePins();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    private void UpdatePins() {
        for (Map.Entry<String, ArrayList<String>> entry : truckLocations.entrySet()) {
            String truckName = entry.getKey();
            ArrayList<String> item = entry.getValue();
            Double lat = Double.valueOf(item.get(1));
            Double lng = Double.valueOf(item.get(2));
            LatLng newPin = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(newPin).title(truckName));
        }
    }

    private String lastMarker = "";

    @Override
    public boolean onMarkerClick(Marker marker) {
        String truckName = marker.getTitle();
        if (!lastMarker.equals(truckName)) {
            marker.showInfoWindow();
            lastMarker = truckName;
        } else {
            lastMarker = "";
            switchToTruckPage(marker);
        }
        return true;
    }

    private void switchToTruckPage(Marker marker) {
        String truckName = marker.getTitle();
        Intent intent = new Intent(getApplicationContext(), TruckPage.class);
        intent.putExtra(TruckPage.Extra_String_TruckName, truckName);
        intent.putExtra(LoginActivity.Extra_String_UserN, loggedInUser);
        startActivity(intent);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
