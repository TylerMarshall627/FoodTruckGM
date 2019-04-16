package com.example.trmarsh.foodtruckgm;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Firebase mRef;

    private HashMap<String, ArrayList<String>> truckLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        LatLng startLoc = new LatLng(36.1313586, -97.073077);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLoc, 12));

        ReadData();

        mMap.setOnMarkerClickListener(this);
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
                    String lat = snap.child("Lat").getValue().toString();
                    String lng = snap.child("Lng").getValue().toString();

                    item.add(truckName);
                    item.add(lat);
                    item.add(lng);

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
}
