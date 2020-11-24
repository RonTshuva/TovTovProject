package com.example.tovtovproject;

public class UserHelperClass {
    private String dateCreated, userName, password, phoneNumber, firstName, lastName, fullAddress, addressCoordinates;

    public UserHelperClass(String dateCreated, String userName, String password, String phoneNumber, String firstName, String lastName, String fullAddress, String addressCoordinates) {
        this.dateCreated = dateCreated;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullAddress = fullAddress;
        this.addressCoordinates = addressCoordinates;
    }


    public String getAddressCoordinates() {
        return addressCoordinates;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}
