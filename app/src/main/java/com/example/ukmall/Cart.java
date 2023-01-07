package com.example.ukmall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Cart extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView cartView;
    RecyclerView.LayoutManager cartlayoutManager;
    CartAdapter cartAdapter;

    Button bt_CheckOut;

    //Ganti dengan data dalam firebase
    int[] arr = {R.drawable.brownies, R.drawable.brownies, R.drawable.brownies, R.drawable.brownies, R.drawable.brownies,
            R.drawable.brownies, R.drawable.brownies, R.drawable.brownies};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        bt_CheckOut=findViewById(R.id.bt_checkout);
        bt_CheckOut.setOnClickListener(this);

        //RecyclerView Cart
        cartView = findViewById(R.id.rv_cart);
        cartlayoutManager = new GridLayoutManager(this, 1);
        cartView.setLayoutManager(cartlayoutManager);
        cartAdapter = new CartAdapter(arr);

        cartView.setAdapter(cartAdapter);
        cartView.setHasFixedSize(true);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //Button checkout, nanti ganti dengan intent.
            case R.id.bt_checkout:
                Toast.makeText(this, "Checkout Button", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}