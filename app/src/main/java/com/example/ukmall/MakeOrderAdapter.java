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

import java.text.DecimalFormat;
import java.util.List;

public class MakeOrderAdapter extends RecyclerView.Adapter<MakeOrderAdapter.MakeOrderViewHolder> {
    DecimalFormat df = new DecimalFormat("0.00");
    private List<Item> itemCartList;

    public MakeOrderAdapter(MakeOrder makeOrder) {

    }

    public MakeOrderAdapter(Receipt receipt) {

    }

    public void setItemCartList(List<Item> itemCartList) {
        this.itemCartList = itemCartList;
        notifyDataSetChanged();
    }

    TextView tv_QuantityProduct;

    int [] arr;


    @NonNull
    @Override
    public MakeOrderAdapter.MakeOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.makeorderproductview,parent,false);
        MakeOrderAdapter.MakeOrderViewHolder MakeOrderViewHolder = new MakeOrderAdapter.MakeOrderViewHolder(view);
        return MakeOrderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MakeOrderViewHolder holder, int position) {
        //Call data dari firebase nanti
        Item item = itemCartList.get(position);
        Picasso.get().load(item.getItemImage()).into(holder.iv_product);
        holder.tv_prodname.setText(item.getItemName());
        holder.tv_prodprice.setText("RM"+df.format(item.getItemPrice()));
        holder.tv_QuantityProduct.setText(item.getQuantity() + "");
    }

    @Override
    public int getItemCount() {
        if (itemCartList == null) {
            return 0;
        } else {
            return itemCartList.size();
        }
    }

    public class MakeOrderViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_product,btn_MinusQuantity, btn_AddQuantity,iv_DeleteProduct;
        TextView tv_prodname,tv_prodprice, tv_QuantityProduct;


        public MakeOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            //Boleh refer cartproductview
            iv_product=itemView.findViewById(R.id.img_product);
            tv_prodname=itemView.findViewById(R.id.tv_nameproduct);
            tv_prodprice=itemView.findViewById(R.id.tv_priceproduct);
            tv_QuantityProduct=itemView.findViewById(R.id.tv_quantityproduct);

        }

    }


}
