package com.example.ukmall.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ukmall.dao.CartDAO;
import com.example.ukmall.utils.model.Item;

@Database(entities = {Item.class}, version = 4)
public abstract class CartDatabase extends RoomDatabase {

    public abstract CartDAO cartDAO();
    private static CartDatabase instance;

    public static synchronized CartDatabase getInstance(Context context){
     if(instance==null){
         instance = Room.databaseBuilder(context.getApplicationContext()
                                        , CartDatabase.class, "ItemDatabase")
                                        .fallbackToDestructiveMigration()
                                        .build();
     }
     return instance;
    }
}
