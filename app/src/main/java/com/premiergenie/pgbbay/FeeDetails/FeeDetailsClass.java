package com.premiergenie.pgbbay.FeeDetails;

import lombok.Data;

/**
 * Created by Anirudh on 10/28/2016.
 */
@Data
public class FeeDetailsClass {

    private String key;
    private String datePaid;
    private String studentName;
    private String courseName;
    private String monthGiven;
    private int    amountPaid;

    public FeeDetailsClass(){}

    public FeeDetailsClass(String date, String studentName, String courseName, int amountPaid){

        this.datePaid = date;
        this.studentName = studentName;
       this.courseName = courseName;
        this.amountPaid = amountPaid;
    }

    public FeeDetailsClass(String date, String studentName, String courseName, String monthGiven, int amountPaid){

        this.datePaid = date;
        this.studentName = studentName;
        this.monthGiven = monthGiven;
        this.courseName = courseName;
        this.amountPaid = amountPaid;
    }
/*
    public String getDatePaid(){
        return datePaid;
    }
    public String getStudentName(){
        return studentName;
    }
    public String getMonthGiven(){
        return monthGiven;
    }
    public String getCourseName(){
        return courseName;
    }
    public int getAmountPaid(){
        return amountPaid;
    }
    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key=key;
    }
*/

}
