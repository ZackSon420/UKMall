package com.example.ukmall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MakeOrder extends AppCompatActivity {

    private TextView tvTotalPrice;
    private Button btnMakeOrder;
    private Spinner paymentMethod, deliveryOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeorder);

        tvTotalPrice = findViewById(R.id.tv_totalprice);
        btnMakeOrder = findViewById(R.id.BtnMakeorder);
        paymentMethod = findViewById(R.id.spinner_paymentMethod);
        deliveryOption = findViewById(R.id.spinner_deliveryOption);

        btnMakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeOrder();
            }
        });
    }

    private String paymentMethodStr,deliveryOptionStr;
    private Double totalPrice;
    private boolean orderStatus;
    private void makeOrder() {

        totalPrice = Double.valueOf(tvTotalPrice.getText().toString());
        orderStatus = false; // false = seller prepare the order, true = order has complete
        paymentMethodStr = paymentMethod.getSelectedItem().toString();
        deliveryOptionStr = deliveryOption.getSelectedItem().toString();
        addOrder();

    }

    private void addOrder() {
        String timestamp = "" + System.currentTimeMillis();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orderId", ""+timestamp);
        hashMap.put("orderStatus", orderStatus);
        hashMap.put("totalPrice", totalPrice);
        hashMap.put("paymentMethod", paymentMethodStr);
        hashMap.put("deliveryOption", deliveryOptionStr);

        db.collection("order").add(hashMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(MakeOrder.this, "Order are successful! Please wait for seller to prepare your order", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MakeOrder.this, "Order failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}