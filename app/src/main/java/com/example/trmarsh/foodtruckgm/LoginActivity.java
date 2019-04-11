package com.example.trmarsh.foodtruckgm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassWord;

    public final static String Extra_String_Email = "com.example.trmarsh.todo.Extra";
    public final static String Extra_String_First = "";
    public final static String Extra_String_Last = "";


    private Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText) findViewById(R.id.editText);
        mPassWord = (EditText) findViewById(R.id.editText2);
    }
    public void onLogin (View v){
        mRef = new Firebase("https://foodtruck-38f8f.firebaseio.com/User");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            // handle to output in the format of the map while logging the snapshot
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Map<String, String>> map = dataSnapshot.getValue(Map.class);
                Log.i("data", dataSnapshot.getValue().toString());
                Log.i("data-map", map.toString());

                // if we don't have any value in the username, don't do operation
                if(!mEmail.getText().toString().equals("")){
                    // use the entered username to check the password
                    Map<String, String> matchMap = map.get(mEmail.getText().toString());
                    String matchPassword = matchMap.get("PassWord");
                    // Store the First name and Last name in a string
                    String matchEmail = matchMap.get("Email");
                    String matchFirstName = matchMap.get("FirstName");
                    String matchLastName = matchMap.get("LastName");
                    // if the password matches, send a success message. Otherwise send fail message
                    if(matchPassword.equals(mPassWord.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                        // Send the User to a welcome page with the indicated first and last name
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        intent.putExtra(Extra_String_Email, matchEmail);
                        intent.putExtra(Extra_String_First, matchFirstName);
                        intent.putExtra(Extra_String_Last, matchLastName);
                        startActivity(intent);
                    } else {
                        // send error and do not redirect
                        Toast.makeText(getApplicationContext(), "Password does not match!", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });




    }
}
