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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserProductPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    User_RV_Adapter recyclerViewAdapter;
    ArrayList<Product> productArrayList;
    FirebaseFirestore db;
    private SessionManager sessionManager;
    TextView tv_userName;
    FirebaseAuth mAuth;
    ImageView profiledp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_page);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



        recyclerView=findViewById(R.id.rv_user_product);
        layoutManager=new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerViewAdapter=new RecyclerViewAdapter(arr);

        db = FirebaseFirestore.getInstance();
        productArrayList = new ArrayList<Product>();
        recyclerViewAdapter=new User_RV_Adapter(UserProductPage.this, productArrayList);

//        Reference storage
        //storage = FirebaseStorage.getInstance().getReference("product_images/");

        //Adapter ada dalam kelas Java baru named "RecyclerViewAdapter"
        recyclerView.setAdapter(recyclerViewAdapter);

        EventChangeListener();
        recyclerView.setHasFixedSize(true);

        // BottomNavigationView
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigation);

        // Utk set Home as default
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Declare Session
        sessionManager = new SessionManager(this);
        tv_userName = findViewById(R.id.tv_usernamepd);
        show_username();

        // Perform item selected listener untuk button BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Homepage.class));
                        Toast.makeText(UserProductPage.this, "Homepage", Toast.LENGTH_SHORT).show();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.addtocart:
                        startActivity(new Intent(getApplicationContext(), Cart.class));
                        Toast.makeText(UserProductPage.this, "Add to Cart", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.addproduct:
                        startActivity(new Intent(getApplicationContext(), Add_Product.class));
                        Toast.makeText(UserProductPage.this, "Add Product", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.account:
                        startActivity(new Intent(getApplicationContext(),Analytics.class));
                        Toast.makeText(UserProductPage.this, "Analytics", Toast.LENGTH_SHORT).show();
                        return true;

                }
                return false;
            }
        });
    }

    private void EventChangeListener() {
        productArrayList.clear();

        //db.collection("product").document("CUS"+mAuth.getCurrentUser().getUid()).collection("Product").orderBy("productTitle", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
        db.collection("product").whereEqualTo("userId", mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("Error", e.getMessage());
                }
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            productArrayList.add(dc.getDocument().toObject(Product.class));
                            recyclerViewAdapter.notifyDataSetChanged();
                            break;
                    }
                }
            }
        });
    }

    public void show_username(){
        /*Intent intent = getIntent();

        String username = intent.getStringExtra("name");*/

//        sessionManager =new SessionManager(this);
//        String username = sessionManager.getUsername();
//
//        tv_userName.setText(username);

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
}