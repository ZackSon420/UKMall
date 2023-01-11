package com.example.ukmall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Analytics extends AppCompatActivity {

    private RecyclerView productAView;
    RecyclerView.LayoutManager productALayoutManager;
    AnalyticsAdapter analyticsAdapter;
    private TextView TotalProductTv, TotalSalesTV, TotalOrderTV;
    private ArrayList<Order> arrayOrder;
    int[] arr = {R.drawable.brownies, R.drawable.brownies, R.drawable.brownies, R.drawable.brownies, R.drawable.brownies};

    private Integer saleTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        TotalProductTv=findViewById(R.id.tv_total_product);
        TotalSalesTV=findViewById(R.id.tv_total_sales);
        TotalOrderTV=findViewById(R.id.tv_total_order);
        //RecyclerView
        productAView = findViewById(R.id.rv_product_analytics);
        productALayoutManager = new GridLayoutManager(this, 1);
        productAView.setLayoutManager(productALayoutManager);
        analyticsAdapter = new AnalyticsAdapter(arr);

        productAView.setAdapter(analyticsAdapter);
        productAView.setHasFixedSize(true);

        arrayOrder = new ArrayList<Order>();

//      Total Product
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collection = db.collection("product");
        CollectionReference orderCollection = db.collection("order");

        AggregateQuery countQuery = collection.count();
        AggregateQuery countOrderQuery = orderCollection.count();

        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                TotalProductTv.setText("" + snapshot.getCount());

            }
        });

        countOrderQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                TotalOrderTV.setText("" + snapshot.getCount());

            }
        });

//      Total Sales
       saleTotal = totalSales();
       TotalSalesTV.setText(saleTotal.toString());

    }

    private Integer totalSales() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Double totalSales = 0.0;


//        db.collectionGroup("product").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//
//                    for(DocumentChange dc : task.getResult().getDocumentChanges()){
//                        if(dc.getType()==DocumentChange.Type.ADDED){
//                            productArrayList.add(dc.getDocument().toObject(Product.class));
//                        }
//                    }
//                    recyclerViewAdapter.notifyDataSetChanged();
//                }
//            }
//        });

        db.collectionGroup("order").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for(DocumentChange dc : task.getResult().getDocumentChanges()){
                        if(dc.getType()==DocumentChange.Type.ADDED){
                            arrayOrder.add(dc.getDocument().toObject(Order.class));
                        }
                    }
                }
            }
        });

        for (int i = 0; i<arrayOrder.size(); i++){
            totalSales = totalSales + arrayOrder.get(i).getTotalPrice();
        }

        return arrayOrder.size();

    }
}