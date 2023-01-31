package com.example.ukmall;

import static android.content.ContentValues.TAG;

import static com.google.firebase.firestore.Query.Direction.ASCENDING;
import static com.google.firebase.firestore.Query.Direction.DESCENDING;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Analytics extends AppCompatActivity {

    private RecyclerView productAView;
    RecyclerView.LayoutManager productALayoutManager;
    AnalyticsAdapter analyticsAdapter;
    private TextView TotalProductTv, TotalSalesTV, TotalOrderTV, TotalSpendTV;
    ArrayList<Product> productList;
//    int[] arr = {R.drawable.brownies, R.drawable.brownies, R.drawable.brownies, R.drawable.brownies, R.drawable.brownies};
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    private Double saleTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        mAuth = FirebaseAuth.getInstance();

        TotalProductTv=findViewById(R.id.tv_total_product);
        TotalSalesTV=findViewById(R.id.tv_total_sales);
        TotalOrderTV=findViewById(R.id.tv_total_order);
        TotalSpendTV=findViewById(R.id.tv_total_spend);
        //RecyclerView
        productAView = findViewById(R.id.rv_product_analytics);
        productALayoutManager = new GridLayoutManager(this, 1);
        productAView.setLayoutManager(productALayoutManager);

        productList=new ArrayList<>();
        getSellerProduct();
        analyticsAdapter = new AnalyticsAdapter(productList);

        productAView.setAdapter(analyticsAdapter);
        productAView.setHasFixedSize(true);


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

//      Total Spend

        DocumentReference userRef = db.collection("user").document(mAuth.getCurrentUser().getUid());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Object spendTotal = documentSnapshot.get("totalSpend");
                    TotalSpendTV.setText("" + spendTotal);
                }
            }
        });

    }

    public void getSellerProduct(){
        productList.clear();

        db = FirebaseFirestore.getInstance();

//        db.collection("product").whereEqualTo("userId", mAuth.getCurrentUser().getUid()).orderBy("bought", DESCENDING).get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for(DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()){
//                            switch (dc.getType()) {
//                            case ADDED:
//                            productList.add(dc.getDocument().toObject(Product.class));
//                            analyticsAdapter.notifyDataSetChanged();
//                            break;
//                            }
//                        }
//                    }
//                });



        db.collection("product").whereEqualTo("userId", mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("Error", e.getMessage());
                }
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            productList.add(dc.getDocument().toObject(Product.class));
                            analyticsAdapter.notifyDataSetChanged();
                            break;
                    }
                }
            }
        });
    }



//      Total Sales
//
//        orderCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for(QueryDocumentSnapshot document : task.getResult()){
////                        Double totalPrice = document.getDouble("totalPrice");
////                        saleTotal += totalPrice;
//                        Order order = document.toObject(Order.class);
//                        arrayOrder.add(order);
//                    }
////                    TotalSalesTV.setText("" +arrayOrder.size());
//                }else{
//                    Log.d("debug", "Error");
//                }
//
//                for (int i = 1; i<arrayOrder.size(); i++){
//                //    saleTotal = saleTotal + arrayOrder.get(i).getTotalPrice();
//                }
//
//              //  TotalSalesTV.setText(String.valueOf(saleTotal));
//            }
//        });
//
//
//
//    }

//    private Integer totalSales() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference orderCollection = db.collection("order");
//        Double totalSales = 0.0;
//
//        orderCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for(QueryDocumentSnapshot document : task.getResult()){
//                        Order order = document.toObject(Order.class);
//                        arrayOrder.add(order);
//                    }
//                }else{
//                    Toast.makeText(Analytics.this, "Retrieve object failed", Toast.LENGTH_SHORT).show();
//                    Log.d(TAG,"failed to retrieve",task.getException());
//                }
//                if(arrayOrder.size()==0){
//                    Log.d("FAILED","Failed To Retrieve");
//                }
//            }
//        });
//
//        db.collectionGroup("order").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//
//                    for(DocumentChange dc : task.getResult().getDocumentChanges()){
//                        if(dc.getType()==DocumentChange.Type.ADDED){
//                            arrayOrder.add(dc.getDocument().toObject(Order.class));
//                        }
//                    }
//                }
//            }
//        });

//        for (int i = 0; i<arrayOrder.size(); i++){
//            totalSales = totalSales + arrayOrder.get(i).getTotalPrice();
//        }
//
//        return arrayOrder.size();
//
//    }
}