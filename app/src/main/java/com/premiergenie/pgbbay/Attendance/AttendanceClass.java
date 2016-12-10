package com.premiergenie.pgbbay.Attendance;

/**
 * Created by Anirudh on 10/17/2016.
 */

public class AttendanceClass {

    private String key;
    private String date;
    private String studentName;
    private String instructorName;
    private String courseName;

    public AttendanceClass(){}

    public AttendanceClass( String date, String studentName, String instructorName, String courseName){

        this.date = date;
        this.studentName = studentName;
        this.instructorName = instructorName;
        this.courseName = courseName;
    }

    public String getDate(){
        return date;
    }
    public String getStudentName(){
        return studentName;
    }
    public String getInstructorName(){
        return instructorName;
    }
    public String getCourseName(){
        return courseName;
    }
    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key=key;
    }


}
