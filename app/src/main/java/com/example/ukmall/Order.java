package com.example.ukmall;

public class Order {
    String orderId, paymentMethod, deliveryOption;
    Double totalPrice;

    public Order(){
    }

    public Order(String orderId, String paymentMethod, String deliveryOption, Double totalPrice) {
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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}

