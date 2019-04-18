package com.example.trmarsh.foodtruckgm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.firebase.client.Firebase;

public class UserSignupActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mUserName;
    private EditText mFirstN;
    private EditText mLastN;
    private EditText mPassWord;
    private CheckBox mIsOwner;

    public final static String Extra_String_UserName = "com.example.trmarsh.foodtruckgm.username";

    public Firebase mRef;
    public Firebase mRefInstance;
    public Firebase mRefInstanceEmail;
    public Firebase mRefInstanceUserName;
    public Firebase mRefInstanceFirstN;
    public Firebase mRefInstanceLastN;
    public Firebase mRefInstancePassWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRef.setAndroidContext(this);
        mRef = new Firebase("https://foodtruck-38f8f.firebaseio.com/User");
        setContentView(R.layout.activity_user_signup);

        mEmail = (EditText) findViewById(R.id.editText);
        mUserName = (EditText) findViewById(R.id.editText1);
        mFirstN = (EditText) findViewById(R.id.editText2);
        mLastN = (EditText) findViewById(R.id.editText3);
        mPassWord = (EditText) findViewById(R.id.editText4);
        mIsOwner = (CheckBox) findViewById(R.id.ownerCheckBox);



    }

    public void onAdd(View v) {
        String email = mEmail.getText().toString();
        String userN = mUserName.getText().toString();
        String firstN = mFirstN.getText().toString();
        String lastN = mLastN.getText().toString();
        String passW = mPassWord.getText().toString();

        mRefInstance = mRef.child(userN);

        mRefInstanceUserName = mRefInstance.child("UserName");
        mRefInstanceUserName.setValue(userN);

        mRefInstanceEmail = mRefInstance.child("Email");
        mRefInstanceEmail.setValue(email);
        mRefInstanceFirstN = mRefInstance.child("FirstName");
        mRefInstanceFirstN.setValue(firstN);
        mRefInstanceLastN = mRefInstance.child("LastName");
        mRefInstanceLastN.setValue(lastN);
        mRefInstancePassWord = mRefInstance.child("Password");
        mRefInstancePassWord.setValue(passW);

        if (mIsOwner.isChecked()) {
            mRefInstance.child("TruckOwner").setValue("1");
            Intent intentL = new Intent(getApplicationContext(), TruckSignupActivity.class);
            intentL.putExtra(Extra_String_UserName, userN);
            startActivity(intentL);
        } else {
            mRefInstance.child("TruckOwner").setValue("0");
            Intent intentL = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intentL);
        }
    }

}
