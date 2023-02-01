package com.example.ukmall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.MyViewHolder> {

    DecimalFormat df = new DecimalFormat("0.00");
    Context context;
    static ArrayList<user> sellerArrayList;

    public AdminAdapter(Context context, ArrayList<user> sellerArrayList) {
        this.context = context;
        this.sellerArrayList = sellerArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topsellerview,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        user user = sellerArrayList.get(position);
        //Ambil gambar dari link dalam product
        //Picasso.get().load(order.getUrl()).into(holder.ivCust);
        holder.tvSellerName.setText(user.getUserName());
        //holder.tvSellerId.setText(seller.getSellerId());
        holder.tvTotalProduct.setText(user.getTotalProduct().toString());              //set number product
        holder.tvTotalSales.setText("RM" + (df.format(user.getTotalSales())));         //set total sales
    }

    @Override
    public int getItemCount() {
        return sellerArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivSeller;
        TextView tvSellerName, tvSellerId, tvTotalProduct, tvTotalSales;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //ivSeller = itemView.findViewById(R.id.img_seller);
            tvSellerName = itemView.findViewById(R.id.tv_sellername);
            //tvSellerId = itemView.findViewById(R.id.tv_order_id);
            tvTotalProduct = itemView.findViewById(R.id.tv_totalsellerproduct);
            tvTotalSales = itemView.findViewById(R.id.tv_total_seller_sale);

        }
    }
}
