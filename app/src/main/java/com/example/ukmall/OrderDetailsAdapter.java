package com.example.ukmall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {

    Context context;
    ArrayList<Item> itemArrayList;

    //Constructor
    public OrderDetailsAdapter(Context context, ArrayList<Item> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderdetailsview,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Double totalProductSale = 0.0;
        Double price = 0.0;
        Double quantity = 0.0;
        Integer index = position+1;
        Item item = itemArrayList.get(position);
        holder.tvIndex.setText(index +".");
        holder.tvProductName.setText(item.getItemName());
        holder.tvProductQuantity.setText(item.getQuantity().toString());

        price = item.getItemPrice();
        quantity = item.getQuantity().doubleValue();
        totalProductSale = price*quantity;

        holder.tvProductSale.setText(totalProductSale.toString());

    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductName,tvProductQuantity, tvProductSale, tvIndex;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName=itemView.findViewById(R.id.tv_nameproduct);
            tvProductQuantity=itemView.findViewById(R.id.tv_quantityorder);
            tvProductSale=itemView.findViewById(R.id.tv_totalp_productOrder);
            tvIndex=itemView.findViewById(R.id.tv_index);
        }
    }
}
