package com.example.ukmall;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class AnalyticsAdapter extends RecyclerView.Adapter<AnalyticsAdapter.ProductAViewHolder> {

//    int [] arr;
    List<Product> productList;

    DecimalFormat df = new DecimalFormat("0.00");
    public AnalyticsAdapter(List<Product> productList) {

        this.productList = productList;
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

        Product product = productList.get(position);
        //set value into component
        Picasso.get().load(product.getUrl()).into(holder.ivProduct);
        holder.tvProductName.setText(product.getProductTitle());
        holder.tvProductPrice.setText("RM" + df.format(product.getOriginalPrice()));
        holder.tvTotalUnitSold.setText(""+product.getBought());
        holder.tvTotalSale.setText("RM"+ df.format(product.getTotalSale()));

    }

    @Override
    public int getItemCount() {
        return productList.size();
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
