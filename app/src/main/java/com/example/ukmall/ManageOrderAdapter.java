package com.example.ukmall;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ManageOrderAdapter extends RecyclerView.Adapter<ManageOrderAdapter.MyViewHolder> {

    DecimalFormat df = new DecimalFormat("0.00");
    Context context;
    static ArrayList<Order> orderArrayList;

    //Constructor
    public ManageOrderAdapter(Context context, ArrayList<Order> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manageorderview,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Order order = orderArrayList.get(position);
        //Ambil gambar dari link dalam product
        //Picasso.get().load(order.getUrl()).into(holder.ivCust);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference orderRef = db.collection("order").document(order.getOrderId());
        orderRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (snapshot != null && snapshot.exists()) {
                    String status = snapshot.getString("username");
                    holder.tvCustName.setText(status);
                  //  editor.putString("status", status);
                   // editor.apply();
                    //Log.d(TAG, "Order status: " + status);

                } else {
                    Log.d(TAG, "No document found with id: " + order.getOrderId());
                }
            }
        });
       // holder.tvCustName.setText(order.);
        holder.tvOrderId.setText(order.getOrderId());
        //holder.tvOrderDate.setText(order.);
        holder.tvTotalOrder.setText("RM" + (df.format(order.getTotalPrice())));
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCust;
        TextView tvCustName,tvOrderDate, tvOrderId, tvTotalOrder, tvStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCust=itemView.findViewById(R.id.img_cust);
            tvCustName=itemView.findViewById(R.id.tv_cust_name);
            tvOrderId=itemView.findViewById(R.id.tv_order_id);
            tvOrderDate=itemView.findViewById(R.id.tv_date);
            tvTotalOrder=itemView.findViewById(R.id.tv_totalp_order);
            tvStatus=itemView.findViewById(R.id.tv_status);

            //call selected product data
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //pass to OrderDetails
                    int index = getAdapterPosition();
                    Intent intent = new Intent(itemView.getContext(),OrderDetails.class);
                    Order selectedOrder = orderArrayList.get(index);
                    intent.putExtra("orderId",selectedOrder.getOrderId());
                    intent.putExtra("payment",selectedOrder.getPaymentMethod());
                    intent.putExtra("delivery",selectedOrder.getDeliveryOption());
                    intent.putExtra("totalprice",selectedOrder.getTotalPrice().toString());
                    //intent.putExtra("orderDate", selectedOrder.getOrderDate());
                    //intent.putExtra("custName", selectedOrder.getCustName());
                    //intent.putExtra("custPhone", selectedOrder.getCustPhone());
                    //intent.putExtra("custEmail", selectedOrder.getCustEmail());
                    //intent.putExtra("productImage",String.valueOf(selectedProduct.getUrl()));

                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
