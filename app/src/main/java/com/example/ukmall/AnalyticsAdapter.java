package com.example.ukmall;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AnalyticsAdapter extends RecyclerView.Adapter<AnalyticsAdapter.ProductAViewHolder> {

    int [] arr;

    public AnalyticsAdapter(int[] arr) {

        this.arr = arr;
    }

    @NonNull
    @Override
    public ProductAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productanalyticsview,parent,false);
        ProductAViewHolder productAViewHolder= new ProductAViewHolder(view);
        return productAViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAViewHolder holder, int position) {
        //set value into component
        holder.ivProduct.setImageResource(arr[position]);
        holder.tvProductName.setText("Brownies");
        holder.tvProductPrice.setText("RM10");
        holder.tvTotalUnitSold.setText("100");
        holder.tvTotalSale.setText("1000");

    }

    @Override
    public int getItemCount() {
        return arr.length;
    }

    public class ProductAViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProduct;
        TextView tvProductName,tvProductPrice, tvTotalUnitSold, tvTotalSale;

        public ProductAViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProduct = itemView.findViewById(R.id.img_product);
            tvProductName = itemView.findViewById(R.id.tv_nameproduct);
            tvProductPrice = itemView.findViewById(R.id.tv_priceproduct);
            tvTotalUnitSold = itemView.findViewById(R.id.tv_total_unit_sold);
            tvTotalSale = itemView.findViewById(R.id.tv_total_sale_product);
        }
    }

}
