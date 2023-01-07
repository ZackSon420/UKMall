package com.example.ukmall;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {


    TextView tv_QuantityProduct;

    int [] arr;

    //Nanti ganti dgn data dlm firebase
    public CartAdapter(int[] arr) {

        this.arr = arr;
    }



    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartproductview,parent,false);
        CartViewHolder cartViewHolder = new CartViewHolder(view);
        return cartViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        //Call data dari firebase nanti
        holder.iv_product.setImageResource(arr[position]);
        holder.tv_prodname.setText("Brownies (4pcs)");
        holder.tv_prodprice.setText("RM10");

        /*holder.iv_DeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.btn_MinusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qnt = 1;
                if (qnt >= 1)
                    qnt--;
                else
                    qnt = 0;
                tv_QuantityProduct.setText(qnt);
            }
        });

        holder.btn_AddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qnt = 1;
                qnt++;
                tv_QuantityProduct.setText(qnt);
            }
        }); */

    }

    @Override
    public int getItemCount() {
        return arr.length;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_product,btn_MinusQuantity, btn_AddQuantity,iv_DeleteProduct;
        TextView tv_prodname,tv_prodprice, tv_QuantityProduct;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            //Boleh refer cartproductview
            iv_product=itemView.findViewById(R.id.img_product);
            tv_prodname=itemView.findViewById(R.id.tv_nameproduct);
            tv_prodprice=itemView.findViewById(R.id.tv_priceproduct);

           /* iv_DeleteProduct=itemView.findViewById(R.id.iv_delete);
            tv_QuantityProduct=itemView.findViewById(R.id.tv_quantityproduct);
            btn_MinusQuantity=itemView.findViewById(R.id.bt_minus_quantity);
            btn_AddQuantity=itemView.findViewById(R.id.bt_add_quantity); */
        }

    }



}
