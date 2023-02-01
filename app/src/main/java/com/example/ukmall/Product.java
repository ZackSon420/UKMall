package com.example.ukmall;

public class Product {
    String productId,productTitle, productDescription, url, url2, productStore, productCategory;
    Double originalPrice, totalSale;
    Integer productQuantity, bought;

    public Product(){
    }

    public Product(String productId, String productTitle, String productDescription, String url, String url2, String productStore, String productCategory, Double originalPrice, Double totalSale, Integer productQuantity, Integer bought) {
        this.productId = productId;
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.url = url;
        this.url2 = url2;
        this.productStore = productStore;
        this.productCategory = productCategory;
        this.originalPrice = originalPrice;
        this.totalSale = totalSale;
        this.productQuantity = productQuantity;
        this.bought = bought;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
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

    public Integer getBought() {
        return bought;
    }

    public void setBought(Integer bought) {
        this.bought = bought;
    }

    public Double getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(Double totalSale) {
        this.totalSale = totalSale;
    }
}

