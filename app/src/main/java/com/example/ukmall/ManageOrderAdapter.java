package com.example.ukmall;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ManageOrderAdapter extends RecyclerView.Adapter<ManageOrderAdapter.MyViewHolder> {

    Context context;
    static ArrayList<Order> orderArrayList;

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
        //holder.tvCustName.setText(order.);
        holder.tvOrderId.setText(order.getOrderId());
        //holder.tvOrderDate.setText(order.);
        holder.tvTotalOrder.setText("RM" + (order.getTotalPrice()).toString());
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
            tvCustName=itemView.findViewById(R.id.tv_custname);
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
                    /*Intent intent = new Intent(itemView.getContext(),ProductDetails.class);
                    Product selectedProduct = productArrayList.get(index);
                    intent.putExtra("productName",selectedProduct.getProductTitle());
                    intent.putExtra("productDesc",selectedProduct.getProductDescription());
                    intent.putExtra("productImage",String.valueOf(selectedProduct.getUrl()));
                    intent.putExtra("productImage2",String.valueOf(selectedProduct.getUrl2()));
//                    intent.putExtra("store",selectedProduct.getStore());
                    intent.putExtra("productPrice", "RM" + String.valueOf(selectedProduct.getOriginalPrice()));

                    itemView.getContext().startActivity(intent);

                     */
                }
            });
        }
    }
}
