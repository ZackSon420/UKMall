package com.example.ukmall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        manageOrderBT = findViewById(R.id. bt_manageorder);
        analytics = findViewById(R.id.bt_analytics);

        sellerPageBT.setOnClickListener(this);
        toReceiveIv.setOnClickListener(this);
        analytics.setOnClickListener(this);



        //Declare Session
        sessionManager = new SessionManager(this);
        tv_userName = findViewById(R.id.tv_usernameaccount);
        show_username();
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
               // startActivity(new Intent(getApplicationContext(), Add_Product.class));
                Toast.makeText(Account_User.this, "To Ship", Toast.LENGTH_SHORT).show();
                break;

            case R.id.toReceiveIV:
                Toast.makeText(Account_User.this, "To Receive", Toast.LENGTH_SHORT).show();
                break;

            case R.id.completeIV:
                Toast.makeText(Account_User.this, "Complete", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bt_sellerpage:
                startActivity(new Intent(getApplicationContext(), UserProductPage.class));
                Toast.makeText(Account_User.this, "Seller Page", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bt_manageorder:
                //startActivity(new Intent(getApplicationContext(), Add_Product.class));
                Toast.makeText(Account_User.this, "Manage Order", Toast.LENGTH_SHORT).show();

            case R.id.bt_analytics:
                startActivity(new Intent(getApplicationContext(), Analytics.class));
                Toast.makeText(Account_User.this, "Analytics", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}