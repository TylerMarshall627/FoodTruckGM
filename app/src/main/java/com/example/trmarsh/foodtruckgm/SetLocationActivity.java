package com.example.trmarsh.foodtruckgm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

import static com.example.trmarsh.foodtruckgm.LoginActivity.Extra_String_UserN;

public class SetLocationActivity extends AppCompatActivity {

    private EditText editLat, editLng;
    private Button btnSet;

    private String truck = null;
    private String loggedInUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        Firebase.setAndroidContext(this);

        loggedInUser = getIntent().getStringExtra(Extra_String_UserN);
        truck = getIntent().getStringExtra(TruckPage.Extra_String_TruckName);

        editLat = findViewById(R.id.edit_lat);
        editLng = findViewById(R.id.edit_lng);
        btnSet = findViewById(R.id.btn_setStuff);
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheckinInfo(truck, editLat.getText().toString(), editLng.getText().toString());
                Intent intent2 = new Intent(getApplicationContext(), MapsActivity.class);
                intent2.putExtra(Extra_String_UserN, loggedInUser);
                startActivity(intent2);
            }
        });
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

}
