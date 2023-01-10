package com.example.ukmall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ukmall.repository.CartRepo;
import com.example.ukmall.utils.model.Item;
import com.example.ukmall.viewmodel.CartViewModel;

import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;

import java.io.Serializable;
import java.util.List;

public class MakeOrder extends AppCompatActivity implements View.OnClickListener,CartAdapter.CartClickedListeners, Serializable{

    private TextView tvTotalPrice;
    private Button btnMakeOrder;
    private Spinner paymentMethod, deliveryOption;

    private RecyclerView cartView;
    RecyclerView.LayoutManager cartlayoutManager;
    MakeOrderAdapter cartAdapter;
    private CartViewModel cartViewModel;
    private TextView totalCartPriceTV;
    public List<Item> selectedProductList;
    private ArrayList<Object> arrayOrder = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeorder);

        totalCartPriceTV=findViewById(R.id.tv_totalprice);
        btnMakeOrder=findViewById(R.id.btn_make_order);
        btnMakeOrder.setOnClickListener(this);
        //CartViewModel
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        //RecyclerView Cart
        cartView = findViewById(R.id.rv_makeorder);
        cartlayoutManager = new GridLayoutManager(this, 1);
        cartView.setLayoutManager(cartlayoutManager);
        cartAdapter = new MakeOrderAdapter(this);

        cartView.setAdapter(cartAdapter);
        cartView.setHasFixedSize(true);

        tvTotalPrice = findViewById(R.id.tv_totalprice);
        btnMakeOrder = findViewById(R.id.btn_make_order);
        paymentMethod = findViewById(R.id.spinner_paymentMethod);
        deliveryOption = findViewById(R.id.spinner_deliveryOption);

        btnMakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeOrder();
            }
        });

        cartViewModel.getAllCartItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> productCarts) {
                double price = 0;

                cartAdapter.setItemCartList(productCarts);
                for (int i=0;i<productCarts.size();i++){
                    String itemName;
                    Double itemPrice;
                    Integer quantity;
                    price = price + productCarts.get(i).getTotalItemPrice();

                    itemName = productCarts.get(i).getItemName();
                    itemPrice = productCarts.get(i).getItemPrice();
                    quantity = productCarts.get(i).getQuantity();

                    HashMap<String, Object> prodhashMap = new HashMap<>();
                    prodhashMap.put("itemName", itemName);
                    prodhashMap.put("itemPrice", itemPrice);
                    prodhashMap.put("quantity", quantity);
                    arrayOrder.add(prodhashMap);
                }
                //selectedProductList.addAll(productCarts);
                totalCartPriceTV.setText(String.valueOf(price));

            }

        });
    }


    private String paymentMethodStr,deliveryOptionStr, orderid;
    private Double totalPrice;
    private boolean orderStatus;
    private void makeOrder() {

        String timestamp = "" + System.currentTimeMillis();
        totalPrice = Double.valueOf(tvTotalPrice.getText().toString());
        orderStatus = false; // false = seller prepare the order, true = order has complete
        paymentMethodStr = paymentMethod.getSelectedItem().toString();
        deliveryOptionStr = deliveryOption.getSelectedItem().toString();
        orderid = "ORD" + timestamp;
        addOrder();

    }

    private void addOrder() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference orderRef = db.collection("order");
        Query docRef = orderRef.whereEqualTo("orderId", orderid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orderId", orderid);
        hashMap.put("orderStatus", orderStatus);
        hashMap.put("totalPrice", totalPrice);
        hashMap.put("paymentMethod", paymentMethodStr);
        hashMap.put("deliveryOption", deliveryOptionStr);

        db.collection("order").document(orderid).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MakeOrder.this, "Order are successful! Please wait for seller to prepare your order", Toast.LENGTH_SHORT).show();
            }
        });


        for(int i = 0; i<arrayOrder.size();i++){

            db.collection("order").document(orderid).collection("ordered").add(arrayOrder.get(i)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(MakeOrder.this, "Sub document successful", Toast.LENGTH_SHORT).show();

                    cartViewModel.deleteAllCartItems();

                    Intent intent = new Intent(MakeOrder.this, Homepage.class);
                    startActivity(intent);
                }
            });

        }


    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDeleteClicked(Item item) {

    }

    @Override
    public void onPlusClicked(Item item) {

    }

    @Override
    public void onMinusClicked(Item item) {

    }
}
