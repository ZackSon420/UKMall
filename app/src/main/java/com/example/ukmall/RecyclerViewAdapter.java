package com.example.ukmall;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    //Array ganti dgn product kat firebase
    int [] arr;

    public RecyclerViewAdapter(int[] arr) {

        this.arr = arr;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productview,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Link dengan Firebase
        holder.iv_product.setImageResource(arr[position]);
        holder.tv_prodname.setText("Brownies (4pcs)");
        holder.tv_prodprice.setText("RM10");
    }

    @Override
    public int getItemCount() {
        return arr.length; //Length items dalam firebase
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        ImageView iv_product;
        TextView tv_prodname,tv_prodprice;
        //Kena create lagi satu untuk rating

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_product=itemView.findViewById(R.id.img_product);
            tv_prodname=itemView.findViewById(R.id.tv_nameproduct);
            tv_prodprice=itemView.findViewById(R.id.tv_priceproduct);
        }
    }
}
