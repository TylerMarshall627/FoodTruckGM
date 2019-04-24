package com.example.trmarsh.foodtruckgm;

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

public class ReviewCreateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ReviewCreateActivity thisThing;

    private EditText rText;
    private RatingBar ratingBar;
    private Spinner spinner;


    private Firebase rRef;
    private Firebase rRefInstance;

    private ArrayList<String> truckNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_create);
        this.thisThing = this;

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        rText = (EditText) findViewById(R.id.editText);
        ratingBar = findViewById(R.id.ratingBar);

        updateTruckSelector();
    }



    private void updateTruckSelector() {
        truckNames = new ArrayList<>();

        Firebase.setAndroidContext(this);
        rRef = new Firebase("https://foodtruck-38f8f.firebaseio.com/Truck");
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void onAdd(View v) {
        String rateUser = getIntent().getStringExtra(LoginActivity.Extra_String_UserN);
        String rateText = rText.getText().toString();
        String rateStars = String.valueOf(ratingBar.getNumStars());
        String rateTruck = spinner.toString();

    }
}
