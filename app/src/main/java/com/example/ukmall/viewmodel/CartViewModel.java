package com.example.ukmall.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ukmall.repository.CartRepo;
import com.example.ukmall.utils.model.Item;

import java.util.List;

public class CartViewModel extends AndroidViewModel {

    private CartRepo cartRepo;

    public CartViewModel(@NonNull Application application) {
        super(application);
        cartRepo = new CartRepo(application);
    }

    public LiveData<List<Item>> getAllCartItems() {
        return cartRepo.getAllCartItemsLiveData();
    }

    public void insertCartItem(Item item) {
        cartRepo.insertCartItem(item);
    }

    public void updateQuantity(int id, int quantity) {
        cartRepo.updateQuantity(id, quantity);
    }

    public void updatePrice(int id, double price) {
        cartRepo.updatePrice(id, price);
    }

    public void deleteCartItem(Item item) {
        cartRepo.deleteCartItem(item);
    }

    public void deleteAllCartItems() {
        cartRepo.deleteAllCartItems();
    }

}
