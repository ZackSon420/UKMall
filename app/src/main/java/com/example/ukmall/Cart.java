package com.example.ukmall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ukmall.utils.model.Item;
import com.example.ukmall.viewmodel.CartViewModel;

import java.util.List;

public class Cart extends AppCompatActivity implements View.OnClickListener,CartAdapter.CartClickedListeners {

    private RecyclerView cartView;
    RecyclerView.LayoutManager cartlayoutManager;
    CartAdapter cartAdapter;
    private CartViewModel cartViewModel;
    private TextView totalCartPriceTV;

    Button bt_CheckOut;

    //Ganti dengan data dalam firebase
//    int[] arr = {R.drawable.brownies, R.drawable.brownies, R.drawable.brownies, R.drawable.brownies, R.drawable.brownies,
//            R.drawable.brownies, R.drawable.brownies, R.drawable.brownies};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        totalCartPriceTV=findViewById(R.id.tv_totalprice);
        bt_CheckOut=findViewById(R.id.bt_checkout);
        bt_CheckOut.setOnClickListener(this);
        //CartViewModel
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        //RecyclerView Cart
        cartView = findViewById(R.id.rv_cart);
        cartlayoutManager = new GridLayoutManager(this, 1);
        cartView.setLayoutManager(cartlayoutManager);
        cartAdapter = new CartAdapter(this);

        cartView.setAdapter(cartAdapter);
        cartView.setHasFixedSize(true);

        cartViewModel.getAllCartItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> shoeCarts) {
                double price = 0;
                cartAdapter.setItemCartList(shoeCarts);
                for (int i=0;i<shoeCarts.size();i++){
                    price = price + shoeCarts.get(i).getTotalItemPrice();
                }
                totalCartPriceTV.setText(String.valueOf(price));
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //Button checkout, nanti ganti dengan intent.
            case R.id.bt_checkout:
                Toast.makeText(this, "Checkout Button", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    public void onDeleteClicked(Item item) {
        cartViewModel.deleteCartItem(item);
    }

    @Override
    public void onPlusClicked(Item item) {
        int quantity = item.getQuantity() + 1;
        cartViewModel.updateQuantity(item.getItemID() , quantity);
        cartViewModel.updatePrice(item.getItemID() , quantity*item.getItemPrice());
        cartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMinusClicked(Item item) {
        int quantity = item.getQuantity() - 1;
        if (quantity != 0){
            cartViewModel.updateQuantity(item.getItemID() , quantity);
            cartViewModel.updatePrice(item.getItemID() , quantity*item.getItemPrice());
            cartAdapter.notifyDataSetChanged();
        }else{
            cartViewModel.deleteCartItem(item);
        }
    }
}