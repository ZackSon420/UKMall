package com.example.ukmall;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OrderDetails extends AppCompatActivity implements View.OnClickListener {

    TextView tvOrderId, tvOrderDate, tvCustName, tvPhoneNumber, tvEmail, tvPayment, tvDelivery, tvTotalPrice;
    Button btnAccept, btnCancel;
    ImageView ivCust;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        tvOrderId = findViewById(R.id.tv_orderId);
        tvOrderDate = findViewById(R.id.tv_order_date);
        tvCustName = findViewById(R.id.tv_custName);
        tvPhoneNumber = findViewById(R.id.tv_custPhone);
        tvEmail = findViewById(R.id.tv_custEmail);
        tvPayment = findViewById(R.id.tv_payment_method);
        tvDelivery = findViewById(R.id.tv_delivery_method);
        tvTotalPrice = findViewById(R.id.tv_totalpriceOrder);
        ivCust = findViewById(R.id.img_custOrder);

        btnAccept = findViewById(R.id.btn_accept);
        btnCancel = findViewById(R.id.btn_cancel);

        btnAccept.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        Intent intent = getIntent();
        tvOrderId.setText(intent.getStringExtra("orderId"));
        tvPayment.setText(intent.getStringExtra("payment"));
        tvDelivery.setText(intent.getStringExtra("delivery"));
        tvTotalPrice.setText("RM" + intent.getStringExtra("totalprice"));
        /*tvProductDesc.setText(intent.getStringExtra("productDesc"));
        tvProductPrice.setText(intent.getStringExtra("productPrice"));
        tvStoreName.setText(intent.getStringExtra("store"));
        img = intent.getStringExtra("productImage");
        img2 = intent.getStringExtra("productImage2");

         */

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_accept:
                Toast.makeText(this, "Order Accepted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,Homepage.class));
                break;
            case R.id.btn_cancel:
                startActivity(new Intent(this, ManageOrder.class));
                break;
        }
    }
}