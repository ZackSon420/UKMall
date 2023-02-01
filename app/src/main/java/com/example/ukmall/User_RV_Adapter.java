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

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User_RV_Adapter extends RecyclerView.Adapter<User_RV_Adapter.MyViewHolder> {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DecimalFormat df = new DecimalFormat("0.00");
    private LayoutInflater inflater;
    Context context;

    ArrayList<Product> productArrayList;
    //Array ganti dgn product kat firebase
    int [] arr;

    public User_RV_Adapter(Context context, ArrayList<Product> productArrayList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.productArrayList = productArrayList;
    }

    public User_RV_Adapter(int[] arr) {

        this.arr = arr;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userproductview,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        Product product = productArrayList.get(position);
        //Ambil gambar dari link dalam product
        Picasso.get().load(product.getUrl()).into(holder.iv_product);
        holder.tv_prodname.setText(product.productTitle);
        holder.tv_prodprice.setText("RM" + df.format(product.originalPrice));


    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_product;
        TextView tv_prodname,tv_prodprice;
        ImageView iv_deletebt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_product=itemView.findViewById(R.id.img_product);
            tv_prodname=itemView.findViewById(R.id.tv_nameproduct);
            tv_prodprice=itemView.findViewById(R.id.tv_priceproduct);
            iv_deletebt=itemView.findViewById(R.id.iv_deletebutton);

            //call selected product data
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Product product = productArrayList.get(position);

                    String productID = product.getProductId();
                    String productImage = product.getUrl();
                    String productName = product.getProductTitle();
                    String productDesc = product.getProductDescription();
                    String productCategory = product.getProductCategory();
                    Integer productQuantity = product.getProductQuantity();
                    Double productPrice = product.getOriginalPrice();

                    Toast.makeText(context, "Selected Product: " + productName, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, EditProductActivity.class);
                    intent.putExtra("productId", productID);
                    intent.putExtra("url", productImage);
                    intent.putExtra("productTitle", productName);
                    intent.putExtra("productDescription", productDesc);
                    intent.putExtra("productCategory", productCategory);
                    intent.putExtra("productQuantity", productQuantity);

                    intent.putExtra("productPrice", productPrice);

                    context.startActivity(intent);
                }
            });

            //Delete selected product data
            iv_deletebt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    Product selectedProduct = productArrayList.get(index);
                    // Show a confirmation dialog
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Delete Product")
                            .setMessage("Are you sure you want to delete this product?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Delete the product from Firestore
                                    db.collection("product").document(selectedProduct.getProductId()).delete();
                                    // Remove the product from the ArrayList
                                    productArrayList.remove(index);
                                    // Notify the adapter to update the RecyclerView
                                    notifyItemRemoved(index);
                                    notifyItemRangeChanged(index, productArrayList.size());
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .show();
                }
            });





        }
    }


}
