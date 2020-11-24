package com.example.tovtovproject;

import android.app.Activity;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ResultListActivity extends Activity {

    public static final String
            PAGE_ARGUMENT = "pageArgument",
            PAGE_ARGUMENT_MY_CONTRIBUTIONS = "myContributions",
            PAGE_ARGUMENT_MY_HELP_REQUESTS = "myHelpRequests",
            PAGE_ARGUMENT_GET_CONTRIBUTIONS_BY_SEARCH = "getContributionsBySearch",
            PAGE_ARGUMENT_GET_TRANSPORTS_BY_SEARCH = "getTransportsBySearch",
            PAGE_ARGUMENT_GET_HELP_REQUESTS_BY_SEARCH = "getHelpRequestsBySearch";



    private final int GET_ALL_CONTRIBUTIONS = 1, GET_MY_CONTRIBUTIONS = 2;
    private SmartTextField searchText;
    private TextView emptyList;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Contribution contribution;
    private String pageArgument;
    private ArrayList<Contribution> resultsFromDatabaseList;
    private ArrayList<Contribution> localContributionSearch;
    private ImageView nextPageB, previousPageB, pageTitleMyHelpRequests,
            pageTitleDeliveryResults, pageTitleMyContributions,
            pageTitleGiverResults, pageTitleNeedHelpResults;
    private  SmartResultButtonField contributionB1, contributionB2, contributionB3, contributionB4;
    private Contribution pContributionB1, pContributionB2, pContributionB3, pContributionB4;
    private int pIndex, pHowManyClicks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_list);

        storage = FirebaseStorage.getInstance();
        emptyList = findViewById(R.id.emptyListText);
        searchText = new SmartTextField( (TextView) findViewById(R.id.searchText), (TextView) findViewById(R.id.searchTextError) , "searchText");
        pageTitleMyHelpRequests = (ImageView) findViewById(R.id.my_help);
        pageTitleDeliveryResults = (ImageView) findViewById(R.id.delivery_results);
        pageTitleMyContributions = (ImageView) findViewById(R.id.my_contributions);
        pageTitleGiverResults = (ImageView) findViewById(R.id.givers_results);
        pageTitleNeedHelpResults = (ImageView) findViewById(R.id.needs_help_results);


        contributionB1 = new SmartResultButtonField(
                (TextView) findViewById(R.id.firstButtonNameText),
                (TextView) findViewById(R.id.firstButtonDescriptionText),
                (ImageView) findViewById(R.id.firstItemButton) ,
                (ImageView) findViewById(R.id.firstButtonImageView)
        );

        contributionB2 = new SmartResultButtonField(
                (TextView) findViewById(R.id.secondButtonNameText),
                (TextView) findViewById(R.id.secondButtonDescriptionText),
                (ImageView) findViewById(R.id.secondItemButton) ,
                (ImageView) findViewById(R.id.secondButtonImageView)
        );

        contributionB3 = new SmartResultButtonField(
                (TextView) findViewById(R.id.thirdButtonNameText),
                (TextView) findViewById(R.id.thirdButtonDescriptionText),
                (ImageView) findViewById(R.id.thirdItemButton) ,
                (ImageView) findViewById(R.id.thirdButtonImageView)
        );

        contributionB4 = new SmartResultButtonField(
                (TextView) findViewById(R.id.forthButtonNameText),
                (TextView) findViewById(R.id.forthButtonDescriptionText),
                (ImageView) findViewById(R.id.forthItemButton) ,
                (ImageView) findViewById(R.id.forthButtonImageView)
        );

        contributionB1.setButtonVisibility(SmartResultButtonField.VIEW_INVISIBLE);
        contributionB2.setButtonVisibility(SmartResultButtonField.VIEW_INVISIBLE);
        contributionB3.setButtonVisibility(SmartResultButtonField.VIEW_INVISIBLE);
        contributionB4.setButtonVisibility(SmartResultButtonField.VIEW_INVISIBLE);

        nextPageB = (ImageView) findViewById(R.id.nextPageButton);
        previousPageB = (ImageView) findViewById(R.id.previousPageButton);

        Intent intent = getIntent();
        pageArgument = intent.getStringExtra(PAGE_ARGUMENT);
        resultsFromDatabaseList = new ArrayList<>();

        if (pageArgument != null) {
            switch (pageArgument) {
                case PAGE_ARGUMENT_MY_CONTRIBUTIONS:
                    createMyContributionsPage();
                    break;
                case PAGE_ARGUMENT_MY_HELP_REQUESTS:
                    createMyHelpRequestsPage();
                    break;
                case PAGE_ARGUMENT_GET_CONTRIBUTIONS_BY_SEARCH:
                    createGetContributionsBySearchPage();
                    break;
                case PAGE_ARGUMENT_GET_TRANSPORTS_BY_SEARCH:
                    createGetTransportsPage();
                    break;
                case PAGE_ARGUMENT_GET_HELP_REQUESTS_BY_SEARCH:
                    createGetHelpRequestsBySearchPage();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Something went wrong: Couldn't get pageArgument!", Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(getApplicationContext(), "Something went wrong: Couldn't get pageArgument!", Toast.LENGTH_SHORT).show();
    }


    private void createGetHelpRequestsBySearchPage(){
        pageTitleNeedHelpResults.setVisibility(View.VISIBLE);
        searchText.getTextLayout().setHint("חפש נזקקים לעזרה");
        getHelpRequestsFromDatabase();
    }
    private void createMyHelpRequestsPage(){
        pageTitleMyHelpRequests.setVisibility(View.VISIBLE);
        searchText.getTextLayout().setHint("חפש בקשות שלך");
        getMyHelpRequestsFromDatabase();

    }

    private void createMyContributionsPage() {
        pageTitleMyContributions.setVisibility(View.VISIBLE);
        searchText.getTextLayout().setHint("חפש בתרומות שלך");
        getMyContributionsFromDatabase();
    }

    private void createGetContributionsBySearchPage() {
        pageTitleGiverResults.setVisibility(View.VISIBLE);
        searchText.getTextLayout().setHint("חפש בתרומות קיימות");
        getContributionsFromDatabase();
    }

    private void createGetTransportsPage(){
        pageTitleGiverResults.setVisibility(View.VISIBLE);
        searchText.getTextLayout().setHint("חפש נזקקים לשליח");
        getTransportationRequestsFromDatabase();
    }


    private void getHelpRequestsFromDatabase() {
        String[] contributionsList = {
                Contribution.CONTRIBUTION_TYPE_HELP
        };
        getContributionsFromDatabase(contributionsList, GET_ALL_CONTRIBUTIONS);
    }

    private void getTransportationRequestsFromDatabase() {
        String[] contributionsList = {
                Contribution.CONTRIBUTION_TYPE_TRANSPORT,
        };
        getContributionsFromDatabase(contributionsList, GET_ALL_CONTRIBUTIONS);
    }

    private void getContributionsFromDatabase() {
        String[] contributionsList = {
                Contribution.CONTRIBUTION_TYPE_SHARE_CONTRIBUTION,
        };
        getContributionsFromDatabase(contributionsList, GET_ALL_CONTRIBUTIONS);
    }

    private void getMyHelpRequestsFromDatabase() {
        String[] contributionsList = {
                Contribution.CONTRIBUTION_TYPE_HELP,
        };
        getContributionsFromDatabase(contributionsList, GET_MY_CONTRIBUTIONS);
    }

    private void getMyContributionsFromDatabase() {
        String[] contributionsList = {
                Contribution.CONTRIBUTION_TYPE_HELP,
                Contribution.CONTRIBUTION_TYPE_SHARE_CONTRIBUTION,
                Contribution.CONTRIBUTION_TYPE_TRANSPORT
        };
        getContributionsFromDatabase(contributionsList, GET_MY_CONTRIBUTIONS);
    }

    private void makeButtons() {

        if( localContributionSearch == null)
            localContributionSearch = resultsFromDatabaseList;

        int howManyButtonsToAssign = localContributionSearch.size();
        if(howManyButtonsToAssign == 0) {
            emptyList.setText("רשימה זו ריקה.");
            emptyList.setVisibility(View.VISIBLE);
        }
        else if(howManyButtonsToAssign > 3) {
            howManyButtonsToAssign = 4;
            emptyList.setText("");
        }
        if(howManyButtonsToAssign > 0)
            emptyList.setVisibility(View.INVISIBLE);

        assignButtons(howManyButtonsToAssign, 0);

        if(localContributionSearch.size() > 4){
            nextPageB.setVisibility(View.VISIBLE);
            previousPageB.setVisibility(View.INVISIBLE);
            pIndex = 0;
            pHowManyClicks = 1;
        }
    }

    public void nextPage(View view){
        pHowManyClicks++;
        pIndex += 4;
        int howManyResults = localContributionSearch.size();
        int maxRight = howManyResults / 4;
        int reminder = howManyResults % 4;
        if(pHowManyClicks <= maxRight){
            assignButtons(4, pIndex);
            if(reminder == 0)
                nextPageB.setVisibility(View.INVISIBLE);
        }
        else{
            assignButtons(reminder, pIndex);
            nextPageB.setVisibility(View.INVISIBLE);
        }

        previousPageB.setVisibility(View.VISIBLE);
        //Toast.makeText(getApplicationContext(), "pIndex " + pIndex + " maxRight " + maxRight + " reminder " + reminder + " clicks " + pHowManyClicks, Toast.LENGTH_SHORT).show();
    }

    public void previousPage(View view){
        pHowManyClicks--;
        pIndex -= 4;
        assignButtons(4, pIndex);
        if (pHowManyClicks == 1) {
            previousPageB.setVisibility(View.INVISIBLE);
        }

        nextPageB.setVisibility(View.VISIBLE);
        //Toast.makeText(getApplicationContext(), "pIndex " + pIndex  + " clicks " + pHowManyClicks, Toast.LENGTH_SHORT).show();
    }

    private void assignButtons(int howManyButtonsToAssign, int fromWhatIndex) {

        contributionB1.setButtonVisibility(SmartResultButtonField.VIEW_INVISIBLE);
        contributionB2.setButtonVisibility(SmartResultButtonField.VIEW_INVISIBLE);
        contributionB3.setButtonVisibility(SmartResultButtonField.VIEW_INVISIBLE);
        contributionB4.setButtonVisibility(SmartResultButtonField.VIEW_INVISIBLE);

        switch(howManyButtonsToAssign)
        {
            case 4:
                pContributionB4 = localContributionSearch.get(fromWhatIndex + 3); // we shouldn't break here
                contributionB4.setNameAndDescription(pContributionB4.getName() , pContributionB4.getDescription());
                contributionB4.setButtonVisibility(SmartResultButtonField.VIEW_VISIBLE);
                setImageByContributionID(pContributionB4, contributionB4.getDescribeImageView());
            case 3:
                pContributionB3 = localContributionSearch.get(fromWhatIndex + 2);
                contributionB3.setNameAndDescription(pContributionB3.getName() , pContributionB3.getDescription());
                contributionB3.setButtonVisibility(SmartResultButtonField.VIEW_VISIBLE);
                setImageByContributionID(pContributionB3, contributionB3.getDescribeImageView());
            case 2:
                pContributionB2 = localContributionSearch.get(fromWhatIndex + 1);
                contributionB2.setNameAndDescription(pContributionB2.getName() , pContributionB2.getDescription());
                contributionB2.setButtonVisibility(SmartResultButtonField.VIEW_VISIBLE);
                setImageByContributionID(pContributionB2, contributionB2.getDescribeImageView());
            case 1:
                pContributionB1 = localContributionSearch.get(fromWhatIndex);
                contributionB1.setNameAndDescription(pContributionB1.getName() , pContributionB1.getDescription());
                contributionB1.setButtonVisibility(SmartResultButtonField.VIEW_VISIBLE);
                setImageByContributionID(pContributionB1, contributionB1.getDescribeImageView());
        }
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


    public void contributionButton1(View view){
        Log.i("button 1:" , " "+ pContributionB1.getContributionID());
        switchToContributionActivity(pContributionB1);
    }

    public void contributionButton2(View view){
        Log.i("button 2:" , " "+ pContributionB2.getContributionID());
        switchToContributionActivity(pContributionB2);
    }

    public void contributionButton3(View view){
        Log.i("button 3:" , " "+ pContributionB3.getContributionID());
        switchToContributionActivity(pContributionB3);
    }

    public void contributionButton4(View view){
        Log.i("button 4:" , " "+ pContributionB4.getContributionID());
        switchToContributionActivity(pContributionB4);
    }

    private void switchToContributionActivity(Contribution contribution){
        if(pageArgument.contains(PAGE_ARGUMENT_GET_CONTRIBUTIONS_BY_SEARCH) && !contribution.getUserName().contains(CurrentUser.getUserName())){
            Intent intent=new Intent(getApplicationContext(), GetContributionActivity.class);
            GetContributionActivity.setContribution(contribution);
            startActivity(intent);
        }else{
            Intent intent=new Intent(getApplicationContext(), AssignedContributionActivity.class);
            intent.putExtra(PAGE_ARGUMENT, pageArgument);
            AssignedContributionActivity.setContribution(contribution);
            startActivity(intent);
        }
    }

    public void searchButton(View view){
        localContributionSearch = new ArrayList<>();
        String wordToSearch = searchText.getTextLayout().getText().toString();
        if (wordToSearch.isEmpty()) {
            searchText.setErrorLayoutText("לא ניתן להשאיר שדה זה ריק");
        }
        else {

            for (Contribution contribution : resultsFromDatabaseList) { // 1 pass of high accuracy search
                if (contribution.getName().contains(wordToSearch)) {
                    localContributionSearch.add(contribution);
                }
            }

            wordToSearch = cleanString(wordToSearch);
            for (Contribution contribution : resultsFromDatabaseList) { // 1 pass of low accuracy search
                String cleanContributeName = cleanString(contribution.getName());
                if (cleanContributeName.contains(wordToSearch) && !findContributionInArray(localContributionSearch, contribution)) {
                    localContributionSearch.add(contribution);
                }
            }
            if(localContributionSearch != null && localContributionSearch.size() == 0)
                 Toast.makeText(getApplicationContext(), "לא נמצאו תוצאות!",Toast.LENGTH_SHORT).show();
            makeButtons();
        }
    }

    private boolean findContributionInArray(ArrayList<Contribution> contributionArrayList, Contribution objectToSearchFor){
        for( Contribution contribution : contributionArrayList){
            if(contribution == objectToSearchFor)
                return true;
        }
        return false;
    }

    private String cleanString(String wordToSearch){
        StringBuilder stringBuilder = new StringBuilder(wordToSearch);
        for (int i = 0; i < stringBuilder.length(); i++) {
            if(stringBuilder.charAt(i) == ' '
                    || (stringBuilder.charAt(i) >= 97 && stringBuilder.charAt(i) <= 122) // lowercases english characters
                    || (stringBuilder.charAt(i) >= 65 &&  stringBuilder.charAt(i) <= 90) // uppercases english characters
                    || (stringBuilder.charAt(i) >= 1488 &&  stringBuilder.charAt(i) <= 1515)) // hebrew characters
            {
                stringBuilder.deleteCharAt(i);
            }

        }
        return stringBuilder.toString();
    }

    private void getContributionsFromDatabase(final String[] contributionsList, final int contributionsToGetOption) {

        database = FirebaseDatabase.getInstance();
        String contributionString;

        for (int i = 0; i < contributionsList.length; i++) {

            contributionString = contributionsList[i];
            databaseReference = database.getReference();

            switch (contributionsToGetOption) {
                case GET_ALL_CONTRIBUTIONS:
                    databaseReference = databaseReference.child("Contributions").child(contributionString);
                    break;
                case GET_MY_CONTRIBUTIONS:
                    databaseReference = databaseReference.child("Contributions").child(contributionString).child(CurrentUser.getUserName());
            }

            final int finalI = i;
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.i("title:", "before for");

                    if (contributionsToGetOption == GET_ALL_CONTRIBUTIONS) {
                        extractAllContributions(snapshot);
                    } else if (contributionsToGetOption == GET_MY_CONTRIBUTIONS) {
                        extractMyContributions(snapshot);
                    }
                    Log.i("title:", "the size is: " + resultsFromDatabaseList.size());
                    if(finalI == contributionsList.length-1) {
                        makeButtons();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


    private void extractAllContributions(DataSnapshot snapshot) {
        Contribution contribution;
        Iterable<DataSnapshot> children = snapshot.getChildren();

        for (DataSnapshot userNameChild : children) {
            for (DataSnapshot contributionChild : userNameChild.getChildren()) {
                contribution = contributionChild.getValue(Contribution.class);
                if (contribution != null && !contribution.isAssigned()) {
                    Log.i("ALL:", "contribution id is: " + contribution.getContributionID());
                    resultsFromDatabaseList.add(contribution);
                } else
                    Log.i("ALL:", "contribution id is: null");
            }
        }
    }


    private void extractMyContributions(DataSnapshot snapshot) {
        Contribution contribution;
        Iterable<DataSnapshot> children = snapshot.getChildren();

        for (DataSnapshot contributionChild : children) {
            contribution = contributionChild.getValue(Contribution.class);
            if (contribution != null) {
                Log.i("MINE:", "contribution id is: " + contribution.getContributionID());
                resultsFromDatabaseList.add(contribution);
            } else
                Log.i("MINE:", "contribution id is: null");
        }
    }
}

