package com.example.ukmall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Analytics extends AppCompatActivity {

    private RecyclerView productAView;
    RecyclerView.LayoutManager productALayoutManager;
    AnalyticsAdapter analyticsAdapter;
    int[] arr = {R.drawable.brownies, R.drawable.brownies, R.drawable.brownies, R.drawable.brownies, R.drawable.brownies};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        //RecyclerView
        productAView = findViewById(R.id.rv_product_analytics);
        productALayoutManager = new GridLayoutManager(this, 1);
        productAView.setLayoutManager(productALayoutManager);
        analyticsAdapter = new AnalyticsAdapter(arr);

        productAView.setAdapter(analyticsAdapter);
        productAView.setHasFixedSize(true);
    }
}