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

import java.util.List;

public class MakeOrderAdapter extends RecyclerView.Adapter<MakeOrderAdapter.MakeOrderViewHolder> {

    private List<Item> itemCartList;

    public MakeOrderAdapter(MakeOrder makeOrder) {

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
    public MakeOrderAdapter.MakeOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.makeorderproductview,parent,false);
        MakeOrderAdapter.MakeOrderViewHolder MakeOrderViewHolder = new MakeOrderAdapter.MakeOrderViewHolder(view);
        return MakeOrderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MakeOrderViewHolder holder, int position) {
        //Call data dari firebase nanti
        Item item = itemCartList.get(position);
        // holder.iv_product.setImageResource(item.getItemImage());
        Picasso.get().load(item.getItemImage()).into(holder.iv_product);
        holder.tv_prodname.setText(item.getItemName());
        holder.tv_prodprice.setText("RM"+item.getItemPrice()+"");
        holder.tv_QuantityProduct.setText(item.getQuantity() + "");
        //   holder.shoePriceTv.setText(item.getTotalItemPrice() + "");
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
            //iv_DeleteProduct=itemView.findViewById(R.id.iv_delete);
            tv_QuantityProduct=itemView.findViewById(R.id.tv_quantityproduct);
            //btn_MinusQuantity=itemView.findViewById(R.id.bt_minus_quantity);
            //btn_AddQuantity=itemView.findViewById(R.id.bt_add_quantity);
        }

    }


    /*public List<Item> selectedList;
    private Context context;

    public MakeOrderAdapter(Context context, List<Item> selectedList) {
        this.context = context;
        this.selectedList = selectedList;
    }

    @NonNull
    @Override
    public MakeOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View makeorderproductview = LayoutInflater.from(parent.getContext()).inflate(R.layout.makeorderproductview,null);
        MakeOrderViewHolder makeOrderViewHolder = new MakeOrderViewHolder(makeorderproductview);
        return makeOrderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MakeOrderViewHolder holder, int position) {

        holder.tvProductName.setText(selectedList.get(position).getItemName());
        holder.tvProductPrice.setText((int) selectedList.get(position).getItemPrice());
        holder.imgViewProductImage.setImageResource(Integer.parseInt(selectedList.get(position).getItemImage()));

    }

    @Override
    public int getItemCount() {
        return selectedList.size();
    }

    public class MakeOrderViewHolder extends RecyclerView.ViewHolder{

        public TextView tvProductName, tvProductPrice, tvQuantity;
        public ImageView imgViewProductImage;

        public MakeOrderViewHolder(@NonNull View itemView) {

            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_nameproduct);
            tvProductPrice = itemView.findViewById(R.id.tv_priceproduct);
            tvQuantity = itemView.findViewById(R.id.tv_quantityproduct);
            imgViewProductImage = itemView.findViewById(R.id.img_product);


        }
    }
     */

}
