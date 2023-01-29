package com.example.ukmall;

import java.io.Serializable;

public class ProductEdit implements Serializable {
    String productId,productTitle, productDescription, productCategory, productQuantity,url;
    Double productPrice;

    public ProductEdit(){
    }



    public ProductEdit( String productId, String url, String productTitle, String productDescription, String productCategory, String productQuantity, Double productPrice) {
        this.productId = productId;
        this.url = url;
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.productCategory= productCategory;
        this.productQuantity= productQuantity;
        this.productPrice = productPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }
}

