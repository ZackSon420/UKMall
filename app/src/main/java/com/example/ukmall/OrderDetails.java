package com.example.ukmall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OrderDetails extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvOrderDetails;
    RecyclerView.LayoutManager layoutManager;
    OrderDetailsAdapter orderDetailsAdapter;
    ArrayList<Item> itemArrayList;
    FirebaseFirestore db;
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

        //recyclerView order details
        rvOrderDetails = findViewById(R.id.rv_orderDetails);
        layoutManager=new GridLayoutManager(this, 1);
        rvOrderDetails.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();
        itemArrayList = new ArrayList<Item>();
        orderDetailsAdapter = new OrderDetailsAdapter(OrderDetails.this, itemArrayList);

        rvOrderDetails.setAdapter(orderDetailsAdapter);
        EventChangeListener();
        rvOrderDetails.setHasFixedSize(true);

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

    private void EventChangeListener() {
        db.collectionGroup("ordered").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for(DocumentChange dc : task.getResult().getDocumentChanges()){
                        if(dc.getType()==DocumentChange.Type.ADDED){
                            itemArrayList.add(dc.getDocument().toObject(Item.class));
                        }
                    }
                    orderDetailsAdapter.notifyDataSetChanged();
                }
            }
        });
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