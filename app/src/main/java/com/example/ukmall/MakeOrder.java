package com.example.ukmall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ukmall.utils.model.Item;
import com.example.ukmall.viewmodel.CartViewModel;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import java.io.Serializable;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.ArrayList;
import java.util.List;

public class MakeOrder extends AppCompatActivity implements View.OnClickListener,CartAdapter.CartClickedListeners, Serializable{

    /*private RecyclerView makeOrderView;
    MakeOrderAdapter makeOrderAdapter;
    RecyclerView.LayoutManager makeOrderLayoutManager;
    Button btnMakeOrder;

    private TextView tvTotalPrice;
    private Button btnMakeOrder;
    private Spinner paymentMethod, deliveryOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeorder);

        btnMakeOrder = findViewById(R.id.btn_make_order);

        //recyclerView products
        makeOrderView = findViewById(R.id.rv_makeorder);
        //makeOrderList = new ArrayList<Item>();
        //makeOrderView.setAdapter(MakeOrderAdapter);
        makeOrderLayoutManager = new GridLayoutManager(MakeOrder.this, 1);
        makeOrderView.setLayoutManager(makeOrderLayoutManager);

        List<Item> allSelectedProduct = getSelectedProduct();
        MakeOrderAdapter makeOrderAdapter = new MakeOrderAdapter(MakeOrder.this, allSelectedProduct);
        makeOrderView.setAdapter(makeOrderAdapter);
    }

    private List<Item> getSelectedProduct() {
        List<Item> allSelectedProduct = new ArrayList<Item>();

        /*Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Bundle");
        ArrayList<Item> object = (ArrayList<Item>) bundle.getSerializable("SelectedProduct");

        allSelectedProduct.addAll(object);

        return allSelectedProduct;
    }*/

    //COPY FROM CART

    private RecyclerView cartView;
    RecyclerView.LayoutManager cartlayoutManager;
    MakeOrderAdapter cartAdapter;
    private CartViewModel cartViewModel;
    private TextView totalCartPriceTV;
    public List<Item> selectedProductList;

    Button btnMakeOrder;

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

        cartViewModel.getAllCartItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> productCarts) {
                double price = 0;
                cartAdapter.setItemCartList(productCarts);
                for (int i=0;i<productCarts.size();i++){
                    price = price + productCarts.get(i).getTotalItemPrice();
                }
                //selectedProductList.addAll(productCarts);
                totalCartPriceTV.setText(String.valueOf(price));

            }

        });
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
        hashMap.put("orderId", "ORD"+timestamp);
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