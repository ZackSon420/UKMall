package com.example.ukmall;

public class Product {
    String productTitle, productDescription, url, url2, productStore;
    Double originalPrice;

    public Product(){
    }

    public Product(String productTitle, String productDescription, String url, String url2, String productStore,Double originalPrice) {
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.url = url;
        this.url2 = url2;
        this.productStore = productStore;
        this.originalPrice = originalPrice;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getProductStore() {
        return productStore;
    }

    public void setProductStore(String productStore) {
        this.productStore = productStore;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }
}

