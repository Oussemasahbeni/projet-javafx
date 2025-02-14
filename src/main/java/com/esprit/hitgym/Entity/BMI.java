package com.esprit.hitgym.Entity;

import com.esprit.hitgym.helpers.CustomDate;

import java.sql.Date;

public class BMI {

    private double bmiValue;
    private String BMIStatus;
    private String BMIDescription;
    private double Weight;
    private double Height;
    private Date RecordedDate;
    private String RecordedMonth;
    private int id;

    // Constructor: Note the parameter order: weight, recordedDate, id, height, bmiValue
    public BMI(double weight, Date recordedDate, int id, double height, double bmiValue) {
        this.Weight = weight;
        this.RecordedDate = recordedDate;
        this.id = id;
        this.Height = height;
        this.bmiValue = bmiValue;

        CustomDate customDate = new CustomDate(recordedDate);
        this.RecordedMonth = customDate.getMonthName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBmiValue() {
        return bmiValue;
    }

    public void setBmiValue(double bmiValue) {
        this.bmiValue = bmiValue;
    }

    public String getBMIStatus() {
        return BMIStatus;
    }

    public void setBMIStatus(String BMIStatus) {
        this.BMIStatus = BMIStatus;
    }

    public String getBMIDescription() {
        return BMIDescription;
    }

    public void setBMIDescription(String BMIDescription) {
        this.BMIDescription = BMIDescription;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        this.Weight = weight;
    }

    public double getHeight() {
        return Height;
    }

    public void setHeight(double height) {
        this.Height = height;
    }

    public Date getRecordedDate() {
        return RecordedDate;
    }

    public void setRecordedDate(Date recordedDate) {
        this.RecordedDate = recordedDate;
    }

    public String getRecordedMonth() {
        return RecordedMonth;
    }

    public void setRecordedMonth(String recordedMonth) {
        this.RecordedMonth = recordedMonth;
    }

}
