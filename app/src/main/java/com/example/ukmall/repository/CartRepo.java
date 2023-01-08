package com.example.ukmall.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.ukmall.dao.CartDAO;
import com.example.ukmall.database.CartDatabase;
import com.example.ukmall.utils.model.Item;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CartRepo {
    private CartDAO cartDAO;
    private LiveData<List<Item>> allCartItemsLiveData;
    private Executor executor = Executors.newSingleThreadExecutor();

    public LiveData<List<Item>> getAllCartItemsLiveData() {
        return allCartItemsLiveData;
    }

    public CartRepo(Application application){
        cartDAO = CartDatabase.getInstance(application).cartDAO();
        allCartItemsLiveData = cartDAO.getAllCartItems();
    }

    public void insertCartItem(Item item){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.insertCartItem(item);
            }
        });
    }

    public void deleteCartItem(Item item){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.deleteCartItem(item);
            }
        });
    }

    public void updateQuantity(int id , int quantity) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.updateQuantity(id, quantity);
            }
        });
    }

    public void updatePrice(int id , double price){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.updatePrice(id , price);
            }
        });
    }

    public void deleteAllCartItems(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.deleteAllItems();
            }
        });
    }

}
