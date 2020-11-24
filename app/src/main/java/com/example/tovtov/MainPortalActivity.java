package com.example.tovtov;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainPortalActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_portal);
    }

    public void giveContribution(View view){
        Intent intent=new Intent(getApplicationContext(), TwoGateActivity.class);
        intent.putExtra(TwoGateActivity.PAGE_ARGUMENT, TwoGateActivity.PAGE_ARGUMENT_GIVER);
        startActivity(intent);
    }

    public void showMyContributions(View view){
        Intent intent=new Intent(getApplicationContext(),ResultListActivity.class);
        intent.putExtra(ResultListActivity.PAGE_ARGUMENT, ResultListActivity.PAGE_ARGUMENT_MY_CONTRIBUTIONS);
        startActivity(intent);
    }

    public void needsContribution(View view){
        Intent intent=new Intent(getApplicationContext(),TwoGateActivity.class);
        intent.putExtra(TwoGateActivity.PAGE_ARGUMENT, TwoGateActivity.PAGE_ARGUMENT_GETTER);
        startActivity(intent);
    }

    public void changeUserDetails(View view){
        Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
        intent.putExtra(RegisterActivity.PAGE_ARGUMENT, RegisterActivity.PAGE_ARGUMENT_CHANGE_DETAILS);
        startActivity(intent);
    }


    public void myHelpRequests(View view){
        Intent intent=new Intent(getApplicationContext(),ResultListActivity.class);
        intent.putExtra(ResultListActivity.PAGE_ARGUMENT, ResultListActivity.PAGE_ARGUMENT_MY_HELP_REQUESTS);
        startActivity(intent);
    }


    public void transportKindness(View view){
        Intent intent=new Intent(getApplicationContext(),ResultListActivity.class);
        intent.putExtra(ResultListActivity.PAGE_ARGUMENT, ResultListActivity.PAGE_ARGUMENT_GET_TRANSPORTS_BY_SEARCH);
        startActivity(intent);
    }
}
