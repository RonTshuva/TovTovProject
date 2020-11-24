package com.example.tovtov;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class TwoGateActivity extends Activity {
    public static final String PAGE_ARGUMENT = "pageArgument", PAGE_ARGUMENT_GIVER = "two_gate_giver", PAGE_ARGUMENT_GETTER = "two_gate_getter";
    private String pageArgument;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        pageArgument = intent.getStringExtra(PAGE_ARGUMENT);
        setContentViewByPageArgument(pageArgument);

    }
    private void setContentViewByPageArgument(String pageArgument){
        if(pageArgument != null) {
            switch(pageArgument){
                case PAGE_ARGUMENT_GETTER: setContentView(R.layout.menu_taker_two_options); break;
                case PAGE_ARGUMENT_GIVER: setContentView(R.layout.menu_giver_two_options); break;
                default: Toast.makeText(getApplicationContext(), "Something went wrong: Couldn't get pageArgument!", Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(getApplicationContext(), "Something went wrong: Couldn't get pageArgument!", Toast.LENGTH_LONG).show();
    }

    public void searchContribution(View view){
        Intent intent=new Intent(getApplicationContext(),ResultListActivity.class);
        intent.putExtra(ResultListActivity.PAGE_ARGUMENT, ResultListActivity.PAGE_ARGUMENT_GET_CONTRIBUTIONS_BY_SEARCH);
        startActivity(intent);
    }

    public void searchForWhomNeedHelp(View view){
        Intent intent=new Intent(getApplicationContext(),ResultListActivity.class);
        intent.putExtra(ResultListActivity.PAGE_ARGUMENT, ResultListActivity.PAGE_ARGUMENT_GET_HELP_REQUESTS_BY_SEARCH);
        startActivity(intent);
    }

    public void publishContribution(View view){
        Intent intent=new Intent(getApplicationContext(),PostContributionActivity.class);
        intent.putExtra(PostContributionActivity.PAGE_ARGUMENT, PostContributionActivity.PAGE_ARGUMENT_GIVER);
        startActivity(intent);
    }

    public void publishHelpRequest(View view){
        Intent intent=new Intent(getApplicationContext(),PostContributionActivity.class);
        intent.putExtra(PostContributionActivity.PAGE_ARGUMENT, PostContributionActivity.PAGE_ARGUMENT_TAKER);
        startActivity(intent);
    }
}

