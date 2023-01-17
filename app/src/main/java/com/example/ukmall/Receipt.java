package com.example.ukmall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ukmall.utils.model.Item;
import com.example.ukmall.viewmodel.CartViewModel;

import java.util.List;

public class Receipt extends AppCompatActivity {

    private RecyclerView cartView;
    private CartViewModel cartViewModel;
    RecyclerView.LayoutManager cartLayoutManager;
    MakeOrderAdapter cartAdapter;


//    private RecyclerView receiptview;
//    RecyclerView.LayoutManager receiptLayoutManager;
//    ReceiptAdapter receiptAdapter;
//    CartViewModel cartViewModel;
//    public List<Item> selectedProductList;
    TextView priceBought, TVBigPrice;

//
//    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        priceBought = findViewById(R.id.tv_pricebought);
        TVBigPrice = findViewById(R.id.tv_bigprice);

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        cartView = findViewById(R.id.rv_itembought);
        cartLayoutManager = new GridLayoutManager(this, 1);
        cartView.setLayoutManager(cartLayoutManager);
        cartAdapter = new MakeOrderAdapter(this);


        cartView.setAdapter(cartAdapter);
        cartView.setHasFixedSize(true);

        cartViewModel.getAllCartItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                cartAdapter.setItemCartList(items);
            }
        });

        Intent intent = getIntent();
        TVBigPrice.setText(String.valueOf(intent.getDoubleExtra("totalPrice", 0)));
        priceBought.setText(String.valueOf(intent.getDoubleExtra("totalPrice", 0)));

    }
}