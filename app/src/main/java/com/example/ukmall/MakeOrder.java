package com.example.ukmall;

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

import java.io.Serializable;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.ArrayList;
import java.util.List;

public class MakeOrder extends AppCompatActivity implements View.OnClickListener,CartAdapter.CartClickedListeners, Serializable{

    /*private RecyclerView makeOrderView;
    MakeOrderAdapter makeOrderAdapter;
    RecyclerView.LayoutManager makeOrderLayoutManager;
    Button btnMakeOrder;

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

    //Ganti dengan data dalam firebase
//    int[] arr = {R.drawable.brownies, R.drawable.brownies, R.drawable.brownies, R.drawable.brownies, R.drawable.brownies,
//            R.drawable.brownies, R.drawable.brownies, R.drawable.brownies};

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

    }
}