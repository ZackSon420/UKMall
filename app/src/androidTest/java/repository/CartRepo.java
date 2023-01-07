package repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ukmall.Product;

public class CartRepo {
    private MutableLiveData<Double> mutableTotalPrice =new MutableLiveData<>();

    public CartRepo(Application application) {
    }


    public void updatePrice(int id, double price) {
    }

    public void updateQuantity(int id, double quantity) {
    }

    public void deleteCartItem(Product product) {
    }
}
