package com.example.tovtov;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AssignedContributionActivity extends Activity {

    public static Contribution contributionHelper;

    private String PAGE_ARGUMENT = "pageArgument";
    private String pageArgument;
    private TextView firstPageTitle, secondPageTitle , firstFullName, firstAddress,
            firstPhoneNumber, secondFullName, secondAddress, secondPhoneNumber;
    private ImageView describeImage;
    private Button deletePostButton;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assigned_contribution);
        Intent intent = getIntent();
        pageArgument = intent.getStringExtra(PAGE_ARGUMENT);
        firstPageTitle = findViewById(R.id.firstTitleText);
        firstFullName = findViewById(R.id.firstFullNameText);
        firstAddress = findViewById(R.id.firstAddressText);
        firstPhoneNumber = findViewById(R.id.firstPhoneNumberText);
        secondPageTitle = findViewById(R.id.secondTitleText);
        secondFullName = findViewById(R.id.secondFullNameText);
        secondAddress = findViewById(R.id.secondAddressText);
        secondPhoneNumber = findViewById(R.id.secondPhoneNumberText);
        deletePostButton = findViewById(R.id.deletePostButton);
        describeImage = findViewById(R.id.descriptionImage);
        storage = FirebaseStorage.getInstance();
        setImageByContributionID(contributionHelper, describeImage);
        setContentViewByPageArgument();

    }

    private void setContentViewByPageArgument(){
        if(contributionHelper.getUserName().contains(CurrentUser.getUserName()))
            deletePostButton.setVisibility(View.VISIBLE);

        if(pageArgument.contains(ResultListActivity.PAGE_ARGUMENT_MY_HELP_REQUESTS) || pageArgument.contains(ResultListActivity.PAGE_ARGUMENT_GET_HELP_REQUESTS_BY_SEARCH) )
            showHelpRequestPage();
        else
            showTransportRequestPage();



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
                            try {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                imageView.setImageBitmap(bitmap);
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }



    @SuppressLint("SetTextI18n")
    private void showHelpRequestPage(){
        if(contributionHelper.getAssignedUserName().contains(CurrentUser.getUserName())) {
            firstPageTitle.setText("פרטי התורם:");
            secondPageTitle.setText("תיאור התרומה:");
        }
        else{
            firstPageTitle.setText("פרטי המבקש:");
            secondPageTitle.setText("תיאור הבקשה:");
        }
        firstFullName.setText(contributionHelper.getFirstName() + " " + contributionHelper.getLastName());
        firstPhoneNumber.setText(contributionHelper.getPhoneNumber());
        firstAddress.setText(contributionHelper.getFullAddress());
        secondFullName.setText(contributionHelper.getDescription());
    }


    @SuppressLint("SetTextI18n")
    private void showTransportRequestPage(){
        firstPageTitle.setText("פרטי המעלה:");
        firstFullName.setText(contributionHelper.getFirstName() + " " + contributionHelper.getLastName());
        firstPhoneNumber.setText(contributionHelper.getPhoneNumber());
        firstAddress.setText(contributionHelper.getFullAddress());
        secondPageTitle.setText("פרטי המבקש:");
        if(!contributionHelper.getAssignedFirstName().isEmpty()) {
            secondFullName.setText(contributionHelper.getAssignedFirstName() + " " + contributionHelper.getAssignedLastName());
            secondPhoneNumber.setText(contributionHelper.getAssignedPhoneNumber());
            secondAddress.setText(contributionHelper.getAssignedFullAddress());
        }
        else
            secondFullName.setText("לא הוצnד לאף אדם");

    }


    public void deletePost(View view){

    }

    public static void setContribution(Contribution contribution){
        contributionHelper = contribution;
    }
}