package com.example.javatodo.model;

import android.content.Context;
import android.widget.Toast;

public class TodoModel {
    private int id;
    private String text;
    private String isCheck;


    public TodoModel() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String check) {
        this.isCheck = check;

    }
}
