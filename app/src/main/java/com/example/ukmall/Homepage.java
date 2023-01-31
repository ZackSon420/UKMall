package com.example.ukmall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ukmall.viewmodel.CartViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Homepage extends AppCompatActivity implements View.OnClickListener{

    //Declare RecyclerView
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Product> productArrayList;
    FirebaseFirestore db;
    StorageReference storage;
    private CartViewModel viewModel;
    FirebaseAuth mAuth;

    private SessionManager sessionManager;
    //button search
    ImageView  iv_searchproduct,iv_User,iv_Logout;
    EditText searchBox;
    TextView tv_userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_activity);
        mAuth = FirebaseAuth.getInstance();

        viewModel = new ViewModelProvider(this).get(CartViewModel.class);


        iv_User=findViewById(R.id.iv_user);
        iv_User.setOnClickListener(this);

        iv_Logout=findViewById(R.id.iv_logout);
        iv_Logout.setOnClickListener(this);

        searchBox = findViewById(R.id.et_searchbox);
        //button search
        iv_searchproduct=findViewById(R.id.iv_searchprod);
        iv_searchproduct.setOnClickListener(this);

        //recyclerView products
        recyclerView=findViewById(R.id.rv_products);
        layoutManager=new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerViewAdapter=new RecyclerViewAdapter(arr);

        db = FirebaseFirestore.getInstance();
        productArrayList = new ArrayList<Product>();
        recyclerViewAdapter=new RecyclerViewAdapter(Homepage.this, productArrayList);

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
        tv_userName = findViewById(R.id.tv_userName);
        show_username();



        iv_searchproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Retrieve the text from the searchBox
                String searchText = searchBox.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    //Perform the search query on your Firebase Firestore database
                    performSearch(searchText);
                }
                else{
                    EventChangeListener();
                }
            }



        });



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
                        startActivity(new Intent(getApplicationContext(), Cart.class));
                        Toast.makeText(Homepage.this, "Add to Cart", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.addproduct:
                        startActivity(new Intent(getApplicationContext(), Add_Product.class));
                        Toast.makeText(Homepage.this, "Add Product", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.account:
                        startActivity(new Intent(getApplicationContext(),ManageOrder.class));
                        Toast.makeText(Homepage.this, "ManageOrder", Toast.LENGTH_SHORT).show();
                        return true;

                }
                return false;
            }
        });


    }


    public void show_username(){
        /*Intent intent = getIntent();

        String username = intent.getStringExtra("name");*/
//
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

    private void performSearch(String searchText) {

        //Query the Firebase Firestore database for documents that contain the searchText in their "name" field
        Query query = db.collection("product").whereEqualTo("productTitle", searchText);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //Clear the productArrayList
                    productArrayList.clear();
                    //Iterate through the query results
                    for (DocumentSnapshot document : task.getResult()) {
                        //Add the product to the productArrayList
                        productArrayList.add(document.toObject(Product.class));
                    }
                    //Notify the RecyclerViewAdapter that the data has changed
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void EventChangeListener() {
        productArrayList.clear();

        db.collection("product").orderBy("productTitle", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
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




    //Untuk function search
    @Override
    public void onClick(View view) {


        switch (view.getId()){

            case R.id.iv_searchprod:
                Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
                break;

//Implements session for logout
            case R.id.iv_logout:
                sessionManager.logout();
                startActivity(new Intent(getApplicationContext(),Login.class));
                Toast.makeText(Homepage.this, "Logout successfully!", Toast.LENGTH_SHORT).show();
                viewModel.deleteAllCartItems();
                break;

            case R.id.iv_user:
                startActivity(new Intent(getApplicationContext(),UserProductPage.class));
                Toast.makeText(Homepage.this, "Seller Page", Toast.LENGTH_SHORT).show();
                break;

        }
    }

}
