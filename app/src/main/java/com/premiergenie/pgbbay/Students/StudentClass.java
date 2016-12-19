package com.premiergenie.pgbbay.Students;

/**
 * Created by Anirudh on 10/9/2016.
 */
public class StudentClass {

    private String key;
    private int grade;
    private int age;
    private String firstName;
    private String lastName;
    private String parentName;
    private String schoolName;
    private String curriculum;
    private String primaryEmail;
    private String phoneNumber;

    public StudentClass(){}

    public StudentClass(String firstName, String lastName, String parentName, String schoolName, int grade, int age, String primaryEmail, String phoneNumber, String curriculum){
        this.firstName = firstName;
        this.lastName = lastName;
        this.schoolName = schoolName;
        this.parentName = parentName;
        this.grade = grade;
        this.age = age;
        this.primaryEmail = primaryEmail;
        this.phoneNumber = phoneNumber;
        this.curriculum = curriculum;
    }


    public int getGrade(){return grade;}
    public String getFirstName(){return firstName;}
    public String getLastName(){return lastName;}
    public String getParentName(){return parentName;}
    public String getSchoolName(){return schoolName;}
    public String getPhoneNumber(){return phoneNumber;}
    public String getPrimaryEmail(){return primaryEmail;}
    public String getCurriculum(){return curriculum;}
    public int getAge(){return age;}
    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key=key;
    }

}
