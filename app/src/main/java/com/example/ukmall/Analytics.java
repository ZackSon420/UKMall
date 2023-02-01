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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Analytics extends AppCompatActivity {

    private RecyclerView productAView;
    RecyclerView.LayoutManager productALayoutManager;
    AnalyticsAdapter analyticsAdapter;
    private TextView TotalProductTv, TotalSalesTV, TotalOrderTV, TotalSpendTV;
    ArrayList<Product> productList;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    private Double saleTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

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

        DocumentReference userRef = db.collection("user").document(userId);

        AggregateQuery countOrderQuery = orderCollection.count();


//      Total Order
        db.collectionGroup("order")
                .whereEqualTo("sellerID", userId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Integer totalOrder = 0;
                            for (DocumentChange dc : task.getResult().getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    totalOrder = totalOrder + 1;
                                }
                            }
                            TotalOrderTV.setText("" + totalOrder);
                        }
                    }
                });

//        countOrderQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                AggregateQuerySnapshot snapshot = task.getResult();
//                TotalOrderTV.setText("" + snapshot.getCount());
//
//            }
//        });


        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Object spendTotal = documentSnapshot.get("totalSpend");
                    Object totalProduct = documentSnapshot.get("totalProduct");
                    Object totalSales = documentSnapshot.get("totalSales");

                    TotalSpendTV.setText("" + spendTotal);
                    TotalProductTv.setText(""+totalProduct);
                    TotalSalesTV.setText(""+totalSales);
                }
            }
        });

    }

    public void getSellerProduct(){
        productList.clear();

        db = FirebaseFirestore.getInstance();


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

                            Collections.sort(productList, new Comparator<Product>() {
                                @Override
                                public int compare(Product t1, Product t2) {

                                    if(t1.bought > t2.bought)
                                        return -1;
                                    else if(t1.bought<t2.bought)
                                        return 1;
                                    else
                                        return 0;
                                }
                            });


                            analyticsAdapter.notifyDataSetChanged();
                            break;
                    }
                }
            }
        });
    }

}