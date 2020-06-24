package com.example.infinitycenter;

public class AppointmentDetails {

    private String date ;
    private String symptoms ;
    private String username ;

    public AppointmentDetails(String username , String date , String symptoms) {
        this.username = username ;
        this.date = date;
        this.symptoms = symptoms;
    }

    public String getDate() {
        return date;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public String getUsername() {
        return username;
    }
}
