package com.example.trmarsh.foodtruckgm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.firebase.client.Firebase;

public class SignupActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mFirstN;
    private EditText mLastN;
    private EditText mPassWord;
    private CheckBox mIsOwner = null;

    public Firebase mRef;
    public Firebase mRefInstance;
    public Firebase mRefInstanceEmail;
    public Firebase mRefInstanceFirstN;
    public Firebase mRefInstanceLastN;
    public Firebase mRefInstancePassWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRef = new Firebase("https://foodtruck-38f8f.firebaseio.com/User");
        setContentView(R.layout.activity_signup);

        mEmail = (EditText) findViewById(R.id.editText);
        mFirstN = (EditText) findViewById(R.id.editText2);
        mLastN = (EditText) findViewById(R.id.editText3);
        mPassWord = (EditText) findViewById(R.id.editText4);
        mIsOwner = (CheckBox) findViewById(R.id.ownerCheckBox);

    }
    public void onAdd (View v){
        String email = mEmail.getText().toString();
        String firstN = mFirstN.getText().toString();
        String lastN = mLastN.getText().toString();
        String passW = mPassWord.getText().toString();

        mRefInstance = mRef.child(email);

        mRefInstanceEmail = mRefInstance.child("Email");
        mRefInstanceEmail.setValue(mEmail);
        mRefInstanceFirstN = mRefInstance.child("FirstName");
        mRefInstanceFirstN.setValue(firstN);
        mRefInstanceLastN = mRefInstance.child("LastName");
        mRefInstanceLastN.setValue(lastN);
        mRefInstancePassWord = mRefInstance.child("Password");
        mRefInstancePassWord.setValue(passW);

        mEmail.setText("");
        mFirstN.setText("");
        mLastN.setText("");
        mPassWord.setText("");

        if(mIsOwner.isChecked()){
            Intent intentL = new Intent(getApplicationContext(), TruckSignupActivity.class);
            startActivity(intentL);
        }else{
            Intent intentL = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intentL);
        }
    }

}
