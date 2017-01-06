package com.premiergenie.pgbbay.Expenses;

/**
 * Created by Anirudh on 10/28/2016.
 */

public class ExpenseDetailsClass {

    private String key;
    private String datePaid;
    private String expenseDetail;
    private String expenseType;
    private int    amountPaid;

    public ExpenseDetailsClass(){}

    public ExpenseDetailsClass(String date, String expenseDetail, String expenseType, int amountPaid){

        this.datePaid = date;
        this.expenseDetail = expenseDetail;
       this.expenseType = expenseType;
        this.amountPaid = amountPaid;
    }

    public ExpenseDetailsClass(String date, String expenseDetail, int amountPaid){

        this.datePaid = date;
        this.expenseDetail = expenseDetail;
        this.amountPaid = amountPaid;
    }


    public String getDatePaid(){
        return datePaid;
    }

    public String getexpenseDetail(){
        return expenseDetail;
    }

    public String getexpenseType(){
        return expenseType;
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

}
