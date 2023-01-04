package com.example.ukmall;

public class Product {
    String name, description, url, url2;
    Integer price;

    public Product(){
    }

    public Product(String name, Integer price, String description, String url, String url2) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.url = url;
        this.url2 = url2;

    }

    public String getUrl2() { return url2; }

    public void setUrl2(String url2) { this.url2 = url2; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}

