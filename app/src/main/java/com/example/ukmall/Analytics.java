package com.example.ukmall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Analytics extends AppCompatActivity {

    private RecyclerView productAView;
    RecyclerView.LayoutManager productALayoutManager;
    AnalyticsAdapter analyticsAdapter;
    private TextView TotalProductTv;
    int[] arr = {R.drawable.brownies, R.drawable.brownies, R.drawable.brownies, R.drawable.brownies, R.drawable.brownies};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        TotalProductTv=findViewById(R.id.tv_total_product);
        //RecyclerView
        productAView = findViewById(R.id.rv_product_analytics);
        productALayoutManager = new GridLayoutManager(this, 1);
        productAView.setLayoutManager(productALayoutManager);
        analyticsAdapter = new AnalyticsAdapter(arr);

        productAView.setAdapter(analyticsAdapter);
        productAView.setHasFixedSize(true);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collection = db.collection("product");
        AggregateQuery countQuery = collection.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                TotalProductTv.setText("Count: " + snapshot.getCount());

            }
        });

    }
}