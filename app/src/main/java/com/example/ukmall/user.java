package com.example.ukmall;

public class user {
    public String email, name, userName;
    public Double totalSpend, totalSales;
    public Integer totalProduct;

    public user(){
    }

    public user(String email, String name){
        this.email = email;
        this. name = name;
    }

    public user(String email, String name, String userName, Double totalSpend, Double totalSales, Integer totalProduct) {
        this.email = email;
        this.name = name;
        this.userName = userName;
        this.totalSpend = totalSpend;
        this.totalSales = totalSales;
        this.totalProduct = totalProduct;
    }

    public user(String email, String name, Double totalSpend) {
        this.email = email;
        this.name = name;
        this.totalSpend = totalSpend;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(Double totalSpend) {
        this.totalSpend = totalSpend;
    }

    public Double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Double totalSales) {
        this.totalSales = totalSales;
    }

    public Integer getTotalProduct() {
        return totalProduct;
    }

    public void setTotalProduct(Integer totalProduct) {
        this.totalProduct = totalProduct;
    }
}
