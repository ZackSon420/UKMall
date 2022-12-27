package com.example.ukmall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Homepage extends AppCompatActivity implements View.OnClickListener {

    //Declare RecyclerView
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapter recyclerViewAdapter;

    //button search
    ImageView  iv_searchproduct;

    //Array utk RecyclerView
    //Nanti tukar kepada data dalam firebase
    int []arr={R.drawable.brownies,R.drawable.brownies,R.drawable.brownies,R.drawable.brownies,R.drawable.brownies,
            R.drawable.brownies,R.drawable.brownies,R.drawable.brownies};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_activity);

        //button search
        iv_searchproduct=findViewById(R.id.iv_searchprod);
        iv_searchproduct.setOnClickListener(this);

        //recyclerView products
        recyclerView=findViewById(R.id.rv_products);
        layoutManager=new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter=new RecyclerViewAdapter(arr);

        //Adapter ada dalam kelas Java baru named "RecyclerViewAdapter"
        recyclerView.setAdapter(recyclerViewAdapter);

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
                        Toast.makeText(Homepage.this, "Add Product", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.account:
                        Toast.makeText(Homepage.this, "User Account", Toast.LENGTH_SHORT).show();
                        return true;

                }
                return false;
            }
        });


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