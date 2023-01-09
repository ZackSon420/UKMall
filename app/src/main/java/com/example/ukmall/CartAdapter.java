package com.example.ukmall;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ukmall.utils.model.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private CartClickedListeners cartClickedListeners;
    private List<Item> itemCartList;

    public CartAdapter(CartClickedListeners cartClickedListeners) {
        this.cartClickedListeners = cartClickedListeners;
    }

    public void setItemCartList(List<Item> itemCartList) {
        this.itemCartList = itemCartList;
        notifyDataSetChanged();
    }

    TextView tv_QuantityProduct;

    int [] arr;

    //Nanti ganti dgn data dlm firebase
//    public CartAdapter(int[] arr) {
//
//        this.arr = arr;
//    }



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
        Item item = itemCartList.get(position);
       // holder.iv_product.setImageResource(item.getItemImage());
        Picasso.get().load(item.getItemImage()).into(holder.iv_product);
        holder.tv_prodname.setText(item.getItemName());
        holder.tv_prodprice.setText("RM"+item.getItemPrice()+"");
        holder.tv_QuantityProduct.setText(item.getQuantity() + "");
     //   holder.shoePriceTv.setText(item.getTotalItemPrice() + "");

        holder.iv_DeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartClickedListeners.onDeleteClicked(item);
            }
        });

        holder.btn_MinusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int qnt = 1;
//                if (qnt >= 1)
//                    qnt--;
//                else
//                    qnt = 0;
//                tv_QuantityProduct.setText(qnt);
                cartClickedListeners.onMinusClicked(item);
            }
        });

        holder.btn_AddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int qnt = 1;
//                qnt++;
//                tv_QuantityProduct.setText(qnt);

                cartClickedListeners.onPlusClicked(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (itemCartList == null) {
            return 0;
        } else {
            return itemCartList.size();
        }
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
            iv_DeleteProduct=itemView.findViewById(R.id.iv_delete);
            tv_QuantityProduct=itemView.findViewById(R.id.tv_quantityproduct);
            btn_MinusQuantity=itemView.findViewById(R.id.bt_minus_quantity);
            btn_AddQuantity=itemView.findViewById(R.id.bt_add_quantity);
        }

    }
    public interface CartClickedListeners {
        void onDeleteClicked(Item item);

        void onPlusClicked(Item item);

        void onMinusClicked(Item item);
    }


}
