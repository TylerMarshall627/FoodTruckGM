package com.example.trmarsh.foodtruckgm;

import android.content.Intent;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLoc, 16));

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
        startActivity(intent);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
