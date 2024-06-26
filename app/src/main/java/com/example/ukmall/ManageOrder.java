package com.example.ukmall;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ManageOrder extends AppCompatActivity  {

    //Declare Recycler View

    private RecyclerView manageOrderRV;
    RecyclerView.LayoutManager layoutManager;
    ManageOrderAdapter manageOrderAdapter;
    FirebaseFirestore db;
    ArrayList<Order> orderArrayList;
    TabLayout tabLayout;
    Button btnAnalytics;
    String custStatus=null;
    public static String c_status=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order);

    //    btnAnalytics = findViewById(R.id.btn_analytic);
       // btnAnalytics.setOnClickListener(this);


        //recycler view order
        manageOrderRV = findViewById(R.id.rv_manage_order);
        layoutManager=new GridLayoutManager(this, 1);
        manageOrderRV.setLayoutManager(layoutManager);
        tabLayout=findViewById(R.id.tabs2);
        db = FirebaseFirestore.getInstance();
        orderArrayList = new ArrayList<Order>();
        manageOrderAdapter =new ManageOrderAdapter(ManageOrder.this, orderArrayList);

        manageOrderRV.setAdapter(manageOrderAdapter);
        String userId = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            userId = user.getUid();
        } else {
            // No user is signed in
        }
        Log.d(TAG, "UserID: " + userId);
        EventChangeListener(userId);
        manageOrderRV.setHasFixedSize(true);
        String finalUserId = userId;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Perform action when tab is selected
                switch (tab.getPosition()) {
                    case 0:
                        // Open file for "To Pack" tab
                        EventChangeListener(finalUserId);
                        break;
                    case 1:
                        // Open file for "Ready" tab
                        EventChangeListener2(finalUserId);
                        break;
                    case 2:
                        // Open file for "Completed" tab
                        EventChangeListener3(finalUserId);
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Perform action when tab is unselected
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Perform action when tab is reselected
            }
        });

    }

    private void EventChangeListener(String userId) {

        orderArrayList.clear();

        db.collectionGroup("order")
                .whereEqualTo("customerID", userId+"")
                .whereEqualTo("status", "pack")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for(DocumentChange dc : task.getResult().getDocumentChanges()){
                        if(dc.getType()==DocumentChange.Type.ADDED){
                            orderArrayList.add(dc.getDocument().toObject(Order.class));
                        }
                    }
                    manageOrderAdapter.notifyDataSetChanged();
                }
            }
        });
        c_status="pack";
    }

    private void EventChangeListener2(String userId) {
        orderArrayList.clear();

        db.collectionGroup("order")
                .whereEqualTo("customerID", userId+"")
                .whereEqualTo("status", "ready")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for(DocumentChange dc : task.getResult().getDocumentChanges()){
                                if(dc.getType()==DocumentChange.Type.ADDED){
                                    orderArrayList.add(dc.getDocument().toObject(Order.class));
                                }
                            }
                            manageOrderAdapter.notifyDataSetChanged();
                        }
                    }
                });
        c_status="ready";
    }
    private void EventChangeListener3(String userId) {

        orderArrayList.clear();

        db.collectionGroup("order")
                .whereEqualTo("customerID", userId+"")
                .whereEqualTo("status", "completed")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for(DocumentChange dc : task.getResult().getDocumentChanges()){
                                if(dc.getType()==DocumentChange.Type.ADDED){
                                    orderArrayList.add(dc.getDocument().toObject(Order.class));
                                }
                            }
                            manageOrderAdapter.notifyDataSetChanged();
                        }
                    }
                });
        c_status="completed";
    }






//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//
//            case R.id.btn_analytic:
//                startActivity(new Intent(this, Analytics.class));
//                break;
//        }
//    }
}