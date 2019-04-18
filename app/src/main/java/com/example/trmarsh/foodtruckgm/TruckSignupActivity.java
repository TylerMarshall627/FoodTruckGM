package com.example.trmarsh.foodtruckgm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.firebase.client.Firebase;

public class TruckSignupActivity extends AppCompatActivity {

    private EditText tName;
    private EditText tDesc;
    private EditText tFacebook;
    private EditText tInstagram;
    private EditText tTwitter;

    public Firebase tRef;
    public Firebase tRefInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tRef.setAndroidContext(this);
        setContentView(R.layout.activity_truck_signup);

        tRef = new Firebase("https://foodtruck-38f8f.firebaseio.com");

        tName = (EditText) findViewById(R.id.editText);
        tDesc = (EditText) findViewById(R.id.editText2);
        tFacebook = (EditText) findViewById(R.id.editText3);
        tInstagram = (EditText) findViewById(R.id.editText4);
        tTwitter = (EditText) findViewById(R.id.editText5);

    }
    public void onAdd (View v){
        // get string value of username and password
        String truckName =  tName.getText().toString();
        String truckDesc =  tDesc.getText().toString();
        String truckFace =  tFacebook.getText().toString();
        String truckInsta =  tInstagram.getText().toString();
        String truckTwit =  tTwitter.getText().toString();

        String userName = getIntent().getStringExtra(UserSignupActivity.Extra_String_UserName);


        //create a child to the table object; this will be the instance child
        tRefInstance = tRef.child("Truck").child(truckName);
        tRefInstance.child("Name").setValue(truckName);
        tRefInstance.child("Bio").setValue(truckDesc);
        tRefInstance.child("Facebook").setValue(truckFace);
        tRefInstance.child("Instagram").setValue(truckInsta);
        tRefInstance.child("Twitter").setValue(truckTwit);
        tRefInstance.child("Lat").setValue("0");
        tRefInstance.child("Lng").setValue("0");
        tRefInstance.child("Owner").setValue(userName);


        // place the truck information in the user (owner's) information
        tRefInstance = tRef.child("User").child(userName).child("Truck").child(truckName);
        tRefInstance.child("Name").setValue(truckName);
        tRefInstance.child("Bio").setValue(truckDesc);
        tRefInstance.child("Facebook").setValue(truckFace);
        tRefInstance.child("Instagram").setValue(truckInsta);
        tRefInstance.child("Twitter").setValue(truckTwit);
        tRefInstance.child("Lat").setValue("0");
        tRefInstance.child("Lng").setValue("0");



        //empty the EditTexts on click
        tName.setText("");
        tDesc.setText("");
        tFacebook.setText("");
        tInstagram.setText("");
        tTwitter.setText("");

        //send to next activity
        // set latlong
        Intent intent = new Intent(getApplicationContext(), TruckPage.class);
        startActivity(intent);
    }
}
