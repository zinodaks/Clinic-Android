package com.example.infinitycenter;

public class User {
    private String username , fName , lName ,  dateOfBirth ;

    public User(String username , String fName , String lName ,  String dateOfBirth) {
        this.username = username ;
        this.fName = fName ;
        this.lName = lName ;
        this.dateOfBirth = dateOfBirth;
    }

    public User(String username) {
        this.username = username ;
    }

    public String getUsername() {
        return username;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }
}
