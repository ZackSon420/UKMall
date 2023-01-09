package com.example.ukmall;

public class Order {
    String orderId, paymentMethod, deliveryOption;
    Integer totalPrice;

    public Order(){
    }

    public Order(String orderId, String paymentMethod, String deliveryOption, Integer totalPrice) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.deliveryOption = deliveryOption;
        this.totalPrice = totalPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(String deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }
}

