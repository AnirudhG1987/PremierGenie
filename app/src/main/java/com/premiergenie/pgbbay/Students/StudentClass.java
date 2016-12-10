package com.premiergenie.pgbbay.Students;

/**
 * Created by Anirudh on 10/9/2016.
 */
public class StudentClass {

    private String key;
    private int grade;
    private int age;
    private String firstName;
    private String lasttName;
    private String parentName;
    private String schoolName;
    private String primaryEmail;
    private String phoneNumber;

    public StudentClass(){}

    public StudentClass(String firstName, String lastName, String parentName, String schoolName, int grade, int age, String primaryEmail, String phoneNumber){
        this.firstName = firstName;
        this.lasttName = lastName;
        this.schoolName = schoolName;
        this.parentName = parentName;
        this.grade = grade;
        this.age = age;
        this.primaryEmail = primaryEmail;
        this.phoneNumber = phoneNumber;
    }


    public int getGrade(){return grade;}
    public String getFirstName(){return firstName;}
    public String getLastName(){return lasttName;}
    public String getParentName(){return parentName;}
    public String getSchoolName(){return schoolName;}
    public String getPhoneNumber(){return phoneNumber;}
    public String getPrimaryEmail(){return primaryEmail;}
    public int getAge(){return age;}
    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key=key;
    }

}
