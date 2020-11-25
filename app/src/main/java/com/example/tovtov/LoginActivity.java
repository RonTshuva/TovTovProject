package com.example.tovtov;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    TextView errorLayout, userName, password;

    FirebaseDatabase rootNode;
    DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.login);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void login(View view){
        //login
        //Intent intent=new Intent(getApplicationContext(),ThreeGateActivity.class);
        //startActivity(intent);
        rootNode = FirebaseDatabase.getInstance();
        userReference = rootNode.getReference("Users");

        userName = findViewById(R.id.userNameText);
        password = findViewById(R.id.passwordText);
        errorLayout = findViewById(R.id.errorLayout);

        final String userNameEntered = userName.getText().toString();
        final String passwordEntered = password.getText().toString();

        Query checkUser = userReference.orderByChild("userName").equalTo(userNameEntered);
        checkUser.addListenerForSingleValueEvent( new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String dateCreated = dataSnapshot.child(userNameEntered).child("dateCreated").getValue(String.class);
                    String hashedPasswordFromDB = dataSnapshot.child(userNameEntered).child("password").getValue(String.class);
                    String hashedPasswordEntered = hashPassword(dateCreated, passwordEntered);

                    if(hashedPasswordFromDB.equals(hashedPasswordEntered)){
                        String date = new Date().toString();
                        userReference.child(userNameEntered).child("lastLoggedIn").setValue(date);
                        String email = dataSnapshot.child(userNameEntered).child("email").getValue(String.class);
                        String firstName = dataSnapshot.child(userNameEntered).child("firstName").getValue(String.class);
                        String lastName = dataSnapshot.child(userNameEntered).child("lastName").getValue(String.class);
                        String fullAddress = dataSnapshot.child(userNameEntered).child("fullAddress").getValue(String.class);
                        String phoneNumber = dataSnapshot.child(userNameEntered).child("phoneNumber").getValue(String.class);
                        String addressCoordinates = dataSnapshot.child(userNameEntered).child("addressCoordinates").getValue(String.class);
                        CurrentUser.setCurrentUser(userNameEntered, email, phoneNumber, firstName, lastName, fullAddress, addressCoordinates);
                        Intent intent=new Intent(getApplicationContext(),MainPortalActivity.class);
                        startActivity(intent);
                    }
                    else
                        errorLayout.setText("משתמש או סיסמא אינם נכונים");
                }
                else
                    errorLayout.setText("משתמש או סיסמא אינם נכונים");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String hashPassword(String userID, String password){
        String hashValue = "" , secretWord = "@SecretWordTov@" , saltedPassword;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            saltedPassword = password + userID + secretWord; // we put the password and the username and the secret word next to each other and hash them to get more secured hash password
            messageDigest.update(saltedPassword.getBytes()); // this will store the saltedPassword as bytes into memory
            byte[] digestedBytes = messageDigest.digest(); // this will call the hash algorithm
            hashValue = new String(digestedBytes); // we convert the hash bytes into string

        } catch (NoSuchAlgorithmException e) { // if the "SHA-256" algorithm does not exist
            e.printStackTrace();
        }
        return hashValue;
    }

    public void signUp(View view){
        Intent intent=new Intent(getApplicationContext(), RegisterActivity.class);
        intent.putExtra(RegisterActivity.PAGE_ARGUMENT, RegisterActivity.PAGE_ARGUMENT_REGISTER);
        startActivity(intent);
    }

}