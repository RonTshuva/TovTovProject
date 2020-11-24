package com.example.tovtov;


import android.widget.TextView;

public class SmartTextField {

    String fieldName;
    TextView textLayout;
    TextView errorLayout;

    public SmartTextField(TextView textLayout, TextView errorLayout, String fieldName ) {
        this.textLayout = textLayout;
        this.errorLayout = errorLayout;
        this.fieldName = fieldName;
    }


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public TextView getTextLayout() {
        return textLayout;
    }

    public TextView getErrorLayout() {
        return errorLayout;
    }

    public void setErrorLayoutText(String errorMessage) {
        this.errorLayout.setText(errorMessage);
    }

    public String toString(){
        return textLayout.getHint().toString();
    }
}