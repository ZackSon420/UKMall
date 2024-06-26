package com.example.ukmall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ukmall.utils.model.Item;
import com.example.ukmall.viewmodel.CartViewModel;

import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MakeOrder extends AppCompatActivity implements View.OnClickListener,CartAdapter.CartClickedListeners, Serializable {

    DecimalFormat df = new DecimalFormat("0.00");
    private Button btnMakeOrder;
    private Spinner paymentMethod, deliveryOption;
    String SECRET_KEY = "";
    String PUBLISH_KEY = "";
    PaymentSheet paymentSheet;
    String customerID, GetSellerProductId;
    String FinalSellerId = " ";
    String EphericalKey;
    String ClientSecret;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String paymentMethodStr, deliveryOptionStr, orderid, username, sellerID;
    private Double totalPrice, subtotal;

    private RecyclerView cartView;
    RecyclerView.LayoutManager cartlayoutManager;
    MakeOrderAdapter cartAdapter;
    private CartViewModel cartViewModel;
    private TextView totalCartPriceTV, TVfee, TVsubprice;
    public List<Item> selectedProductList;
    private ArrayList<Object> arrayOrder = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeorder);

        mAuth = FirebaseAuth.getInstance();
        totalCartPriceTV = findViewById(R.id.tv_totalprice);
        TVfee = findViewById(R.id.tv_fee);
        TVsubprice = findViewById(R.id.tv_subtotal);
        btnMakeOrder = findViewById(R.id.btn_make_order);
        btnMakeOrder.setOnClickListener(this);
        //CartViewModel
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        //RecyclerView Cart
        cartView = findViewById(R.id.rv_makeorder);
        cartlayoutManager = new GridLayoutManager(this, 1);
        cartView.setLayoutManager(cartlayoutManager);
        cartAdapter = new MakeOrderAdapter(this);

        cartView.setAdapter(cartAdapter);
        cartView.setHasFixedSize(true);

        btnMakeOrder = findViewById(R.id.btn_make_order);
        paymentMethod = findViewById(R.id.spinner_paymentMethod);
        deliveryOption = findViewById(R.id.spinner_deliveryOption);

        PaymentConfiguration.init(this, PUBLISH_KEY);
        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {

            onPaymentResult(paymentSheetResult);

        });

        //StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            customerID = object.getString("id");
                            getEphericalKey(customerID);

                        } catch (JSONException e) {

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MakeOrder.this);
        requestQueue.add(stringRequest);


        btnMakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PaymentFlow();
                //makeOrder();

            }
        });

        cartViewModel.getAllCartItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> productCarts) {
                double price = 0;
                double fee = 1.0;

                cartAdapter.setItemCartList(productCarts);
                for (int i = 0; i < productCarts.size(); i++) {
                    String itemName, productID;
                    Double itemPrice;
                    final Long[] quantity = new Long[1];
                    final Double[] totalSale = new Double[1];

                    price = price + productCarts.get(i).getTotalItemPrice();

                    sellerID = productCarts.get(i).getSellerID();
                    productID = productCarts.get(i).getProductID();
                    itemName = productCarts.get(i).getItemName();
                    itemPrice = productCarts.get(i).getItemPrice();
                    quantity[0] = Long.valueOf(productCarts.get(i).getQuantity());
                    totalSale[0] = Double.valueOf(productCarts.get(i).getTotalItemPrice());

                    HashMap<String, Object> prodhashMap = new HashMap<>();
                    prodhashMap.put("productID", productID);
                    prodhashMap.put("itemName", itemName);
                    prodhashMap.put("itemPrice", itemPrice);
                    prodhashMap.put("quantity", quantity[0]);
                    arrayOrder.add(prodhashMap);

                    //Option to get one sellerId from latest product
                    GetSellerProductId = productCarts.get(i).getProductID();

                    /*Store sellerId for each product
                    seller = productCarts.get(i).getSellerId();
                    prodhashMap.put("sellerId", seller);*/


                    db.collection("product").document(productID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Object bought = documentSnapshot.get("bought");

                            quantity[0] = quantity[0] + (Long) bought;
                            totalSale[0] = totalSale[0] + Double.parseDouble(documentSnapshot.get("totalSale").toString());

                            db.collection("product").document(productID).update("bought", quantity[0]);
                            db.collection("product").document(productID).update("totalSale", totalSale[0]);
                        }
                    });

                }
                //selectedProductList.addAll(productCarts);
                TVsubprice.setText(df.format(price));
                TVfee.setText(df.format(fee));
                totalCartPriceTV.setText(df.format(price+fee));

            }

        });
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "payment success", Toast.LENGTH_SHORT).show();
            makeOrder();
        }
        if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Toast.makeText(this, "payment failed", Toast.LENGTH_SHORT).show();
            // makeOrder();
        }
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(this, "payment canceled", Toast.LENGTH_SHORT).show();
            // makeOrder();
        }
    }

    private void getEphericalKey(String customerID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            EphericalKey = object.getString("id");
                            getClientSecret(customerID, EphericalKey);

                        } catch (JSONException e) {

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                header.put("Stripe-Version", "2022-11-15 ");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MakeOrder.this);
        requestQueue.add(stringRequest);

    }

    private void getClientSecret(String customerID, String ephericalKey) {
        totalPrice = Double.valueOf(totalCartPriceTV.getText().toString());
        subtotal = Double.valueOf(TVsubprice.getText().toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            ClientSecret = object.getString("client_secret");
                            // getClientSecret(customerID,EphericalKey);


                        } catch (JSONException e) {

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // Toast.makeText(MakeOrder.this, "Price:"+totalPrice, Toast.LENGTH_SHORT).show();
                //double num=60;
                String totalPrice = totalCartPriceTV.getText().toString();
                Long totalPriceCents = (long) (Double.parseDouble(totalPrice) * 100);
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                params.put("amount", String.valueOf(totalPriceCents));
                params.put("currency", "myr");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MakeOrder.this);
        requestQueue.add(stringRequest);

    }

    private void PaymentFlow() {

        paymentSheet.presentWithPaymentIntent(
                ClientSecret, new PaymentSheet.Configuration("testing"
                        , new PaymentSheet.CustomerConfiguration(
                        customerID,
                        EphericalKey
                ))
        );

    }


    private void makeOrder() {

        String timestamp = "" + System.currentTimeMillis();
        totalPrice = Double.valueOf(totalCartPriceTV.getText().toString());
        paymentMethodStr = paymentMethod.getSelectedItem().toString();
        deliveryOptionStr = deliveryOption.getSelectedItem().toString();
        orderid = "ORD" + timestamp;

        db.collection("product").whereEqualTo("productId",GetSellerProductId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(MakeOrder.this,"Dapat WOI PRODUCT DETAILS",Toast.LENGTH_SHORT).show();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Fetch from database as Map
                        FinalSellerId = (String) document.getData().get("userId");
//                        Toast.makeText(MakeOrder.this,"SELLER ID " + FinalSellerId, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        addOrder();

    }

    private void addOrder() {
        CollectionReference orderRef = db.collection("order");

        db.collection("user").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){

                    Object name = documentSnapshot.get("userName");
                    username = name.toString();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("orderId", orderid);
                    hashMap.put("totalPrice", totalPrice);
                    hashMap.put("paymentMethod", paymentMethodStr);
                    hashMap.put("deliveryOption", deliveryOptionStr);
                    hashMap.put("customerID", mAuth.getCurrentUser().getUid());
                    hashMap.put("status", "pack");
                    hashMap.put("username", username);
                    hashMap.put("sellerID", sellerID);

                    db.collection("order").document(orderid).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                }
            }
        });


        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        //CollectionReference orderDetailRef = db.collection("orderDetail");
        // Query docRef = orderDetailRef.whereEqualTo("orderId", orderid);

        HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("orderId", orderid);
        hashMap2.put("customerID", customerID);
        hashMap2.put("paymentStatus", "Successful");


        for (int i = 0; i < arrayOrder.size(); i++) {
            db.collection("order").document(orderid).collection("ordered").add(arrayOrder.get(i));
        }

        db.collection("OrderDetail").document(orderid).set(hashMap2).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                updateTotalSpend(totalPrice);
                updateTotalSale(FinalSellerId, (totalPrice-1));

                Toast.makeText(MakeOrder.this, "Order are successful! Please wait for seller to prepare your order", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(MakeOrder.this, Receipt.class);
                intent.putExtra("subtotal", subtotal);
                intent.putExtra("totalPrice", totalPrice);
                intent.putExtra("orderID", orderid);
                startActivity(intent);
            }
        });
    }


    public void updateTotalSpend(Double totalPrice) {
        DocumentReference userRef = db.collection("user").document(mAuth.getCurrentUser().getUid());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Double initSpend, totalSpend;

                    Double spendTotal = Double.parseDouble(documentSnapshot.get("totalSpend").toString());
                    initSpend = (Double) spendTotal;
                    totalSpend = initSpend + totalPrice;

                    DocumentReference userRef = db.collection("user").document(mAuth.getCurrentUser().getUid());
                    userRef.update("totalSpend", totalSpend);

                }
            }
        });
    }

    //add new sales into seller database
    public void updateTotalSale (String SellerId, Double newSales){
        DocumentReference sellerRef = db.collection("user").document(SellerId);
        sellerRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Double initSale, totalSales;

                    Double saleTotal = Double.parseDouble(documentSnapshot.get("totalSales").toString());
                    initSale = (Double) saleTotal;
                    totalSales = initSale+newSales;

                    sellerRef.update("totalSales", totalSales);

                }
            }
        });
    }


//    public void updateTotalSpend(Double totalPrice) {
//        DocumentReference userRef = db.collection("user").document(mAuth.getCurrentUser().getUid());
//        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()) {
//
//                    Object spendTotal = documentSnapshot.get("totalSpend");
//                    Double totalSpend = totalPrice + (Double) spendTotal;
//
//                    DocumentReference userRef = db.collection("user").document(mAuth.getCurrentUser().getUid());
//                    userRef.update("totalSpend", totalSpend);
//
//                }
//            }
//        });
//    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDeleteClicked(Item item) {

    }

    @Override
    public void onPlusClicked(Item item) {

    }

    @Override
    public void onMinusClicked(Item item) {

    }
}
