package com.example.tovtovproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class GetContributionActivity extends Activity {

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference contributionReference;
    private static Contribution contributionHelper;
    private SmartTextField itemName;
    private SmartTextField itemDescription;
    private ImageView imageViewDescription;
    private Uri imageUri;

    private Contribution item;
    private TextView descriptionText, contactNameText, contactPhoneNumberText, contactAddressText;
    private Button assignToMeButton, transportationButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_result);
        imageViewDescription = findViewById(R.id.imageViewDescription);
        descriptionText = findViewById(R.id.descriptionTextView);
        contactNameText = findViewById(R.id.contactNameText);
        contactPhoneNumberText = findViewById(R.id.contactPhoneNumberText);
        contactAddressText = findViewById(R.id.contactAddressText);
        assignToMeButton = findViewById(R.id.assignToMeButton);
        transportationButton = findViewById(R.id.transportationButton);
        storage = FirebaseStorage.getInstance();

        if(contributionHelper != null){
            descriptionText.setText(contributionHelper.getDescription());
            contactNameText.setText(contributionHelper.getFirstName() + " " + contributionHelper.getLastName());
            contactAddressText.setText(contributionHelper.getFullAddress());
            contactPhoneNumberText.setText(contributionHelper.getPhoneNumber());
            setImageByContributionID(contributionHelper, imageViewDescription);
        }else
            Toast.makeText(getApplicationContext(), "couldn't find contribution", Toast.LENGTH_LONG).show();
    }



    public static void setContribution(Contribution contribution){
        contributionHelper = contribution;
    }



    public void assignToMe(View view){

        assignToMeButton.setEnabled(false);
        assignToMeButton.setVisibility(View.INVISIBLE);
        database = FirebaseDatabase.getInstance();

        contributionHelper.assignedUser(CurrentUser.getUserName(), CurrentUser.getFirstName(), CurrentUser.getLastName(), CurrentUser.getPhoneNumber(),
                CurrentUser.getFullAddress(),CurrentUser.getAddressCoordinates());

        database.getReference("Contributions").
                child(contributionHelper.getContributionType()).
                child(contributionHelper.getUserName()).
                child(contributionHelper.getContributionID()).setValue(contributionHelper);

        database.getReference("Contributions").
                child("Help").
                child(CurrentUser.getUserName()).
                child(contributionHelper.getContributionID()).setValue(contributionHelper);

        transportationButton.setEnabled(true);
        transportationButton.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(), "הוצמד אלייך בהצלחה!", Toast.LENGTH_LONG).show();
        /*contributionReference.child("assignedUserName").setValue(CurrentUser.getUserName());
        contributionReference.child("assignedPhoneNumber").setValue(CurrentUser.getPhoneNumber());
        contributionReference.child("assignedFirstName").setValue(CurrentUser.getFirstName());
        contributionReference.child("assignedLastName").setValue(CurrentUser.getLastName());
        contributionReference.child("assignedFullAddress").setValue(CurrentUser.getFullAddress());
        contributionReference.child("assignedAddressCoordinates").setValue(CurrentUser.getAddressCoordinates());
        contributionReference.child("assigned").setValue(true);*/
    }

    public void askForTransportation(View view){
        transportationButton.setEnabled(false);
        transportationButton.setVisibility(View.INVISIBLE);
        database.getReference("Contributions").
                child("Transport").
                child(CurrentUser.getUserName()).
                child(contributionHelper.getContributionID()).setValue(contributionHelper);
        Toast.makeText(getApplicationContext(), "בקשה לשליח התקבלה!", Toast.LENGTH_LONG).show();
    }

    private void setImageByContributionID(Contribution contribution, final ImageView imageView) {

        if(contribution.isHasImage()) {
            storageReference = storage.getReference()
                    .child("images")
                    .child(contribution.getContributionID());

            storageReference.getBytes(1024 * 1024)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imageView.setImageBitmap(bitmap);
                        }
                    });
        }
    }

}