package com.example.tovtov;

public class CurrentUser {
    static private String userName, email, phoneNumber, firstName, lastName, fullAddress, addressCoordinates;

    public static void setCurrentUser(String userName, String email, String phoneNumber, String firstName, String lastName, String fullAddress, String addressCoordinates) {
        CurrentUser.userName = userName;
        CurrentUser.email = email;
        CurrentUser.phoneNumber = phoneNumber;
        CurrentUser.firstName = firstName;
        CurrentUser.lastName = lastName;
        CurrentUser.fullAddress = fullAddress;
        CurrentUser.addressCoordinates = addressCoordinates;
    }

    public static String getAddressCoordinates() {
        return addressCoordinates;
    }

    public static void setAddressCoordinates(String addressCoordinates) {
        CurrentUser.addressCoordinates = addressCoordinates;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        CurrentUser.userName = userName;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        CurrentUser.email = email;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static void setPhoneNumber(String phoneNumber) {
        CurrentUser.phoneNumber = phoneNumber;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setFirstName(String firstName) {
        CurrentUser.firstName = firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static void setLastName(String lastName) {
        CurrentUser.lastName = lastName;
    }

    public static String getFullAddress() {
        return fullAddress;
    }

    public static void setFullAddress(String fullAddress) {
        CurrentUser.fullAddress = fullAddress;
    }
}
