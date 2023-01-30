package com.example.ukmall;

public class user {
    public String email, name;
    public Double totalSpend;

    public user(){
    }

    public user(String email, String name){
        this.email = email;
        this. name = name;
    }

    public user(String email, String name, Double totalSpend) {
        this.email = email;
        this.name = name;
        this.totalSpend = totalSpend;
    }
}
