package com.example.trmarsh.foodtruckgm;

import android.content.Intent;
import android.os.Handler;
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

        // go back to reviews if not logged in
        loggedInUser = getIntent().getStringExtra(LoginActivity.Extra_String_UserN);
        if (loggedInUser == null) {
            Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
            intent.putExtra(LoginActivity.Extra_String_UserN, loggedInUser);
            startActivity(intent);
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
        rRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                truckNames = new ArrayList<>();
                truckNames.add("- select truck -");
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String truckName = snap.child("Name").getValue().toString();

                    truckNames.add(truckName);
                }

                // show the array contents in spinner to pick from
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(thisThing, android.R.layout.simple_spinner_item, truckNames);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }


    private String selectedTruck = null;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if (!item.equals("- select truck -")) {
            selectedTruck = item;
            Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void onAdd(View v) {
        Firebase rRef = new Firebase("https://foodtruck-38f8f.firebaseio.com");

        String rateText = rText.getText().toString();
        String rateStars = String.valueOf(ratingBar.getRating());
        String rateTruck = selectedTruck;

        if (!rateStars.equals("0.0") && rateTruck != "- select truck -" && rateTruck!= null) {
            // assign a random primary key
            Random rand = new Random();
            int n = rand.nextInt(99999);
            String rateID = Integer.toString(n);

            rRefInstance = rRef.child("Review").child(rateID);
            rRefInstance.child("User").setValue(loggedInUser);
            rRefInstance.child("Text").setValue(rateText);
            rRefInstance.child("Rating").setValue(rateStars);
            rRefInstance.child("Truck").setValue(rateTruck);

            // send back to all reviews
            Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
            intent.putExtra(LoginActivity.Extra_String_UserN, loggedInUser);
            startActivity(intent);
        } else {
            if (rateStars.equals("0.0")) {
                Toast.makeText(getApplicationContext(), "Must assign a star rating.", Toast.LENGTH_SHORT).show();
            }
            if (rateTruck != "- select truck -") {
                Toast.makeText(getApplicationContext(), "You didn't select a truck!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
