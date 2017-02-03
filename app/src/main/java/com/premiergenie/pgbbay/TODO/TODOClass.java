package com.premiergenie.pgbbay.TODO;

/**
 * Created by Anirudh on 1/7/2017.
 */

public class TODOClass {

    private String todo;
    private String key;

    public TODOClass(){}

    public TODOClass(String todo){ this.todo = todo; }

    public String getTodo(){ return todo; }

    public void setTodo(String s){ todo = s; }

    public String getKey() { return key; }

    public void setKey(String s) { key = s; }

}
