package com.example.ukmall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Homepage extends AppCompatActivity implements View.OnClickListener {

    //Declare RecyclerView
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Product> productArrayList;
    FirebaseFirestore db;

    //button search
    ImageView  iv_searchproduct;

    TextView tv_userName;

    //Array utk RecyclerView
    //Nanti tukar kepada data dalam firebase
    int []arr={R.drawable.brownies,R.drawable.brownies,R.drawable.brownies,R.drawable.brownies,R.drawable.brownies,
            R.drawable.brownies,R.drawable.brownies,R.drawable.brownies};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_activity);

        tv_userName = findViewById(R.id.tv_userName);
        show_username();

        //button search
        iv_searchproduct=findViewById(R.id.iv_searchprod);
        iv_searchproduct.setOnClickListener(this);

        //recyclerView products
        recyclerView=findViewById(R.id.rv_products);
        layoutManager=new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerViewAdapter=new RecyclerViewAdapter(arr);

        db = FirebaseFirestore.getInstance();
        productArrayList = new ArrayList<Product>();
        recyclerViewAdapter=new RecyclerViewAdapter(Homepage.this, productArrayList);

        //Adapter ada dalam kelas Java baru named "RecyclerViewAdapter"
        recyclerView.setAdapter(recyclerViewAdapter);

        EventChangeListener();

        recyclerView.setHasFixedSize(true);

        // BottomNavigationView
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigation);

        // Utk set Home as default
        bottomNavigationView.setSelectedItemId(R.id.home);


        // Perform item selected listener untuk button BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Homepage.class));
                        Toast.makeText(Homepage.this, "Homepage", Toast.LENGTH_SHORT).show();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.addtocart:
                        Toast.makeText(Homepage.this, "Add to Cart", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.addproduct:
                        startActivity(new Intent(getApplicationContext(), Add_Product.class));
                        //Toast.makeText(Add_Product.class, "Add Product", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.account:
                        Toast.makeText(Homepage.this, "User Account", Toast.LENGTH_SHORT).show();
                        return true;

                }
                return false;
            }
        });


    }

    private void EventChangeListener(){

        db.collectionGroup("product").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for(DocumentChange dc : task.getResult().getDocumentChanges()){
                        if(dc.getType()==DocumentChange.Type.ADDED){
                            productArrayList.add(dc.getDocument().toObject(Product.class));
                        }
                    }
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });

    }

//    private void EventChangeListener() {
//
//        db.collection("store") // Order product placement ikut nama
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                        if(error != null){
//                            Log.e("Firestore Error", error.getMessage());
//                            return;
//                        }
//
//                        for(DocumentChange dc : value.getDocumentChanges()){
//                            if(dc.getType()==DocumentChange.Type.ADDED){
//                                productArrayList.add(dc.getDocument().toObject(Product.class));
//                            }
//                        }
//
//                       recyclerViewAdapter.notifyDataSetChanged();
//
//                    }
//                });
//
//    }

    public void show_username(){
        Intent intent = getIntent();

        String username = intent.getStringExtra("name");

        tv_userName.setText(username);
    }

    //Untuk function search
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.iv_searchprod:
                Toast.makeText(this, "This is Search Function", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}