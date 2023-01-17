package com.example.ukmall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ManageOrder extends AppCompatActivity implements View.OnClickListener {

    //Declare Recycler View

    private RecyclerView manageOrderRV;
    RecyclerView.LayoutManager layoutManager;
    ManageOrderAdapter manageOrderAdapter;
    FirebaseFirestore db;
    ArrayList<Order> orderArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order);

        //recycler view order
        manageOrderRV = findViewById(R.id.rv_manage_order);
        layoutManager=new GridLayoutManager(this, 1);
        manageOrderRV.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();
        orderArrayList = new ArrayList<Order>();
        manageOrderAdapter =new ManageOrderAdapter(ManageOrder.this, orderArrayList);

        manageOrderRV.setAdapter(manageOrderAdapter);
        EventChangeListener();
        manageOrderRV.setHasFixedSize(true);
    }

    private void EventChangeListener() {

        db.collectionGroup("order").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
    }

    @Override
    public void onClick(View view) {

    }
}