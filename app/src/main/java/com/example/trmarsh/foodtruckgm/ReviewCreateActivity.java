package com.example.trmarsh.foodtruckgm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class ReviewCreateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ReviewCreateActivity thisThing;

    private EditText rText;
    private RatingBar ratingBar;
    private Spinner spinner;

    private Firebase rRefInstance;

    private ArrayList<String> truckNames;
    private String loggedInUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_create);
        this.thisThing = this;

        loggedInUser = getIntent().getStringExtra(LoginActivity.Extra_String_UserN);
        if (loggedInUser == null) {
            //TODO just go back a page
        }

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        rText = (EditText) findViewById(R.id.editText);
        ratingBar = findViewById(R.id.ratingBar);

        Firebase.setAndroidContext(this);
        updateTruckSelector();
    }



    private void updateTruckSelector() {
        truckNames = new ArrayList<>();
        Firebase rRef = new Firebase("https://foodtruck-38f8f.firebaseio.com/Truck");
        rRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                truckNames = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String truckName = snap.child("Name").getValue().toString();

                    truckNames.add(truckName);
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(thisThing, android.R.layout.simple_spinner_item, truckNames);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }


    private String selectedTruck = "";
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        selectedTruck = item;
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void onAdd(View v) {
        Firebase rRef = new Firebase("https://foodtruck-38f8f.firebaseio.com");


        String rateText = rText.getText().toString();
        String rateStars = String.valueOf(ratingBar.getRating());
        String rateTruck = selectedTruck;
        Random rand = new Random();
        int n = rand.nextInt(9999);
        String rateID = Integer.toString(n);

        rRefInstance = rRef.child("Review").child(rateID);
        rRefInstance.child("User").setValue(loggedInUser);
        rRefInstance.child("Text").setValue(rateText);
        rRefInstance.child("Rating").setValue(rateStars);
        rRefInstance.child("Truck").setValue(rateTruck);


        Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
        intent.putExtra(LoginActivity.Extra_String_UserN,loggedInUser);
        startActivity(intent);

    }
}
