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

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context context;
    Product product;

    ArrayList<Product> productArrayList;
    //Array ganti dgn product kat firebase
    int [] arr;

    public RecyclerViewAdapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

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
//        holder.iv_product.setImageResource(arr[position]);
//        holder.tv_prodname.setText("Brownies (4pcs)");
//        holder.tv_prodprice.setText("RM10");

        product = productArrayList.get(position);
        holder.tv_prodname.setText(product.name);
        holder.tv_prodprice.setText(String.valueOf(product.price));

    }

    @Override
    public int getItemCount() {
//      return arr.length; //Length items dalam firebase
        return productArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_product;
        TextView tv_prodname,tv_prodprice;
        //Kena create lagi satu untuk rating

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_product=itemView.findViewById(R.id.img_product);
            tv_prodname=itemView.findViewById(R.id.tv_nameproduct);
            tv_prodprice=itemView.findViewById(R.id.tv_priceproduct);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(),ProductDetails.class);
                    intent.putExtra("productName",product.getName());
                    intent.putExtra("productPrice",product.getPrice());

                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

}
