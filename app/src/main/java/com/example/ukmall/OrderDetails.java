package com.example.ukmall;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class OrderDetails extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvOrderDetails;
    RecyclerView.LayoutManager layoutManager;
    OrderDetailsAdapter orderDetailsAdapter;
    ArrayList<Item> itemArrayList;
    TextView tvOrderId, tvOrderDate, tvCustName, tvPhoneNumber, tvEmail, tvPayment, tvDelivery, tvTotalPrice;
    Button btnAccept, btnCancel;
    ImageView ivCust;
    String docId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference orderRef = db.collection("order");
    String[] orderStatus ={"pack"};
    String c_orderStatus="pack";
    ArrayList<String> orderArrayList;
    Button btn;
    boolean sellerPage=true;
    private String storedData;
    private SharedPreferences setKeys;
    private SharedPreferences.Editor editKeys;
    // private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private DocumentReference orderRef2 = db.collection("order").document(docId);
    //TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        orderArrayList = new ArrayList<String>();
        tvOrderId = findViewById(R.id.tv_orderId);
        tvOrderDate = findViewById(R.id.tv_order_date);
        tvCustName = findViewById(R.id.tv_custName);
        tvPhoneNumber = findViewById(R.id.tv_custPhone);
        tvEmail = findViewById(R.id.tv_custEmail);
        tvPayment = findViewById(R.id.tv_payment_method);
        tvDelivery = findViewById(R.id.tv_delivery_method);
        tvTotalPrice = findViewById(R.id.tv_totalpriceOrder);
        ivCust = findViewById(R.id.img_custOrder);
       // tvMessage = findViewById(R.id.message);

        btnAccept = findViewById(R.id.btn_accept);
        btnCancel = findViewById(R.id.btn_cancel);
        btnAccept.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        //intent
        Intent intent = getIntent();
        docId = (intent.getStringExtra("orderId"));
        tvOrderId.setText(intent.getStringExtra("orderId"));
        tvPayment.setText(intent.getStringExtra("payment"));
        tvDelivery.setText(intent.getStringExtra("delivery"));
        tvTotalPrice.setText("RM" + intent.getStringExtra("totalprice"));

        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
        DocumentReference orderRef2 = db2.collection("order").document(intent.getStringExtra("orderId"));
        orderRef2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (snapshot != null && snapshot.exists()) {
                    String status = snapshot.getString("username");
                    tvCustName.setText(status);
                    //  editor.putString("status", status);
                    // editor.apply();
                    //Log.d(TAG, "Order status: " + status);

                } else {
                    Log.d(TAG, "No document found with id: " + intent.getStringExtra("orderId"));
                }
            }
        });
