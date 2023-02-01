package com.example.ukmall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class Admin extends AppCompatActivity implements View.OnClickListener{

    //Declare Recycler View

    DecimalFormat df = new DecimalFormat("0.00");
    private RecyclerView topSellerRV;
    RecyclerView.LayoutManager layoutManager;
    AdminAdapter adminAdapter;
    FirebaseFirestore db;
    ArrayList<user> sellerArrayList;
    TextView tvTotalUser, tvTotalProduct, tvTotalOrder, tvTotalSales;
    Double totalSales = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        tvTotalUser = findViewById(R.id.tv_totalUser);
        tvTotalOrder = findViewById(R.id.tv_totalOrder);
        tvTotalProduct = findViewById(R.id.tv_totalProduct);
        tvTotalSales = findViewById(R.id.tv_totalSales);

        topSellerRV = findViewById(R.id.rv_seller);
        layoutManager=new GridLayoutManager(this, 1);
        topSellerRV.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();
        sellerArrayList = new ArrayList<user>();
        adminAdapter = new AdminAdapter(Admin.this, sellerArrayList);

        CollectionReference productCollection = db.collection("product");
        CollectionReference orderCollection = db.collection("order");
        CollectionReference userCollection = db.collection("user");

        AggregateQuery countProductQuery = productCollection.count();
        AggregateQuery countOrderQuery = orderCollection.count();
        AggregateQuery countSellerQuery = userCollection.count();

        countProductQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                tvTotalProduct.setText("" + snapshot.getCount());
            }
        });

        countOrderQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                tvTotalOrder.setText("" + snapshot.getCount());
            }
        });

        countSellerQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                tvTotalUser.setText("" + snapshot.getCount());
            }
        });

        getTotalSales();

        topSellerRV.setAdapter(adminAdapter);
        EventChangeListener();
        topSellerRV.setHasFixedSize(true);

    }

    private void getTotalSales() {

        db.collectionGroup("order").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Double totalPrice = document.getDouble("totalPrice");
                        totalSales += totalPrice;
                    }
                }else{
                    Log.d("debug", "Error");
                }
                tvTotalSales.setText(String.valueOf("RM" + df.format(totalSales)));
            }
        });
    }

    private void EventChangeListener() {

        db.collectionGroup("user").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for(DocumentChange dc : task.getResult().getDocumentChanges()){
                        if(dc.getType()==DocumentChange.Type.ADDED){
                            sellerArrayList.add(dc.getDocument().toObject(user.class));

                            Collections.sort(sellerArrayList, new Comparator<user>() {
                                @Override
                                public int compare(user s1, user s2) {

                                    if (s1.totalSales > s2.totalSales) {
                                        return -1;
                                    }
                                    else if (s1.totalSales < s2.totalSales) {
                                        return 1;
                                    }
                                    else {
                                        return 0;
                                    }
                                }
                            });
                        }
                    }
                    adminAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

}