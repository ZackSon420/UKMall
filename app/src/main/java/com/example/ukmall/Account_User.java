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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

public class Account_User extends AppCompatActivity implements View.OnClickListener {

    DecimalFormat df = new DecimalFormat("0.00");
    private SessionManager sessionManager;
    TextView tv_userName, tv_totalSale;
    ImageView toShipIv, toReceiveIv, completeIv;
    Button sellerPageBT, manageOrderBT, analytics;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_user);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tv_totalSale=findViewById(R.id.tv_totalsales3);
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
        get_totalSale();

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

        DocumentReference userRef = db.collection("user").document(mAuth.getCurrentUser().getUid());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){

                    Object name = documentSnapshot.get("userName");
                    tv_userName.setText(""+name);

                }
            }
        });
    }

    public void get_totalSale(){
        DocumentReference userRef = db.collection("user").document(mAuth.getCurrentUser().getUid());

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Object totalSales = documentSnapshot.get("totalSales");

                    tv_totalSale.setText(""+df.format(totalSales));
                }
            }
        });
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