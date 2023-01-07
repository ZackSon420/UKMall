package model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ukmall.Product;

import repository.CartRepo;

public class CartViewModel extends AndroidViewModel {

    private CartRepo cartRepo;
    public CartViewModel(@NonNull Application application) {
        super(application);
        cartRepo = new CartRepo(application);
    }

    public void updateQuantity(int id, double quantity){
        cartRepo.updateQuantity(id, quantity);
    }

    public void updatePrice(int id, double price){
        cartRepo.updatePrice(id, price);
    }

    public void deleteCartItem(Product product){
        cartRepo.deleteCartItem(product);
    }

}