//        orderStatus[0] = "ready";

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference orderRef = db.collection("order").document(docId);
//        orderRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        String status = document.getString("status");
//                        Log.d(TAG, "Order status: " + status);
//                        //orderStatus[0]=status;
//                    } else {
//                        Log.d(TAG, "No document found with id: " + docId);
//                    }
//                } else {
//                    Log.d(TAG, "Error getting document: ", task.getException());
//                }
//            }
//        });
        SharedPreferences sharedPreferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        //SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sharedPreferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference orderRef = db.collection("order").document(docId);
        orderRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (snapshot != null && snapshot.exists()) {
                    String status = snapshot.getString("status");
                    editor.putString("status", status);
                    editor.apply();
                    Log.d(TAG, "Order status: " + status);

                } else {
                    Log.d(TAG, "No document found with id: " + docId);
                }
            }
        });

        //SharedPreferences sharedPreferences = getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
        storedData = sharedPreferences.getString("status", "");
       String storedData2 =sharedPreferences.getString("status", "");
        orderStatus[0]=ManageOrderSeller.status;
        c_orderStatus=ManageOrder.c_status;
        Log.d(TAG, storedData);
        Log.d(TAG, storedData2);
        storedData="";
        editor.remove("status");
        editor.apply();
        btn =null;

        //String orderStatus = "pack"; // or "ready"
       // Button button = null;

        if ((orderStatus[0]!=null && orderStatus[0].equals("pack"))||(c_orderStatus!=null&&c_orderStatus.equals("pack"))) {
            btn = new Button(this);
            btn.setText("Pack");
        } else if (orderStatus[0]!=null && orderStatus[0].equals("ready")||(c_orderStatus!=null&&c_orderStatus.equals("ready"))) {
            btn = new Button(this);
            btn.setText("Order Received");
        }

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        //params.bottomToBottom = R.id.textView25;
        if ((orderStatus[0]!=null && orderStatus[0].equals("pack"))||(c_orderStatus!=null&&c_orderStatus.equals("pack"))) {
            params.setMargins(0, 16, 550, 0);
            params.topToBottom = R.id.linearLayout13;
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            btn.setLayoutParams(params);
            ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
            constraintLayout.addView(btn);
        }
        if ((orderStatus[0]!=null && orderStatus[0].equals("ready"))||(c_orderStatus!=null&&c_orderStatus.equals("ready"))) {
            params.setMargins(0, 16, 450, 0);
            params.topToBottom = R.id.linearLayout13;
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            btn.setLayoutParams(params);
            ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);
            constraintLayout.addView(btn);
        }


        final AlertDialog.Builder[] builder = new AlertDialog.Builder[1];

        if (orderStatus[0]!=null && orderStatus[0].equals("pack")) {
            Button finalBtn = btn;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    builder[0] = new AlertDialog.Builder(OrderDetails.this);
                    builder[0].setTitle("Confirm");
                    builder[0].setMessage("Are you sure you want to change the status to ready?");
                    builder[0].setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            orderStatus[0] = "ready";
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference orderRef = db.collection("order").document(docId);
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("status", "ready");

                            orderRef.update(updates);

                            // orderStatus[0] = "completed";
                            finalBtn.setText("Waiting for customer to Receive Order ");
                            finalBtn.setEnabled(false);
                            finalBtn.setBackgroundColor(0xFFFFFF);
                            finalBtn.setTextColor(Color.BLACK);
                            params.setMargins(0, 16, 130, 0);

                            //finalBtn.setVisibility(View.GONE);
                        }
                    });
                    builder[0].setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder[0].create().show();
                }
            });
        } else if (orderStatus[0]!=null && orderStatus[0].equals("ready")) {

            Button finalBtn1 = btn;
            finalBtn1.setText("Waiting for customer to Receive Order ");
            finalBtn1.setEnabled(false);
            finalBtn1.setBackgroundColor(0xFFFFFF);
            finalBtn1.setTextColor(Color.BLACK);
            params.setMargins(0, 16, 130, 0);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    builder[0] = new AlertDialog.Builder(OrderDetails.this);
                    builder[0].setTitle("Order Received");
                    builder[0].setMessage("Are you sure you have received the full order?");
                    builder[0].setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            orderStatus[0] = "completed";
                            finalBtn1.setText("Order Completed");
                            finalBtn1.setEnabled(false);
                            finalBtn1.setBackgroundColor(0xFFFFFF);
                            finalBtn1.setTextColor(Color.BLACK);
                            params.setMargins(0, 16, 450, 0);

                            //finalBtn1.setVisibility(View.GONE);
                        }
                    });
                    builder[0].setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder[0].create().show();
                }
            });
        }else if (c_orderStatus!=null&&c_orderStatus.equals("pack")){
            Button finalBtn1 = btn;
            finalBtn1.setText("Waiting for seller to Pack Order ");
            finalBtn1.setEnabled(false);
            finalBtn1.setBackgroundColor(0xFFFFFF);
            finalBtn1.setTextColor(Color.BLACK);
            params.setMargins(0, 16, 130, 0);
        }else if (c_orderStatus!=null&&c_orderStatus.equals("ready")){
            Button finalBtn = btn;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    builder[0] = new AlertDialog.Builder(OrderDetails.this);
                    builder[0].setTitle("Confirm");
                    builder[0].setMessage("Are you sure you have received the order?");
                    builder[0].setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            orderStatus[0] = "ready";
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference orderRef = db.collection("order").document(docId);
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("status", "completed");

                            orderRef.update(updates);

                            // orderStatus[0] = "completed";
                            finalBtn.setText("Order has been completed");
                            finalBtn.setEnabled(false);
                            finalBtn.setBackgroundColor(0xFFFFFF);
                            finalBtn.setTextColor(Color.BLACK);
                            params.setMargins(0, 16, 130, 0);

                            //finalBtn.setVisibility(View.GONE);
                        }
                    });
                    builder[0].setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder[0].create().show();
                }
            });
        }

        /*tvProductDesc.setText(intent.getStringExtra("productDesc"));
        tvProductPrice.setText(intent.getStringExtra("productPrice"));
        tvStoreName.setText(intent.getStringExtra("store"));
        img = intent.getStringExtra("productImage");
        img2 = intent.getStringExtra("productImage2");*/

        //recyclerView order details
        rvOrderDetails = findViewById(R.id.rv_orderDetails);
        layoutManager=new GridLayoutManager(this, 1);
        rvOrderDetails.setLayoutManager(layoutManager);

        itemArrayList = new ArrayList<Item>();
        orderDetailsAdapter = new OrderDetailsAdapter(this, itemArrayList);

        rvOrderDetails.setAdapter(orderDetailsAdapter);
        EventChangeListener();
        rvOrderDetails.setHasFixedSize(true);



    }

    private void EventChangeListener() {

        //Display selected product dalam specific order
        CollectionReference orderRef = db.collection("order").document(docId).collection("ordered");
        orderRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for(DocumentChange dc : task.getResult().getDocumentChanges()){
                        if(dc.getType()==DocumentChange.Type.ADDED){
                            itemArrayList.add(dc.getDocument().toObject(Item.class));
                        }
                    }
                    orderDetailsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public class OrderStatusHandler implements OnCompleteListener<QuerySnapshot> {
        private String status;

        public String getStatus() {
            return status;
        }

        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                QuerySnapshot result = task.getResult();
                if (!result.isEmpty()) {
                    DocumentSnapshot orderDocument = result.getDocuments().get(0);
                    status = orderDocument.getString("status");
                }
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_accept:
                Toast.makeText(this, "Order Accepted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,Homepage.class));
                break;
            case R.id.btn_cancel:
                startActivity(new Intent(this, ManageOrder.class));
                break;
        }
    }

}