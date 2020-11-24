package com.example.tovtov;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;


public class PostContributionActivity extends Activity {

    public static final String PAGE_ARGUMENT = "pageArgument", PAGE_ARGUMENT_GIVER = "shareContributions", PAGE_ARGUMENT_TAKER = "Help";

    private String pageArgument;
    private SmartTextField contributionShareName;
    private SmartTextField contributionShareDescription;
    private Button shareContributionButton;
    private ImageView contributionShareImage;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference contributionReference;
    private Contribution contributionShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        pageArgument = intent.getStringExtra(PAGE_ARGUMENT);
        setContentViewByPageArgument(pageArgument);

        contributionShareName = new SmartTextField( (TextView) findViewById(R.id.contributionShareNameText), (TextView)findViewById(R.id.contributionShareNameError) , "contributionShareName");
        contributionShareDescription = new SmartTextField( (TextView) findViewById(R.id.contributionShareDescriptionText), (TextView)findViewById(R.id.contributionShareDescriptionError) , "contributionShareDescription");
        contributionShareImage = findViewById(R.id.contributionShareImage);
    }

    private void setContentViewByPageArgument(String pageArgument){
        if(pageArgument != null) {
            switch(pageArgument){
                case PAGE_ARGUMENT_TAKER: setContentView(R.layout.make_post_taker); break;
                case PAGE_ARGUMENT_GIVER: setContentView(R.layout.make_post_giver); break;
                default: Toast.makeText(getApplicationContext(), "Something went wrong: Couldn't get pageArgument!", Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(getApplicationContext(), "Something went wrong: Couldn't get pageArgument!", Toast.LENGTH_LONG).show();
    }

    public void choosePicture(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode ==RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            contributionShareImage.setImageURI(imageUri);
        }
    }

    public void uploadContribution(View view){
        if(validInput()){

            String contributionID = UUID.randomUUID().toString(),
                    contributionName = contributionShareName.getTextLayout().getText().toString(),
                    contributionDescription = contributionShareDescription.getTextLayout().getText().toString();
            contributionShare = new Contribution(contributionName, contributionDescription, CurrentUser.getUserName(), CurrentUser.getFirstName(), CurrentUser.getLastName() ,
                    CurrentUser.getPhoneNumber(), CurrentUser.getFullAddress(), pageArgument, contributionID , false , imageUri != null);

            addContributionShareToDatabase(contributionShare);
            if(imageUri != null)
                uploadPicture(contributionID); // upload the picture and on success switch to "ItemsActivity"
            else
                onUploadSuccess();

        }
    }

    private void addContributionShareToDatabase(Contribution contribution){
        database = FirebaseDatabase.getInstance();

        contributionReference = database.getReference("Contributions");
        contributionReference.
                child(contribution.getContributionType()).
                child(contribution.getUserName()).
                child(contribution.getContributionID()).setValue(contribution);
    }

    private boolean validInput(){
        boolean validNameFlag = !contributionShareName.getTextLayout().getText().toString().isEmpty() ,
                validDescriptionFlag = !contributionShareDescription.getTextLayout().getText().toString().isEmpty(),
                validItemImage = imageUri != null;
        contributionShareName.setErrorLayoutText(validNameFlag ? "" : "שדה זה לא יכול להשאר ריק");
        contributionShareDescription.setErrorLayoutText(validDescriptionFlag ? "" : "שדה זה לא יכול להשאר ריק");
        return validNameFlag && validDescriptionFlag;
    }

    private void uploadPicture(String itemID) {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        StorageReference riversRef = storageReference.child("images/" + itemID);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        onUploadSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "העלאה נכשלה!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        pd.setMessage("Progress: " + (int) progressPercent + "%");
                    }
                });
    }

    private void onUploadSuccess(){
        if(pageArgument.contains(PAGE_ARGUMENT_GIVER))
            showMyContributions();
        else if(pageArgument.contains(PAGE_ARGUMENT_TAKER))
            showMyHelpRequests();
        else
            Toast.makeText(getApplicationContext(), "something went wrong onUploadSuccess", Toast.LENGTH_LONG).show();
    }

    private void showMyContributions(){
        Toast.makeText(getApplicationContext(), "תרומה נקלטה בהצלחה!", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getApplicationContext() , ResultListActivity.class);
        intent.putExtra(ResultListActivity.PAGE_ARGUMENT, ResultListActivity.PAGE_ARGUMENT_MY_CONTRIBUTIONS);
        startActivity(intent);
        finish();
    }

    private void showMyHelpRequests(){
        Toast.makeText(getApplicationContext(), "בקשה נקלטה בהצלחה!", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getApplicationContext() , ResultListActivity.class);
        intent.putExtra(ResultListActivity.PAGE_ARGUMENT, ResultListActivity.PAGE_ARGUMENT_MY_HELP_REQUESTS);
        startActivity(intent);
        finish();
    }
}
