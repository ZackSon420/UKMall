package com.example.ukmall.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ukmall.utils.model.Item;

import java.util.List;

@Dao
public interface CartDAO {

    @Insert
    void insertCartItem(Item item);

    @Query("Select * from Items")
    LiveData<List<Item> > getAllCartItems();

    @Delete
    void deleteCartItem(Item item);

    @Query("UPDATE Items SET quantity=:quantity WHERE itemID=:id ")
    void updateQuantity(int id,int quantity);

    @Query("UPDATE Items SET totalItemPrice=:totalItemPrice WHERE itemID=:id ")
    void updatePrice(int id,double totalItemPrice);
    

    @Query("DELETE FROM Items")
    void deleteAllItems();
}
