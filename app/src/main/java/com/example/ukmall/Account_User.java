package com.example.ukmall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Account_User extends AppCompatActivity implements View.OnClickListener {

    private SessionManager sessionManager;
    TextView tv_userName;
    ImageView toShipIv, toReceiveIv, completeIv;
    Button sellerPageBT, manageOrderBT, analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_user);

        toShipIv = findViewById(R.id.toShipIV);
        toReceiveIv = findViewById(R.id.toReceiveIV);
        completeIv = findViewById(R.id.completeIV);

        toShipIv.setOnClickListener(this);
        toReceiveIv.setOnClickListener(this);
        completeIv.setOnClickListener(this);


        sellerPageBT = findViewById(R.id.bt_sellerpage);
        manageOrderBT = findViewById(R.id.bt_manageorder);
        analytics = findViewById(R.id.bt_analytics);

        sellerPageBT.setOnClickListener(this);
        manageOrderBT.setOnClickListener(this);
        analytics.setOnClickListener(this);

        // BottomNavigationView
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigation);

        // Utk set Home as default
        bottomNavigationView.setSelectedItemId(R.id.account);



        //Declare Session
        sessionManager = new SessionManager(this);
        tv_userName = findViewById(R.id.tv_usernameaccount);
        show_username();

        //Perform item selected listener untuk button BottomNavigationView

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Homepage.class));
                        Toast.makeText(Account_User.this, "Homepage", Toast.LENGTH_SHORT).show();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.addtocart:
                        startActivity(new Intent(getApplicationContext(), Cart.class));
                        Toast.makeText(Account_User.this, "Add to Cart", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.addproduct:
                        startActivity(new Intent(getApplicationContext(), Add_Product.class));
                        Toast.makeText(Account_User.this, "Add Product", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.account:
                        //startActivity(new Intent(getApplicationContext(),ManageOrder.class));
                        //Toast.makeText(Homepage.this, "ManageOrder", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Account_User.class));
                        Toast.makeText(Account_User.this, "My Account", Toast.LENGTH_SHORT).show();
                        return true;

                }
                return false;
            }
        });
    }

    public void show_username(){
        /*Intent intent = getIntent();

        String username = intent.getStringExtra("name");*/

        sessionManager =new SessionManager(this);
        String username = sessionManager.getUsername();

        tv_userName.setText(username);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.toShipIV:
                startActivity(new Intent(getApplicationContext(), ManageOrder.class));
                Toast.makeText(Account_User.this, "To Ship", Toast.LENGTH_SHORT).show();
                break;

            case R.id.toReceiveIV:
                startActivity(new Intent(getApplicationContext(), ManageOrder.class));
                Toast.makeText(Account_User.this, "To Receive", Toast.LENGTH_SHORT).show();
                break;

            case R.id.completeIV:
                startActivity(new Intent(getApplicationContext(), ManageOrder.class));
                Toast.makeText(Account_User.this, "Complete", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bt_sellerpage:
                startActivity(new Intent(getApplicationContext(), UserProductPage.class));
                //Toast.makeText(Account_User.this, "Seller Page", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bt_manageorder:
                startActivity(new Intent(getApplicationContext(), ManageOrderSeller.class));
                //Toast.makeText(Account_User.this, "Manage Order", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bt_analytics:
                startActivity(new Intent(getApplicationContext(), Analytics.class));
                //Toast.makeText(Account_User.this, "Analytics", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}