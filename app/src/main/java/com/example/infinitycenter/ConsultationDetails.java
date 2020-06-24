package com.example.infinitycenter;

public class ConsultationDetails {

    private String date;
    private String medicine ;
    private String instructions ;

    public ConsultationDetails(String date , String medicine , String instructions) {
        this.date = date ;
        this.medicine = medicine;
        this.instructions = instructions;
    }

    public String getDate() {
        return date;
    }

    public String getMedicine() {
        return medicine;
    }

    public String getInstructions() {
        return instructions;
    }
}
