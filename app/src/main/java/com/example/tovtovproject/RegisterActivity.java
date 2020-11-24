package com.example.tovtovproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {


    public static final String PAGE_ARGUMENT = "pageArgument" , PAGE_ARGUMENT_REGISTER = "register",  PAGE_ARGUMENT_CHANGE_DETAILS = "changeDetails";
    private final int VALID_USERNAME = 1  ,VALID_ADDRESS = 2, VALID_PASSWORD = 3, VALID_NAME = 4, VALID_PHONE_NUMBER = 5;

    //textbox inputs
    private SmartTextField userName;
    private SmartTextField password;
    private SmartTextField confirmPassword;
    private SmartTextField phoneNumber;
    private SmartTextField firstName;
    private SmartTextField lastName;
    private SmartTextField fullAddress;
    private Button pageButton;
    private String pageArgument;
    private ImageView pageTitleSignUp;
    private ImageView pageTitleAccountCredentials;

    //database declarations
    private FirebaseDatabase rootNode;
    private DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        //define SmartTextViews
        pageButton = findViewById(R.id.signIN_button);
        pageTitleSignUp = findViewById(R.id.signUP_logo_img);
        pageTitleAccountCredentials = findViewById(R.id.myAccountDetails);

        userName = new SmartTextField( (TextView) findViewById(R.id.userNameText), (TextView) findViewById(R.id.userNameError ) , "userName");
        password = new SmartTextField( (TextView) findViewById(R.id.passwordText), (TextView) findViewById(R.id.passwordError ) , "password");
        confirmPassword = new SmartTextField((TextView) findViewById(R.id.confirmPasswordText), (TextView) findViewById(R.id.confirmPasswordError ), "confirmPassword");
        phoneNumber = new SmartTextField((TextView) findViewById(R.id.phoneNumberText), (TextView) findViewById(R.id.phoneNumberError) , "phoneNumber");
        firstName = new SmartTextField( (TextView) findViewById(R.id.firstNameText), (TextView) findViewById(R.id.firstNameError ), "firstName");
        lastName = new SmartTextField( (TextView) findViewById(R.id.lastNameText), (TextView) findViewById(R.id.lastNameError ) , "lastName");
        fullAddress = new SmartTextField( (TextView) findViewById(R.id.fullAddressText), (TextView) findViewById(R.id.fullAddressError ) , "fullAddress");

        Intent intent = getIntent();
        pageArgument = intent.getStringExtra(PAGE_ARGUMENT);
        setContentViewByPageArgument(pageArgument);

}

    private void setContentViewByPageArgument(String pageArgument){
        if(pageArgument != null) {
            switch(pageArgument){
                case PAGE_ARGUMENT_REGISTER: makeSignUpPage(); break;
                case PAGE_ARGUMENT_CHANGE_DETAILS: makeChangeDetailsPage(); break;
                default: Toast.makeText(getApplicationContext(), "Something went wrong: Couldn't get pageArgument!", Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(getApplicationContext(), "Something went wrong: Couldn't get pageArgument!", Toast.LENGTH_LONG).show();
    }

    public void onSignUpClick(View view){
        rootNode = FirebaseDatabase.getInstance();
        userReference = rootNode.getReference("Users");
        if(validInput())
            createUser();
    }

    private void makeSignUpPage(){
        pageTitleSignUp.setVisibility(View.VISIBLE);
        pageButton.setText("הצטרף לעשיית הטוב");

    }

    private void makeChangeDetailsPage(){
        pageTitleAccountCredentials.setVisibility(View.VISIBLE);
        pageButton.setText("שנה פרטים");
        userName.getTextLayout().setText(CurrentUser.getUserName());
        userName.getTextLayout().setEnabled(false);
    }

    private boolean validInput(){
        // set the flags according to the input.
        boolean userNameFlag = isValid(userName) , passwordFlag = isValid(password), confirmPasswordFlag = isValid(confirmPassword) , phoneNumberFlag = isValid(phoneNumber),
                firstNameFlag = isValid(firstName) , lastNameFlag = isValid(lastName) , fullAddressFlag = isValid(fullAddress); // we assign flag first so all the methods will executed. if we put everything into 1 if statement and use ' && ' operator not every method will be executed
        return userNameFlag && passwordFlag && confirmPasswordFlag && phoneNumberFlag && firstNameFlag && lastNameFlag && fullAddressFlag; // return only if all of the inputs are correct
    }

    @SuppressLint("SetTextI18n")
    private boolean isValid(SmartTextField textField){
        boolean isValid = true;
        String input = textField.getTextLayout().getText().toString();
        // this "switch" is checking what kind of textView we are at the moment, then sends the input to the method "isValidInput" to know if the input is correct
        // if the input is incorrect we set an error using textField.setErrorLayoutText

        switch (textField.getFieldName()) // check what's the hint of the textBox
        {
            case "userName":
                if (!isValidInput(input,VALID_USERNAME)) { // if the value is not valid put an error
                    textField.setErrorLayoutText("שם משתמש חייב להכיל בתוכו מספרים וגם אותיות באנגלית");
                    isValid = false;
                }
                break;
            case "password":
                if (!isValidInput(input,VALID_PASSWORD)) {
                    textField.setErrorLayoutText("הסיסמא חייבת להכיל לפחות 6 אותיות (גדולות, קטנות ומספרים)");
                    isValid = false;
                }
                else
                    textField.setErrorLayoutText("");
                break;
            case "confirmPassword":
                String password = this.password.getTextLayout().getText().toString() , confirmPassword = textField.getTextLayout().getText().toString();
                if(!confirmPassword.equals(password)) {
                    textField.setErrorLayoutText("אין התאמה בין הסיסמאות");
                    isValid = false;
                }
                else
                    textField.setErrorLayoutText("");
                break;
            case "phoneNumber":
                if (!isValidInput(input,VALID_PHONE_NUMBER)) {
                    textField.setErrorLayoutText("מספר איננו חוקי");
                    isValid = false;
                }
                else
                    textField.setErrorLayoutText("");
                break;
            case "fullAddress":
                if (!isValidInput(input,VALID_ADDRESS)) {
                    textField.setErrorLayoutText("הכתובת חייבת להכיל רק אותיות ומספרים");
                    isValid = false;
                }
                else
                    textField.setErrorLayoutText("");
                break;
            default: // first name , last name all of them has the same condition ( must contain letters only )
                if(!isValidInput(input, VALID_NAME)) {
                    textField.setErrorLayoutText(textField.toString() + " חייב להכיל רק אותיות");
                    isValid = false;
                }
                else
                    textField.setErrorLayoutText("");
                break;
        }

        // this "if" is checking if the input is empty, if it is we set an error
        if(input.isEmpty()) {
            if(pageArgument.contains(PAGE_ARGUMENT_REGISTER)) {
                textField.setErrorLayoutText("שדה זה לא יכול להשאר ריק!");
                isValid = false;
            }
            else if(pageArgument.contains(PAGE_ARGUMENT_CHANGE_DETAILS)){
                isValid = true;
            }
        }
        return isValid;
    }

    private boolean isValidInput(String input, int option){
        if(pageArgument.contains(PAGE_ARGUMENT_CHANGE_DETAILS) && input.isEmpty())
            return true;

        boolean valid = false, numbersFlag = false, phoneNumberFlag = false , lowerLettersFlag = false, upperLettersFlag = false, invalidCharacter = false, fullAddressFlag = false;

        // this "for" is changing the flags, depends on the characters we have in the variable "input"
        for(int i = 0; i < input.length() && !invalidCharacter ; i++){
            if ((input.charAt(i) + "").matches("[0-9]")) numbersFlag = true;
            else if ((input.charAt(i) == '+') || (input.charAt(i) == '-')) phoneNumberFlag = true;
            else if ((input.charAt(i) + "").matches("[a-z]")) lowerLettersFlag = true;
            else if ((input.charAt(i) + "").matches("[A-Z]")) upperLettersFlag = true;
            else if (input.charAt(i) == ' ') fullAddressFlag = true;
            else invalidCharacter = true;
        }

        // the switch is checking if the input is valid using the flags the "for" assigned earlier.
        switch(option)
        {
            case VALID_PHONE_NUMBER:
                if ( numbersFlag && !lowerLettersFlag && !upperLettersFlag  && input.length() > 8)
                    valid = true;
                break;
            case VALID_PASSWORD:
                if (lowerLettersFlag && upperLettersFlag && numbersFlag  && input.length() > 5)
                    valid = true;
                break;
            case VALID_USERNAME:
                if ((lowerLettersFlag || upperLettersFlag)  )
                    valid = true;
                break;
            case VALID_NAME:
                if ((lowerLettersFlag || upperLettersFlag) && !numbersFlag )
                    valid = true;
                break;
            case VALID_ADDRESS:
                if (lowerLettersFlag && (upperLettersFlag || numbersFlag || fullAddressFlag))
                    valid = true;
                break;
        }

        if(invalidCharacter)
            return false;
        return valid;
    }

    private void createUser(){
        Query checkUser = userReference.orderByChild("userName").equalTo(userName.getTextLayout().getText().toString()); // this checks the "userName" folder for every user that we have in the fireBase (in the "users" folder)

        checkUser.addListenerForSingleValueEvent( new ValueEventListener() { //
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && pageArgument.contains(PAGE_ARGUMENT_REGISTER)) // if the data exists we know that the username is already taken!
                    userName.setErrorLayoutText("שם משתמש כבר קיים!"); // set the error
                else if(pageArgument.contains(PAGE_ARGUMENT_REGISTER)){ // if the username doesn't exists we can now safely create this userName
                    userName.setErrorLayoutText(""); // reset the error message so it wont confuse the user
                    String userNameString = userName.getTextLayout().getText().toString(); // we take the strings from the textBoxes
                    String dateCreated = new Date().toString();
                    String passwordString = password.getTextLayout().getText().toString();
                    String fullAddressString = fullAddress.getTextLayout().getText().toString();

                    UserHelperClass userClass = new UserHelperClass( // we create the user object so we can save it to the dataBase
                            dateCreated,
                            userNameString,
                            hashPassword(dateCreated, passwordString), // hash the password
                            phoneNumber.getTextLayout().getText().toString(),
                            firstName.getTextLayout().getText().toString(),
                            lastName.getTextLayout().getText().toString(),
                            fullAddressString,
                            getLocationByAddress(fullAddressString)
                    );
                    userReference.child(userNameString).setValue(userClass); // in the "Users" folder create new folder and set the name of that folder as "userNameString"
                    // then put the information  ( "userClass" object ) that belongs to the user (like firstName, email etc...) to that folder
                    createdUserIsSuccessful();
                }
                else if (pageArgument.contains(PAGE_ARGUMENT_CHANGE_DETAILS)){
                    String userNameString = CurrentUser.getUserName(); // we take the strings from the textBoxes
                    String passwordString = password.getTextLayout().getText().toString();
                    String fullAddressString = fullAddress.getTextLayout().getText().toString();
                    String phoneNumberString = phoneNumber.getTextLayout().getText().toString();
                    String firstNameString = firstName.getTextLayout().getText().toString();
                    String lastNameString = lastName.getTextLayout().getText().toString();

                    if(!passwordString.isEmpty()) {
                        String dateCreated = new Date().toString();
                        userReference.child(userNameString).child("dateCreated").setValue(dateCreated);
                        userReference.child(userNameString).child("password").setValue(hashPassword(dateCreated, passwordString));
                    }
                    if(!fullAddressString.isEmpty()) {
                        String coord = getLocationByAddress(fullAddressString);
                        userReference.child(userNameString).child("fullAddress").setValue(fullAddressString);
                        userReference.child(userNameString).child("addressCoordinates").setValue(coord);
                        CurrentUser.setFullAddress(fullAddressString);
                        CurrentUser.setAddressCoordinates(coord);
                    }
                    if(!phoneNumberString.isEmpty())
                        userReference.child(userNameString).child("phoneNumber").setValue(phoneNumberString);
                    if(!firstNameString.isEmpty())
                        userReference.child(userNameString).child("firstName").setValue(firstNameString);
                    if(!lastNameString.isEmpty())
                        userReference.child(userNameString).child("lastName").setValue(lastNameString);

                    // then put the information  ( "userClass" object ) that belongs to the user (like firstName, email etc...) to that folder
                    changedUserDetailsIsSuccessful();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String getLocationByAddress(String addressString){
        if(addressString == null) return null;
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault() );
        String cordinates = "0,0";
        try {
            List addressList = geocoder.getFromLocationName(addressString, 1);
            if(addressList != null && addressList.size() > 0) {
                Address address = (Address) addressList.get(0);
                cordinates = "" +  address.getLatitude() + "," + address.getLongitude();
                Log.i("title: " , cordinates);
            }
            else
                Toast.makeText(getApplicationContext(), "Couldn't get address coordinates", Toast.LENGTH_SHORT).show();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return cordinates;
    }

    private String hashPassword(String dateCreated, String password){
        String hashValue = "" , secretWord = "@SecretWordTov@" , saltedPassword;
        if(password.isEmpty() || dateCreated == null)
            return null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");// we put the password and the username and the secret word next to each other and hash them to get more secured hash password
            saltedPassword = password + dateCreated + secretWord;// this will store the saltedPassword as bytes into memory
            messageDigest.update(saltedPassword.getBytes());// this will call the hash algorithm
            byte[] digestedBytes = messageDigest.digest();// we convert the hash bytes into string
            hashValue = new String(digestedBytes);

        } catch (NoSuchAlgorithmException e) {// if the "SHA-256" algorithm does not exist
            e.printStackTrace();
        }
        return hashValue;
    }

    private void createdUserIsSuccessful(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "משתמש נוצר בהצלחה!", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "התחבר בבקשה", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void changedUserDetailsIsSuccessful(){
        Toast.makeText(getApplicationContext(), "הפרטים נשמרו!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String generateUserID(){
        Random random = new Random();
        return (1 + random.nextInt(1000000)) +  "";
    }
}