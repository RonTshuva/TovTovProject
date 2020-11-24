package com.example.tovtov;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SmartResultButtonField {

    public static int VIEW_INVISIBLE = 1, VIEW_VISIBLE = 0;
    TextView nameText;
    TextView descriptionText;
    ImageView buttonImageView;
    ImageView describeImageView;

    public SmartResultButtonField(TextView nameText, TextView descriptionText, ImageView buttonImageView, ImageView describeImageView) {
        this.nameText = nameText;
        this.descriptionText = descriptionText;
        this.buttonImageView = buttonImageView;
        this.describeImageView = describeImageView;
    }

    public ImageView getDescribeImageView() {
        return describeImageView;
    }

    public void setNameText(String text){
        nameText.setText(text);
    }

    public void setDescriptionText(String text){
        descriptionText.setText(text);
    }

    public void setNameAndDescription(String nameTextString, String descriptionTextString){
        nameText.setText(nameTextString);
        descriptionText.setText(descriptionTextString);
    }

    public void setButtonVisibility(int visibility){
        if(visibility == 1) {
            nameText.setVisibility(View.INVISIBLE);
            descriptionText.setVisibility(View.INVISIBLE);
            buttonImageView.setVisibility(View.INVISIBLE);
            describeImageView.setVisibility(View.INVISIBLE);
        }
        else if (visibility == 0){
            nameText.setVisibility(View.VISIBLE);
            descriptionText.setVisibility(View.VISIBLE);
            buttonImageView.setVisibility(View.VISIBLE);
            describeImageView.setVisibility(View.VISIBLE);
        }
    }



}

