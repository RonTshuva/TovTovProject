package com.example.tovtovproject;

public class Contribution {

    public final static String CONTRIBUTION_TYPE_SHARE_CONTRIBUTION = "shareContributions", CONTRIBUTION_TYPE_HELP = "Help", CONTRIBUTION_TYPE_TRANSPORT = "Transport";
    private String name, description, userName, firstName, lastName, phoneNumber, fullAddress, addressCoordinates,
            assignedUserName, assignedFirstName, assignedLastName, assignedPhoneNumber, assignedFullAddress, assignedAddressCoordinates, contributionType ,contributionID;
    private boolean assigned, hasImage;

    public Contribution(){

    }



    public Contribution(String name, String description, String userName, String firstName, String lastName, String phoneNumber,
                        String fullAddress, String contributionType, String contributionID, boolean assigned, boolean hasImage) {
        this.name = name;
        this.description = description;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.fullAddress = fullAddress;
        this.addressCoordinates = addressCoordinates;
        this.assignedUserName = new String("");
        this.assignedFirstName = new String("");
        this.assignedLastName = new String("");
        this.assignedPhoneNumber = new String("");
        this.assignedFullAddress = new String("");
        this.assignedAddressCoordinates = new String("");
        this.hasImage = hasImage;
        this.contributionType = contributionType;
        this.contributionID = contributionID;
        this.assigned = false;
    }


    public String getAddressCoordinates() {
        return addressCoordinates;
    }

    public String getAssignedAddressCoordinates() {
        return assignedAddressCoordinates;
    }

    public String getAssignedFirstName() {
        return assignedFirstName;
    }

    public String getAssignedFullAddress() {
        return assignedFullAddress;
    }

    public String getAssignedLastName() {
        return assignedLastName;
    }

    public String getAssignedPhoneNumber() {
        return assignedPhoneNumber;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public void assignedUser(String assignedUserName, String assignedFirstName, String assignedLastName, String assignedPhoneNumber, String assignedFullAddress, String assignedAddressCoordinates){
        this.assignedUserName = assignedUserName;
        this.assignedFirstName = assignedFirstName;
        this.assignedLastName = assignedLastName;
        this.assignedPhoneNumber = assignedPhoneNumber;
        this.assignedFullAddress = assignedFullAddress;
        this.assignedAddressCoordinates = assignedAddressCoordinates;
        this.assigned = true;
    }

    public void unAssignUserName() {
        this.assignedUserName = new String("");
        this.assignedFirstName = new String("");
        this.assignedLastName = new String("");
        this.assignedPhoneNumber = new String("");
        this.assignedFullAddress = new String("");
        this.assigned = true;
    }

    public String getAssignedUserName() {
        return assignedUserName;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setContributionType(String contributionType) {
        this.contributionType = contributionType;
    }

    public String getContributionType() {
        return contributionType;
    }

    public String getContributionID() {
        return contributionID;
    }

    public void setContributionID(String contributionID) {
        this.contributionID = contributionID;
    }
}
