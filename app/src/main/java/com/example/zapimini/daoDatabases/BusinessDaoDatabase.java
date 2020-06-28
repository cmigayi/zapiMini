package com.example.zapimini.daoDatabases;

import android.content.Context;

import com.example.zapimini.daos.BusinessDao;
import com.example.zapimini.data.Business;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Business.class, exportSchema = false, version = 1)
public abstract class BusinessDaoDatabase extends RoomDatabase {
    private static final String DB_NAME = "business_db";
    private static BusinessDaoDatabase instance;

    public static synchronized BusinessDaoDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BusinessDaoDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract BusinessDao businessDao();
}
